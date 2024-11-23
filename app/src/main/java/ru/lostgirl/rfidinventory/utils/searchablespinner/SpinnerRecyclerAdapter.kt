package ru.lostgirl.rfidinventory.utils.searchablespinner

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.lostgirl.rfidinventory.R
import ru.lostgirl.rfidinventory.databinding.ListItemSeachableSpinnerBinding

internal class SpinnerRecyclerAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val onItemSelectListener: OnItemSelectListener
) :
    RecyclerView.Adapter<SpinnerRecyclerAdapter.SpinnerHolder>() {
    private var spinnerListItems: ArrayList<String> = list
    private lateinit var selectedItem: String
    var highlightSelectedItem: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerHolder {
        return SpinnerHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_seachable_spinner,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return spinnerListItems.size
    }

    override fun onBindViewHolder(holder: SpinnerHolder, position: Int) {
        val currentSting = spinnerListItems[position]
        holder.textViewSpinnerItem.text = currentSting
        if (highlightSelectedItem && ::selectedItem.isInitialized) {
            val colorDrawable: ColorDrawable =
                if (currentSting.equals(selectedItem, true)) {
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                } else {
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }
            holder.textViewSpinnerItem.background = colorDrawable
        }
        holder.textViewSpinnerItem.setOnClickListener {
            holder.textViewSpinnerItem.isClickable = false
            selectedItem = currentSting
            notifyDataSetChanged()
            onItemSelectListener.setOnItemSelectListener(
                getOriginalItemPosition(currentSting),
                currentSting
            )
        }
    }

    class SpinnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemSeachableSpinnerBinding.bind(itemView)
        val textViewSpinnerItem: TextView = binding.textViewSpinnerItem
    }

    fun filter(query: CharSequence?) {
        val filteredNames: ArrayList<String> = ArrayList()
        if (query.isNullOrEmpty()) {
            filterList(list)
        } else {
            for (s in list) {
                if (s.contains(query, true)) {
                    filteredNames.add(s)
                }
            }
            filterList(filteredNames)
        }
    }

    private fun filterList(filteredNames: ArrayList<String>) {
        spinnerListItems = filteredNames
        notifyDataSetChanged()
    }

    private fun getOriginalItemPosition(selectedString: String): Int {
        return list.indexOf(selectedString)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
