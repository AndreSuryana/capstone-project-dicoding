package com.dicoding.kasmee.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.HomeFragmentBinding
import com.dicoding.kasmee.ui.main.detail.cash.DetailCashActivity
import com.dicoding.kasmee.util.Status
import com.dicoding.kasmee.util.loadImage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    private var firstVisit = false

    private val cashAdapter: CashAdapter by lazy {
        CashAdapter(::onCashClicked)
    }

    private val transactionAdapter: TransactionAdapter by lazy {
        TransactionAdapter(::onTransactionClicked, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firstVisit = true
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodayTransaction()
        setUserInfo()
        setCash()
        setTransaction()
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
            // Refresh cash and transaction
            setCash()
            setTransaction()
        }
    }

    private fun setTodayTransaction() {
        // TODO : Check if there is transaction today. If there is no transaction showNoTodayTransaction
        showNoTodayTransaction()
    }

    private fun setUserInfo(){

        lifecycleScope.launch {
            viewModel.getUserInfo()
            viewModel.user.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val userName = getString(R.string.home_greetings, resource.data?.name)
                        binding?.tvGreetings?.text = userName
                        binding?.ivProfileImage?.loadImage(resource.data?.profilePhotoPath)
                    }
                    Status.ERROR -> {
                        resource?.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> Any() // Do nothing
                }
            }
        }
    }

    private fun setCash() {

        // RecyclerView Setup
        binding?.rvCash?.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = cashAdapter
        }

        // Observe List of Cash
        viewModel.generateCash()
        viewModel.cash.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    hideNoCash()
                    resource?.data?.listCash?.let {
                        cashAdapter.submitList(it)
                    }
                }
                Status.ERROR -> {
                    showNoCash()
                    resource?.message?.let { showSnackBar(it) }
                }
                Status.LOADING -> {
                    showProgressBar()
                    showNoCash()
                }
            }
        }
    }

    private fun onCashClicked(cash: Cash) {
        Intent(activity, DetailCashActivity::class.java).also {
            it.putExtra(DetailCashActivity.EXTRA_CASH_ID, cash.id)
            startActivity(it)
        }
    }

    private fun setTransaction() {

        // RecyclerView Setup
        binding?.rvTransaction?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        // Observe List of Transaction
        lifecycleScope.launch {
            viewModel.generateTransaction()
            viewModel.transaction.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideProgressBar()
                        transactionAdapter.submitList(resource.data?.listTransaction)
                    }
                    Status.ERROR -> {
                        hideProgressBar()
                        resource.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        // TODO : Get rid of the snackbar and create an intent of detail transaction
        showSnackBar(transaction.id.toString())
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showNoTodayTransaction() {
        binding?.noTransaction?.isVisible = true
    }

    private fun hideNoTodayTransaction() {
        binding?.noTransaction?.isVisible = false
    }

    private fun showNoCash() {
        binding?.noCash?.isVisible = true
    }

    private fun hideNoCash() {
        binding?.noCash?.isVisible = false
    }

    private fun showSnackBar(message: String) {
        binding?.root?.let {
            Snackbar.make(
                it,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}