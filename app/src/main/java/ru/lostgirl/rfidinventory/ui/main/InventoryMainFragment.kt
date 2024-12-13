package ru.lostgirl.rfidinventory.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.databinding.FragmentInventoryMainBinding
import ru.lostgirl.rfidinventory.mvi.MviFragment
import ru.lostgirl.rfidinventory.utils.ExcelUtil
import ru.lostgirl.rfidinventory.utils.UIHelper.alertDialog
import ru.lostgirl.rfidinventory.utils.UIHelper.alertProcessDialog
import ru.lostgirl.rfidinventory.utils.UIHelper.showToastMessage

class InventoryMainFragment :
    MviFragment<InventoryMainViewState, InventoryMainViewEffect, InventoryMainViewEvent, InventoryMainViewModel>() {
    override val viewModel by viewModel<InventoryMainViewModel>()
    private lateinit var binding: FragmentInventoryMainBinding
    private var activeProcessDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInventoryMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
        viewModel.process(InventoryMainViewEvent.RefreshData)
    }

    override fun renderViewEffect(viewEffect: InventoryMainViewEffect) {
        when (viewEffect) {
            is InventoryMainViewEffect.ShowAlertDialog -> showAlertDialog(
                viewEffect.message,
                viewEffect.onOkClickListener
            )

            is InventoryMainViewEffect.ShowAlertProcessDialog ->
                showProcessDialog(viewEffect.message, viewEffect.processEvent)

            is InventoryMainViewEffect.ShowToast -> showToast(
                viewEffect.message,
                viewEffect.errorMessage,
                viewEffect.isError
            )

            is InventoryMainViewEffect.HideAlertProcessDialog -> {
                viewEffect.processDialog?.dismiss()
            }

            InventoryMainViewEffect.OpenFileManager -> openFileManager()
        }
    }

    override fun renderViewState(viewState: InventoryMainViewState) = with(binding) {
        progressPercentText.text = viewState.inventoryState.percentFoundString
        progressBar.progress = viewState.inventoryState.percentFound
        countAllText.text = viewState.inventoryState.countAllString
        countFoundText.text = viewState.inventoryState.countFoundString
        countNotFoundText.text = viewState.inventoryState.countNotFoundString
        countFoundInWrongPlaceText.text = viewState.inventoryState.countFoundInWrongPlaceString
        mainInfoBlock.visibility = if (viewState.mainInfoBlockVisible) View.VISIBLE else View.GONE
        openInventoryButton.visibility =
            if (viewState.openInventoryButtonVisible) View.VISIBLE else View.GONE
        closeInventoryButton.visibility =
            if (viewState.closeInventoryButtonVisible) View.VISIBLE else View.GONE
        exportButton.visibility = if (viewState.exportButtonVisible) View.VISIBLE else View.GONE
        loadFromFileButton.visibility =
            if (viewState.loadFromFileButtonVisible) View.VISIBLE else View.GONE
        loadFromApiButton.visibility =
            if (viewState.loadFromFileButtonVisible) View.VISIBLE else View.GONE
        loadingWarningText.visibility =
            if (viewState.loadingWarningTextVisible) View.VISIBLE else View.GONE
        progressPercentText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                viewState.progressPercentTextColor
            )
        )
    }

    // Инициализация кнопок
    private fun initButtons() = with(binding) {
        openInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_inventoryMainFragment_to_inventoryListFragment)
        }
        loadFromFileButton.setOnClickListener {
            viewModel.process(InventoryMainViewEvent.OpenFileManager)
        }
        loadFromApiButton.setOnClickListener {
            viewModel.process(
                InventoryMainViewEvent.ShowProcessDialog(
                    R.string.file_load_action_message,
                    InventoryMainViewEvent.LoadDataFromApi()
                )
            )
        }
        exportButton.setOnClickListener {
            viewModel.process(
                InventoryMainViewEvent.ShowProcessDialog(
                    R.string.file_save_action_message,
                    InventoryMainViewEvent.SaveDataToFile()
                )
            )
        }
        closeInventoryButton.setOnClickListener {
            viewModel.process(
                InventoryMainViewEvent.ShowAlertDialog(R.string.clear_all_message) {
                    viewModel.process(
                        InventoryMainViewEvent.ShowProcessDialog(
                            R.string.file_save_action_message,
                            InventoryMainViewEvent.CloseInventory()
                        )
                    )
                }
            )
        }
    }

    // Отображение Toast
    private fun showToast(message: Int, errorMessage: Int, isError: Boolean = false) {
        showToastMessage(
            requireContext(),
            if (isError) getString(errorMessage) else getString(message),
            if (isError) R.color.toast_red_text else R.color.toast_green_text
        )
    }

    // Показать ProcessDialog при выполнении долгих расчетов, в параметр передаем Event,
    // который запустится после начала отображения ProcessDialog
    private fun showProcessDialog(message: Int, processEvent: InventoryMainViewEvent) {
        val dialog = alertProcessDialog(
            requireContext(),
            getString(message)
        )
        activeProcessDialog = dialog
        dialog.show()
        when (processEvent) {
            is InventoryMainViewEvent.LoadDataFromFile -> {
                viewModel.process(
                    processEvent.copy(processDialog = activeProcessDialog)
                )
            }

            is InventoryMainViewEvent.SaveDataToFile -> {
                viewModel.process(
                    processEvent.copy(processDialog = activeProcessDialog)
                )
            }

            is InventoryMainViewEvent.CloseInventory -> {
                viewModel.process(
                    processEvent.copy(processDialog = activeProcessDialog)
                )
            }

            is InventoryMainViewEvent.LoadDataFromApi -> {
                viewModel.process(
                    processEvent.copy(processDialog = activeProcessDialog)
                )
            }

            else -> {
                dialog.dismiss()
                showToast(R.string.data_error_load_message, R.string.data_error_load_message, true)
            }
        }
    }

    // Показать AlertDialog для подтверждения действия (onOkClickListener) или подсказки
    private fun showAlertDialog(msg: Int, onOkClickListener: () -> Unit) {
        alertDialog(requireContext(), getString(msg)) {
            onOkClickListener()
        }
    }

    // Launcher с настройкой обработки файла для загрузки данных
    private val openExcelFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val excelFileUri: Uri? = data?.data
                excelFileUri?.let { uri ->
                    activity?.let { activity ->
                        viewModel.process(
                            InventoryMainViewEvent.ShowProcessDialog(
                                R.string.file_load_action_message,
                                InventoryMainViewEvent.LoadDataFromFile(
                                    uri,
                                    activity.contentResolver
                                )
                            )
                        )
                    }
                }
            }
        }

    // Открыть окно выбора файл для загрузки
    private fun openFileManager() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        val mimetypes = arrayOf(
            ExcelUtil.MEM_TYPE_XLS,
            ExcelUtil.MEM_TYPE_XLSX
        )
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        openExcelFileLauncher.launch(intent)
    }
}

