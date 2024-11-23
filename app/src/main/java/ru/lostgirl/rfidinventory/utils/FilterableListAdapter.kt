package ru.lostgirl.rfidinventory.utils

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class FilterableListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val delimiter: String,
) : ListAdapter<T, VH>(diffCallback), Filterable {

    private var originalList: List<T> = currentList.toList()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty()) {
                        originalList
                    } else {
                        onFilter(originalList, constraint.toString(), delimiter)
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<T>, true)
            }
        }
    }

    override fun submitList(list: List<T>?) {
        submitList(list, false)
    }

    abstract fun onFilter(list: List<T>, constraint: String, delimiter: String): List<T>

    private fun submitList(list: List<T>?, filtered: Boolean) {
        if (!filtered) {
            originalList = list ?: listOf()
        }
        super.submitList(list)
    }

    fun submitListWithFilter(list: List<T>?, filterString: String) {
        originalList = list ?: listOf()
        super.submitList(onFilter(originalList, filterString, delimiter))
    }
}

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
    fun onLongClick(item: T): Boolean
}

