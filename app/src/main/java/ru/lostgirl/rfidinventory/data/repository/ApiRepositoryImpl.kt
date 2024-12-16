package ru.lostgirl.rfidinventory.data.repository

import ru.lostgirl.rfidinventory.data.api.ApiService
import ru.lostgirl.rfidinventory.data.api.models.InventoryDataForSave
import ru.lostgirl.rfidinventory.data.storage.InventoryStorage
import ru.lostgirl.rfidinventory.domain.repository.ApiRepository
import ru.lostgirl.rfidinventory.utils.error.ApiError
import ru.lostgirl.rfidinventory.utils.error.NetworkError
import ru.lostgirl.rfidinventory.utils.error.UnknownError
import java.io.IOException

class ApiRepositoryImpl(
    private val apiService: ApiService,
    private val inventoryStorage: InventoryStorage,
) : ApiRepository {
    override suspend fun load() {
        try {
            val response = apiService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            //Сохранение справочника location
            val locations = body.locations.map { it.toInventoryLocation() }
            inventoryStorage.insertManyInventoryLocation(locations)
            val localLocations = inventoryStorage.getAllLocationList()

            //Сохранение справочника ТМЦ
            val items = body.items.map { item ->
                val localLocationID = localLocations.firstOrNull { it.apiID == item.locationId }?.apiID ?: 0
                val localLocation = localLocations.firstOrNull { it.apiID == item.locationId }?.name ?: ""
                item.toInventoryItem(
                    localLocationID = localLocationID,
                    localLocation = localLocation
                )
            }
            inventoryStorage.insertManyInventoryItem(items)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveItems() {
        try {
            val items = inventoryStorage.getInventoryItemForSave().map {
                it.toApiItemForSave()
            }
            val response = apiService.saveItems(InventoryDataForSave(items = items))
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}