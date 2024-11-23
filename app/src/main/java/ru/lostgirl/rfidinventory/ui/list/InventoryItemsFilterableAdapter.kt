package ru.lostgirl.rfidinventory.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.databinding.InventoryItemBinding
import ru.lostgirl.rfidinventory.domain.models.InventoryItemForListModel
import ru.lostgirl.rfidinventory.domain.models.InventoryItemState
import ru.lostgirl.rfidinventory.domain.models.toColor
import ru.lostgirl.rfidinventory.domain.models.toIco
import ru.lostgirl.rfidinventory.utils.FilterableListAdapter
import ru.lostgirl.rfidinventory.utils.ItemComparatorAllItems
import ru.lostgirl.rfidinventory.utils.OnItemClickListener

const val FILTER_INDEX_NOT_FOUND = 1
const val FILTER_INDEX_FOUND = 2
const val FILTER_INDEX_FOUND_IN_WRONG_PLACE = 3

class InventoryItemsFilterableAdapter(
    private val itemClickListener: OnItemClickListener<InventoryItemForListModel>,
    delimiter: String,
) :
    FilterableListAdapter<InventoryItemForListModel, InventoryItemsFilterableAdapter.ItemHolder>(
        ItemComparatorAllItems(),
        delimiter
    ) {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = InventoryItemBinding.bind(view)

        fun setData(
            item: InventoryItemForListModel,
            itemClickListener: OnItemClickListener<InventoryItemForListModel>,
        ) = with(binding) {
            itemName.text = item.model
            itemLocation.text = item.location
            itemOldLocation.visibility =
                if (item.location != item.oldLocation) View.VISIBLE else View.GONE
            itemOldLocation.text = item.oldLocation
            itemNum.text = item.inventoryNum
            managerName.text = item.managerName
            rfidNum.text = item.rfidCardNum
            breakLine.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    item.state.toColor()
                )
            )
            stateShape.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    item.state.toIco()
                )
            )
            cardView.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
            cardView.setOnLongClickListener {
                itemClickListener.onLongClick(item)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.inventory_item, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), itemClickListener)
    }

    override fun onFilter(
        list: List<InventoryItemForListModel>,
        constraint: String,
        delimiter: String,
    ): List<InventoryItemForListModel> {
        val filterList = constraint.split(delimiter)
        val firstFilter = filterList.first().lowercase()
        val showNotFound = filterList[FILTER_INDEX_NOT_FOUND].toBoolean()
        val showFound = filterList[FILTER_INDEX_FOUND].toBoolean()
        val showFoundInWrongPlace = filterList[FILTER_INDEX_FOUND_IN_WRONG_PLACE].toBoolean()
        return list.filter {
            (
                it.model.lowercase().contains(firstFilter) ||
                    it.inventoryNum.lowercase().contains(firstFilter)
                ) &&
                (showNotFound || it.state != InventoryItemState.STATE_NOT_FOUND) &&
                (showFound || it.state != InventoryItemState.STATE_FOUND) &&
                (showFoundInWrongPlace || it.state != InventoryItemState.STATE_FOUND_IN_WRONG_PLACE)
        }
    }
}
