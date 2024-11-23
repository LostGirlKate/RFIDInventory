package ru.lostgirl.rfidinventory.ui.list

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState
import ru.lostgirl.rfidinventory.domain.usecase.GetAllLocationsUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryInfoUseCase
import ru.lostgirl.rfidinventory.domain.usecase.GetInventoryItemByLocationIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.ResetLocationInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetCommentInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.domain.usecase.SetFoundInventoryItemByIDUseCase
import ru.lostgirl.rfidinventory.mvi.MviViewModel

class InventoryListViewModel(
    private val getInventoryItemByLocationIDUseCase: GetInventoryItemByLocationIDUseCase,
    private val getInventoryInfo: GetInventoryInfoUseCase,
    private val resetLocationInventoryItemByID: ResetLocationInventoryItemByIDUseCase,
    private val setFoundInventoryItemByIDUseCase: SetFoundInventoryItemByIDUseCase,
    private val setCommentInventoryItemByIDUseCase: SetCommentInventoryItemByIDUseCase,
    application: Application,
    getAllLocationsUseCase: GetAllLocationsUseCase,
) : MviViewModel<InventoryListViewState, InventoryListViewEffect, InventoryListViewEvent>(
    application
),
    DefaultLifecycleObserver {

    init {
        viewState = InventoryListViewState(
            locations = getAllLocationsUseCase.execute()
        )
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(viewState.currentLocationID),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocationID
            )
        )
    }

    override fun process(viewEvent: InventoryListViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is InventoryListViewEvent.EditCurrentLocation -> {
                editCurrentLocation(viewEvent.locationID)
            }

            is InventoryListViewEvent.ResetInventoryItemState -> {
                resetLocationInventory(viewEvent.item)
            }

            is InventoryListViewEvent.ShowAlertDialog -> {
                showAlertDialog(
                    viewEvent.message,
                    viewEvent.onOkClickListener
                )
            }

            is InventoryListViewEvent.SetCommentInventoryItem -> {
                setCommentInventoryItem(viewEvent.item, viewEvent.comment)
            }

            is InventoryListViewEvent.SetFoundInventoryItemState -> {
                setFoundInventoryItemState(viewEvent.item)
            }

            is InventoryListViewEvent.ShowSettingsAlertDialog -> {
                showSettingsAlertDialog(
                    viewEvent.itemState,
                    viewEvent.onOkClickListener
                )
            }
        }
    }

    private fun showAlertDialog(message: Int, onOkClickListener: () -> Unit) {
        viewEffect = InventoryListViewEffect.ShowAlertDialog(message, onOkClickListener)
    }

    private fun showSettingsAlertDialog(
        itemState: InventoryItemState,
        onOkClickListener: (resetState: Boolean, setStateFound: Boolean, comment: String) -> Unit,
    ) {
        viewEffect = InventoryListViewEffect.ShowSettingsAlertDialog(itemState, onOkClickListener)
    }

    // сброс статуса ТМЦ
    private fun resetLocationInventory(item: InventoryItemForListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { resetLocationInventoryById(it) }
        }
    }

    // установка статуса найдено вручную
    private fun setFoundInventoryItemState(item: InventoryItemForListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { setFoundInventoryItemByID(it) }
        }
    }

    // установка примечания
    private fun setCommentInventoryItem(item: InventoryItemForListModel, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            item.id?.let { setCommentInventoryItemByID(it, comment) }
        }
    }

    // сброс статуса ТМЦ по id
    private fun resetLocationInventoryById(id: Int) {
        resetLocationInventoryItemByID.execute(id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(viewState.currentLocationID),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocationID
            )
        )
    }

    // установка статуса найдено вручную
    private fun setFoundInventoryItemByID(id: Int) {
        setFoundInventoryItemByIDUseCase.execute(id)
        viewState = viewState.copy(
            inventoryState = getInventoryInfo.execute(viewState.currentLocationID),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                viewState.currentLocationID
            )
        )
    }

    // установка комментария
    private fun setCommentInventoryItemByID(id: Int, comment: String) {
        setCommentInventoryItemByIDUseCase.execute(id, comment)
    }

    // Обновление данных(общего списка ТМЦ и информации об инвентаризации) с учетом выбранного фильтра по местоположению
    private fun editCurrentLocation(idLocation: Int) = viewModelScope.launch {
        viewState = viewState.copy(
            currentLocationID = idLocation,
            inventoryState = getInventoryInfo.execute(idLocation),
            inventoryItems = getInventoryItemByLocationIDUseCase.execute(
                idLocation
            )
        )
    }
}
