package ru.lostgirl.rfidinventory.data.storage.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.lostgirl.rfidinventory.data.storage.InventoryStorage
import ru.lostgirl.rfidinventory.data.storage.models.InventoryCounts
import ru.lostgirl.rfidinventory.data.storage.models.InventoryItem
import ru.lostgirl.rfidinventory.data.storage.models.InventoryLocation

@Dao
interface Dao : InventoryStorage {
    @Insert
    override suspend fun insertInventoryLocation(location: InventoryLocation)

    @Query(" select -1 as id, 'Все' as name, -1 as api_id union select id, name, api_id from inventory_location ")
    override fun getAllLocationList(): List<InventoryLocation>

    @Query("select * from inventory_item")
    override fun getAllInventoryItemList(): List<InventoryItem>

    @Insert
    override fun insertManyInventoryItem(items: List<InventoryItem>): List<Long>

    @Insert
    override fun insertManyInventoryLocation(items: List<InventoryLocation>): List<Long>

    @Query(
        """select * from inventory_item 
           where (location_id LIKE :locationID and actual_location_id is null) 
                 or actual_location_id like :locationID or :locationID = -1 
           order by inventory_num"""
    )
    override fun getInventoryItemByLocationID(locationID: Int): List<InventoryItem>

    @Query(
        """select count(1) countAll, 
                  count(case when actual_location_id is not null and actual_location_id = location_id then 1 else null end) countFound, 
                  count(case when actual_location_id is null then 1 else null end) countNotFound , 
                  count(case when actual_location_id is not null and actual_location_id<>location_id then 1 else null end) countFoundInWrongPlace
           from inventory_item 
           where (location_id LIKE :locationID and actual_location_id is null) 
                 or actual_location_id like :locationID or :locationID = -1"""
    )
    override fun getInventoryItemsCounts(locationID: Int): List<InventoryCounts>

    @Query("select * from inventory_item where  id like :id")
    override fun getInventoryItemDetail(id: Int): InventoryItem

    @Query(
        "UPDATE inventory_item SET prev_location = actual_location , prev_location_id = actual_location_id,  actual_location_id = :locationID , actual_location = :location WHERE id = :id "
    )
    override fun updateLocationInventoryItemByID(
        locationID: Int,
        location: String,
        id: Int,
    )

    @Query(
        "UPDATE inventory_item SET actual_location_id = prev_location_id , actual_location = prev_location,  prev_location = null , prev_location_id = null WHERE id = :id "
    )
    override fun resetLocationInventoryItemByID(
        id: Int,
    )

    @Query("UPDATE inventory_item SET comment = :comment WHERE id = :id ")
    override fun setCommentInventoryItemByID(id: Int, comment: String)

    @Query(
        "UPDATE inventory_item SET prev_location = actual_location , prev_location_id = actual_location_id, actual_location_id = location_id , actual_location = location WHERE id = :id "
    )
    override fun setFoundInventoryItemByID(id: Int)

    @Query("select * from inventory_item where actual_location_id is not null")
    override fun getInventoryItemForSave(): List<InventoryItem>

    @Query("select count(api_id) > 0 from inventory_location where api_id is not null")
    override fun isDataFromApi(): Boolean
}
