package ru.lostgirl.rfidinventory.ui.rfidscan

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.Slider
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.activities.MainActivity
import ru.lostgirl.rfidinventory.databinding.FragmentRfidScannerBinding
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState
import ru.lostgirl.rfidinventory.mvi.MviFragment
import ru.lostgirl.rfidinventory.ui.list.InventoryItemsFilterableAdapter
import ru.lostgirl.rfidinventory.utils.OnItemClickListener
import ru.lostgirl.rfidinventory.utils.UIHelper
import ru.lostgirl.rfidinventory.utils.UIHelper.refreshToggleButton
import ru.lostgirl.rfidinventory.utils.UIHelper.setOnCheckedChangeListenerToFilterButton
import timber.log.Timber

const val MAX_STREAM_COUNT = 10

class RfidScannerFragment :
    MviFragment<RfidScannerViewState, RfidScannerViewEffect, RfidScannerViewEvent, RfidScannerViewModel>() {
    override val viewModel by viewModel<RfidScannerViewModel>()
    private lateinit var binding: FragmentRfidScannerBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter
    private lateinit var soundPool: SoundPool
    private lateinit var am: AudioManager
    private var canBackPress: Boolean = true
    private var activeReadyDialog: androidx.appcompat.app.AlertDialog? = null
    private val args: RfidScannerFragmentArgs by navArgs()
    private var soundMap: HashMap<Int, Int> = HashMap()
    private val onBacPressedCallBack =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (canBackPress) {
                    findNavController().popBackStack(R.id.inventoryListFragment, false)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, onBacPressedCallBack)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRfidScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationID = args.locationId
        val locationName = args.title
        val scannerType = args.scannerType
        initSetting()
        initRcView()
        initFilterButtons()
        lifecycle.addObserver(viewModel)
        viewModel.process(RfidScannerViewEvent.SetScannerType(scannerType))
        viewModel.process(RfidScannerViewEvent.SetCurrentLocation(locationID, locationName))
        initSound()
    }

    override fun renderViewEffect(viewEffect: RfidScannerViewEffect) {
        when (viewEffect) {
            is RfidScannerViewEffect.InventoryReady -> {
                inventoryReady(viewEffect.message)
            }

            is RfidScannerViewEffect.ShowToast -> {
                showToast(
                    viewEffect.message,
                    viewEffect.errorMessage,
                    viewEffect.isError
                )
            }

            is RfidScannerViewEffect.PlaySound -> {
                playSound(viewEffect.id)
            }

            is RfidScannerViewEffect.ShowAlertDialog -> showAlertDialog(
                viewEffect.message,
                viewEffect.onOkClickListener
            )

            is RfidScannerViewEffect.ShowSettingsAlertDialog -> {
                showAlertSettingsDialog(
                    viewEffect.itemState,
                    viewEffect.onOkClickListener
                )
            }
        }
    }

    override fun renderViewState(viewState: RfidScannerViewState) = with(binding) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(viewState.canBackPress)
            actionBar.setDisplayHomeAsUpEnabled(viewState.canBackPress)
            actionBar.setDisplayShowHomeEnabled(viewState.canBackPress)
            actionBar.title = "${viewState.scannerType ?: ""}  ${viewState.currentLocationName}"
            if (viewState.canBackPress) {
                ((activity as AppCompatActivity) as MainActivity).binding.myToolbar.setNavigationIcon(R.drawable.ico_back)
            }
        }

        errorText.visibility =
            if (!viewState.isReaderInit && viewState.scannerType != null) View.VISIBLE else View.GONE
        startRfidButton.visibility =
            if (viewState.startScanningButtonVisible) View.VISIBLE else View.GONE
        stopRfidButton.visibility =
            if (viewState.stopScanningButtonVisible) View.VISIBLE else View.GONE
        scanningBlock.visibility =
            if (viewState.stopScanningButtonVisible) View.VISIBLE else View.GONE

        progressPercentText.text = viewState.inventoryState.percentScanningString
        progressBar.progress = viewState.inventoryState.percentScanningFound

        settingPanel.visibility = if (viewState.panelSettingsVisible) {
            View.VISIBLE
        } else {
            if (!viewState.isReaderInit) View.INVISIBLE else View.GONE
        }
        scannerPowerValueCaption.text =
            getString(R.string.current_power, viewState.scannerPowerValue)
        scannerPowerSlider.value = when (viewState.scannerPowerValue in 0..100) {
            true -> viewState.scannerPowerValue.toFloat()
            else -> {
                if (viewState.scannerPowerValue < 0) 1F else 100F
            }
        }

        canBackPress = viewState.canBackPress
        refreshToggleButton(
            filterButtonNotFound,
            viewState.inventoryState.countNotFoundString
        )
        refreshToggleButton(filterButtonFound, viewState.inventoryState.countFoundString)
        refreshToggleButton(
            filterButtonFoundInWrongPlace,
            viewState.inventoryState.countFoundInWrongPlaceString
        )
        val filter = getFilterString()
        adapter.submitListWithFilter(
            viewState.inventoryItems,
            filter
        )
    }

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    private fun showAlertDialog(msg: Int, onOkClickListener: () -> Unit) {
        UIHelper.alertDialog(requireContext(), getString(msg)) {
            onOkClickListener()
        }
    }

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    private fun showAlertSettingsDialog(
        itemState: InventoryItemState,
        onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) {
        UIHelper.alertSettingsDialog(requireContext(), itemState, onOkClickListener)
    }

    // Оповещение о выполнении инвентаризации (все метки найдены)
    private fun inventoryReady(message: Int) {
        if (activeReadyDialog == null) {
            activeReadyDialog = UIHelper.detailDialog(
                requireContext(),
                getString(message),
                R.drawable.green_background,
                true,
                R.color.textColor,
                true
            )
        }
    }

    // Инициализация AudioManager для звуков
    private fun initSound() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_STREAM_COUNT)
            .build()
        soundMap[1] = soundPool.load(context, R.raw.barcodebeep, 1)
        soundMap[2] = soundPool.load(context, R.raw.serror, 1)
        am = context?.getSystemService(AUDIO_SERVICE) as AudioManager // 实例化AudioManager对象
    }

    // Произрывание звукового сигнала
    private fun playSound(id: Int) {
        val audioMaxVolume =
            am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val audioCurrentVolume =
            am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val volumeRatio = audioCurrentVolume / audioMaxVolume
        try {
            soundPool.play(
                soundMap[id]!!,
                volumeRatio,
                volumeRatio,
                1,
                0,
                1f
            )
        } catch (e: Exception) {
            Timber.d("InvMobRFID", e.toString())
        }
    }

    // Инициализация кнопок и слайдера настройки мощности
    private fun initSetting() {
        binding.startRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(true))
        }
        binding.stopRfidButton.setOnClickListener {
            viewModel.process(RfidScannerViewEvent.SetScanningState(false))
        }
        binding.scannerPowerSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.process(RfidScannerViewEvent.SetScanningPower(slider.value.toInt()))
            }
        })
        binding.scannerPowerSlider.setLabelFormatter { value ->
            value.toInt().toString()
        }
    }

    // Инициализация RecyclerView
    private fun initRcView() = with(binding) {
        rcItems.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                }

                override fun onLongClick(item: InventoryItemForListModel): Boolean {
                    viewModel.process(
                        RfidScannerViewEvent.ShowSettingsAlertDialog(item.state) { resetState: Boolean, setStateFound: Boolean, comment: String ->
                            if (comment.isNotEmpty()) {
                                viewModel.process(
                                    RfidScannerViewEvent.SetCommentInventoryItem(
                                        item, comment
                                    )
                                )
                            }
                            if (setStateFound) {
                                viewModel.process(
                                    RfidScannerViewEvent.SetFoundInventoryItemState(
                                        item
                                    )
                                )
                            }
                            if (resetState) {
                                viewModel.process(
                                    RfidScannerViewEvent.ResetInventoryItemState(
                                        item
                                    )
                                )
                            }
                        }
                    )

                    return true
                }
            },
            getString(R.string.filter_delimetr)
        )
        rcItems.adapter = adapter
    }

    // Инициализация фильтров по статусам
    private fun initFilterButtons() {
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonNotFound,
            getString(R.string.tmc_not_found),
            R.drawable.red_background
        ) { filterData() }
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonFound,
            getString(R.string.tmc_found),
            R.drawable.green_background
        ) { filterData() }
        setOnCheckedChangeListenerToFilterButton(
            requireContext(),
            binding.filterButtonFoundInWrongPlace,
            getString(R.string.tmc_found_in_wrong_place),
            R.drawable.orange_background
        ) { filterData() }
    }

    // Получение актуального фильтра для адаптера
    private fun getFilterString(): String =
        listOf(
            "",
            binding.filterButtonNotFound.isChecked.toString(),
            binding.filterButtonFound.isChecked.toString(),
            binding.filterButtonFoundInWrongPlace.isChecked.toString()
        ).joinToString(getString(R.string.filter_delimetr))

    // Фильтрация данных с списке ТМЦ
    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

    // Показать Toast
    private fun showToast(message: Int, errorMessage: Int, isError: Boolean = false) {
        UIHelper.showToastMessage(
            requireContext(),
            if (isError) getString(errorMessage) else getString(message),
            if (isError) R.color.toast_red_text else R.color.toast_green_text
        )
    }
}
