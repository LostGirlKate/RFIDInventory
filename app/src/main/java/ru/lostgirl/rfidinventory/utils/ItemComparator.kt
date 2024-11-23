package ru.lostgirl.rfidinventory.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import ru.lostgirl.rfidinventory.domain.models.InventoryItemDetailModel

class ItemComparator : DiffUtil.ItemCallback<InventoryItemDetailModel>() {
    override fun areItemsTheSame(
        oldItem: InventoryItemDetailModel,
        newItem: InventoryItemDetailModel,
    ): Boolean {
        return oldItem.paraName == newItem.paraName
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: InventoryItemDetailModel,
        newItem: InventoryItemDetailModel,
    ): Boolean {
        return oldItem == newItem
    }
}
