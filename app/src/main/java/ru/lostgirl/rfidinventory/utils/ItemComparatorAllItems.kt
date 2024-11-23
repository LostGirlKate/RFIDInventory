package ru.lostgirl.rfidinventory.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel

class ItemComparatorAllItems : DiffUtil.ItemCallback<InventoryItemForListModel>() {
    override fun areItemsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: InventoryItemForListModel,
        newItem: InventoryItemForListModel,
    ): Boolean {
        return oldItem == newItem
    }
}
