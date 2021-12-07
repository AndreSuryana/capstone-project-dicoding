package com.dicoding.kasmee.ui.main.transactions

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.TransactionsFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private var _binding: TransactionsFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: TransactionsViewModel by viewModels()

    private val transactionPagingAdapter: TransactionPagingAdapter by lazy {
        TransactionPagingAdapter(::onTransactionClicked)
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
        binding?.rvTransaction?.apply {
            layoutManager =
                if (context?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
            setHasFixedSize(true)
            adapter = transactionPagingAdapter
        }

        // Observe
        lifecycleScope.launch {
            viewModel.transaction.observe(viewLifecycleOwner) {
                transactionPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        binding?.root?.let {
            Snackbar.make(
                it,
                transaction.id.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}