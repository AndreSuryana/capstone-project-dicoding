package com.dicoding.kasmee.ui.main.cash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.CashFragmentBinding
import com.dicoding.kasmee.util.KasmeeLoadStateAdapter
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashFragment : Fragment() {

    private var _binding: CashFragmentBinding? = null
    private val binding get() = _binding

    private val viewModel: CashViewModel by viewModels()

    private val cashPagingAdapter: CashPagingAdapter by lazy {
        CashPagingAdapter(::onCashClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CashFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCash()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setCash() {

        // RecyclerView Setup
        binding?.apply {
            val layoutManager =
                if (context?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }

            rvCash.layoutManager = layoutManager
            rvCash.setHasFixedSize(true)
            rvCash.adapter = cashPagingAdapter.withLoadStateHeaderAndFooter(
                header = KasmeeLoadStateAdapter { cashPagingAdapter.retry() },
                footer = KasmeeLoadStateAdapter { cashPagingAdapter.retry() }
            )

            layoutError.btnRetry.setOnClickListener { cashPagingAdapter.retry() }
        }

        cashPagingAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                LoadState.Loading -> showShimmer()
                else -> hideShimmer()
            }

            binding?.layoutError?.error?.isVisible =
                loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && cashPagingAdapter.itemCount < 1
        }

        lifecycleScope.launch {
            viewModel.cash.observe(viewLifecycleOwner) {
                cashPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun onCashClicked(cash: Cash) {
        Intent(activity, DetailCashActivity::class.java).also {
            it.putExtra(DetailCashActivity.EXTRA_CASH_ID, cash.id)
            startActivity(it)
        }
    }

    private fun showShimmer() {
        binding?.shimmer?.startShimmer()
    }

    private fun hideShimmer() {
        binding?.shimmer?.isVisible = false
        binding?.shimmer?.stopShimmer()
    }
}