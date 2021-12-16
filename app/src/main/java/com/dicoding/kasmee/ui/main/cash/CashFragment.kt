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
import com.dicoding.kasmee.ui.add.cash.AddCashFragment
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import com.dicoding.kasmee.util.KasmeeLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashFragment : Fragment() {

    private var _binding: CashFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: CashViewModel by viewModels()

    private var firstVisit = false

    private val cashPagingAdapter: CashPagingAdapter by lazy {
        CashPagingAdapter(::onCashClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firstVisit = true
        _binding = CashFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCash()

        // FAB Setup
        binding?.fabAddCash?.setOnClickListener {
            showDialogAddCash()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        if (firstVisit)
            firstVisit = false
        else {
            // Refresh cash list
            cashPagingAdapter.refresh()
        }
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

            binding?.noCash?.isVisible =
                loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && cashPagingAdapter.itemCount < 1
            binding?.layoutError?.error?.isVisible =
                loadState.source.refresh is LoadState.Error && loadState.append.endOfPaginationReached && cashPagingAdapter.itemCount < 1
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

    private fun showDialogAddCash() {
        val dialog = AddCashFragment()
        dialog.show(childFragmentManager, AddCashFragment::class.java.simpleName)
        dialog.setOnCashAddedListener(object : AddCashFragment.OnCashAddedListener {
            override fun onAdded(isAdded: Boolean) {
                if (isAdded) {
                    // Refresh list
                    cashPagingAdapter.refresh()
                }
            }
        })
    }

    private fun showShimmer() {
        binding?.apply {
            shimmer.startShimmer()
            noCash.visibility = View.GONE
        }
    }

    private fun hideShimmer() {
        binding?.apply {
            shimmer.visibility = View.GONE
            shimmer.stopShimmer()
        }
    }
}