package ru.lostgirl.rfidinventory.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.data.readers.ReaderType
import ru.lostgirl.rfidinventory.databinding.FragmentInventoryListBinding
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState
import ru.lostgirl.rfidinventory.domain.models.InventoryLocationFullModel
import ru.lostgirl.rfidinventory.mvi.MviFragment
import ru.lostgirl.rfidinventory.utils.OnItemClickListener
import ru.lostgirl.rfidinventory.utils.UIHelper
import ru.lostgirl.rfidinventory.utils.UIHelper.refreshToggleButton
import ru.lostgirl.rfidinventory.utils.UIHelper.setOnCheckedChangeListenerToFilterButton
import ru.lostgirl.rfidinventory.utils.searchablespinner.OnItemSelectListener
import ru.lostgirl.rfidinventory.utils.searchablespinner.SearchableSpinner

class InventoryListFragment :
    MviFragment<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent, InventoryListViewModel>() {

    override val viewModel by viewModel<InventoryListViewModel>()
    private lateinit var binding: FragmentInventoryListBinding
    private lateinit var adapter: InventoryItemsFilterableAdapter
    private val locationListName = ArrayList<String>()
    private val locationList = ArrayList<InventoryLocationFullModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInventoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initSearch()
        initButtons()
        initFilterButtons()
        initRcView()
        lifecycle.addObserver(viewModel)
    }

    override fun renderViewEffect(viewEffect: InventoryListViewEffect) {
        when (viewEffect) {
            is InventoryListViewEffect.ShowAlertDialog -> showAlertDialog(
                viewEffect.message,
                viewEffect.onOkClickListener
            )

            is InventoryListViewEffect.ShowSettingsAlertDialog -> {
                showAlertSettingsDialog(
                    viewEffect.itemState,
                    viewEffect.onOkClickListener
                )
            }
        }
    }

    override fun renderViewState(viewState: InventoryListViewState) = with(binding) {
        val filter = getFilterString()
        adapter.submitListWithFilter(viewState.inventoryItems, filter)
        for (location in viewState.locations) {
            if (locationList.none { it.id == location.id!! }) {
                locationList.add(location)
                locationListName.add(location.name)
            }
        }
        binding.locationSpinner.text =
            locationList.firstOrNull { it.id == viewState.currentLocationID }?.name ?: ""
        refreshToggleButton(filterButtonNotFound, viewState.inventoryState.countNotFoundString)
        refreshToggleButton(filterButtonFound, viewState.inventoryState.countFoundString)
        refreshToggleButton(
            filterButtonFoundInWrongPlace,
            viewState.inventoryState.countFoundInWrongPlaceString
        )
        filterData()
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

    // Инициализация RecyclerView
    private fun initRcView() = with(binding) {
        rcInventoryList.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemsFilterableAdapter(
            object : OnItemClickListener<InventoryItemForListModel> {
                override fun onItemClick(item: InventoryItemForListModel) {
                    openItemDetail(item)
                }

                override fun onLongClick(item: InventoryItemForListModel): Boolean {
                    viewModel.process(
                        InventoryListViewEvent.ShowSettingsAlertDialog(item.state) { resetState: Boolean, setStateFound: Boolean, comment: String ->
                            if (comment.isNotEmpty()) {
                                viewModel.process(
                                    InventoryListViewEvent.SetCommentInventoryItem(
                                        item, comment
                                    )
                                )
                            }
                            if (setStateFound) {
                                viewModel.process(
                                    InventoryListViewEvent.SetFoundInventoryItemState(
                                        item
                                    )
                                )
                            }
                            if (resetState) {
                                viewModel.process(
                                    InventoryListViewEvent.ResetInventoryItemState(
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
        rcInventoryList.adapter = adapter
    }

    // Открытие окна с детализацией по ТМЦ
    private fun openItemDetail(item: InventoryItemForListModel) {
        val action =
            InventoryListFragmentDirections.actionInventoryListFragmentToInventoryItemDetailFragment(
                item.model,
                item.id!!,
                item.state
            )
        findNavController().navigate(action)
    }

    // Открытие окна для сканирования, доступно только если выбран фильтр по местоположению
    private fun openScannerFragment(type: ReaderType) {
        val title = binding.locationSpinner.text
        val locationID = locationList.firstOrNull { it.name == title }?.id ?: 0
        if (locationID < 1) {
            UIHelper.detailDialog(
                requireContext(),
                getString(R.string.you_need_choose_location_file),
                0,
                false,
                R.color.textColor
            )
        } else {
            val action =
                InventoryListFragmentDirections.actionInventoryListFragmentToRfidScannerFragment(
                    title.toString(),
                    locationID,
                    type
                )
            findNavController().navigate(action)
        }
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

    // Инициализация кнопок для перехода в режим сканирования
    private fun initButtons() {
        binding.openRfidScannerButton.setOnClickListener { openScannerFragment(ReaderType.RFID) }
        binding.open2dScannerButton.setOnClickListener { openScannerFragment(ReaderType.BARCODE_2D) }
    }

    // Получение актуального фильтра для адаптера
    private fun getFilterString(): String =
        listOf(
            binding.searchView.query.toString(),
            binding.filterButtonNotFound.isChecked.toString(),
            binding.filterButtonFound.isChecked.toString(),
            binding.filterButtonFoundInWrongPlace.isChecked.toString()
        ).joinToString(getString(R.string.filter_delimetr))

    // Фильтрация данных с списке ТМЦ
    private fun filterData() {
        val filter = getFilterString()
        adapter.filter.filter(filter)
    }

    // Инициализация SearchView
    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterData()
                return false
            }
        })
        binding.searchView.clearFocus()
    }

    // Инициализация SearchableSpinner выбор местоположения
    private fun initSpinner() {
        val searchableSpinner = SearchableSpinner(requireContext())
        searchableSpinner.windowTitle = getString(R.string.location_search)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                val newLocation = locationList.firstOrNull { it.name == selectedString }?.id ?: 0
                refreshInventoryData(newLocation)
            }
        }
        searchableSpinner.setSpinnerListItems(locationListName)
        binding.locationSpinner.setOnClickListener {
            searchableSpinner.show()
        }
    }

    // Обновление информации об инвентаризации по местоположению
    fun refreshInventoryData(locationID: Int) {
        viewModel.process(InventoryListViewEvent.EditCurrentLocation(locationID))
    }
}
