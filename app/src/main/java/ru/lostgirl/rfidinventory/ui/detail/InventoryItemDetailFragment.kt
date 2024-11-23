package ru.lostgirl.rfidinventory.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.lostgirl.rfidinventory.databinding.FragmentInventoryItemDetailBinding
import ru.lostgirl.rfidinventory.mvi.MviFragment

class InventoryItemDetailFragment :
    MviFragment<
            InventoryItemDetailState,
            InventoryItemDetailEffect,
            InventoryItemDetailEvent,
            InventoryItemDetailViewModel
            >() {

    override val viewModel by viewModel<InventoryItemDetailViewModel>()
    private lateinit var binding: FragmentInventoryItemDetailBinding
    private lateinit var adapter: InventoryItemDetailAdapter
    private val args: InventoryItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryItemDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        val itemID = args.itemId
        val state = args.state
        lifecycle.addObserver(viewModel)
        viewModel.process(InventoryItemDetailEvent.OpenDetails(itemID, state))
    }

    override fun renderViewEffect(viewEffect: InventoryItemDetailEffect) {}

    override fun renderViewState(viewState: InventoryItemDetailState) {
        adapter.submitList(viewState.details)
    }

    // Инициализация RecyclerView
    private fun initRcView() = with(binding) {
        rcInventoryDetailList.layoutManager = LinearLayoutManager(activity)
        adapter = InventoryItemDetailAdapter()
        rcInventoryDetailList.adapter = adapter
    }
}
