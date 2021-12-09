package com.dicoding.kasmee.ui.main.transactions

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
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.TransactionsFragmentBinding
import com.dicoding.kasmee.ui.detail.transaction.DetailTransactionFragment
import com.dicoding.kasmee.util.KasmeeLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private var _binding: TransactionsFragmentBinding? = null
    private val binding get() = _binding

    private val viewModel: TransactionsViewModel by viewModels()

    private val transactionPagingAdapter: TransactionPagingAdapter by lazy {
        TransactionPagingAdapter(::onTransactionClicked, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TransactionsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTransaction()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setTransaction() {

        // RecyclerView Setup
        binding?.apply {
            val layoutManager =
                if (context?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }

            rvTransaction.layoutManager = layoutManager
            rvTransaction.setHasFixedSize(true)
            rvTransaction.adapter = transactionPagingAdapter.withLoadStateHeaderAndFooter(
                header = KasmeeLoadStateAdapter { transactionPagingAdapter.retry() },
                footer = KasmeeLoadStateAdapter { transactionPagingAdapter.retry() }
            )

            layoutError.btnRetry.setOnClickListener { transactionPagingAdapter.retry() }
        }

        transactionPagingAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                LoadState.Loading -> showShimmer()
                else -> hideShimmer()
            }

            binding?.layoutError?.error?.isVisible =
                loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && transactionPagingAdapter.itemCount < 1
        }

        // Observe
        lifecycleScope.launch {
            viewModel.transaction.observe(viewLifecycleOwner) {
                transactionPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        val dialog = DetailTransactionFragment(transaction)
        dialog.show(childFragmentManager, DetailTransactionFragment::class.java.simpleName)
        dialog.setOnTransactionDelted(object : DetailTransactionFragment.OnTransactionDeleted {
            override fun onDeleted(isDeleted: Boolean) {
                if (isDeleted) {
                    // Refresh list
                    setTransaction()
                }
            }
        })
    }

    private fun showShimmer() {
        binding?.shimmer?.startShimmer()
    }

    private fun hideShimmer() {
        binding?.shimmer?.isVisible = false
        binding?.shimmer?.stopShimmer()
    }
}