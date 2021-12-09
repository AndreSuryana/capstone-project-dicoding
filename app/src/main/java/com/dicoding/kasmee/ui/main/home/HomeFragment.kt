package com.dicoding.kasmee.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.HomeFragmentBinding
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
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

    private fun setUserInfo() {

        lifecycleScope.launch {
            viewModel.getUserInfo()
            viewModel.user.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideShimmerUser()
                        val userName = getString(R.string.home_greetings, resource.data?.name)
                        binding?.tvGreetings?.text = userName
                        binding?.ivProfileImage?.loadImage(resource.data?.profilePhotoPath)
                    }
                    Status.ERROR -> {
                        hideShimmerUser()
                        resource?.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> {
                        showShimmerUser()
                    }
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
                    hideShimmerCash()
                    hideNoCash()
                    resource?.data?.let {
                        cashAdapter.submitList(it)
                    }
                }
                Status.ERROR -> {
                    hideShimmerCash()
                    showNoCash()
                    resource?.message?.let { showSnackBar(it) }
                }
                Status.LOADING -> {
                    showShimmerCash()
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
                        hideShimmerTransaction()
                        transactionAdapter.submitList(resource.data)
                    }
                    Status.ERROR -> {
                        hideShimmerTransaction()
                        resource.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> {
                        showShimmerTransaction()
                    }
                }
            }
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        // TODO : Get rid of the snackbar and create an intent of detail transaction
        showSnackBar(transaction.id.toString())
    }

    private fun showShimmerUser() {
        binding?.contentUser?.visibility = View.INVISIBLE
        binding?.contentRecentTransaction?.visibility = View.INVISIBLE
        binding?.shimmerUser?.startShimmer()
        binding?.shimmerRecentTransaction?.startShimmer()
    }

    private fun hideShimmerUser() {
        binding?.shimmerUser?.visibility = View.INVISIBLE
        binding?.shimmerRecentTransaction?.visibility = View.INVISIBLE
        binding?.shimmerUser?.stopShimmer()
        binding?.shimmerRecentTransaction?.stopShimmer()
        binding?.contentUser?.visibility = View.VISIBLE
        binding?.contentRecentTransaction?.visibility = View.VISIBLE
    }

    private fun showShimmerCash() {
        binding?.contentCash?.visibility = View.INVISIBLE
        binding?.shimmerCash?.startShimmer()
    }

    private fun hideShimmerCash() {
        binding?.shimmerCash?.visibility = View.INVISIBLE
        binding?.shimmerCash?.stopShimmer()
        binding?.contentCash?.visibility = View.VISIBLE
    }

    private fun showShimmerTransaction() {
        binding?.contentTransaction?.visibility = View.INVISIBLE
        binding?.shimmerTransaction?.startShimmer()
    }

    private fun hideShimmerTransaction() {
        binding?.shimmerTransaction?.visibility = View.INVISIBLE
        binding?.shimmerTransaction?.stopShimmer()
        binding?.contentTransaction?.visibility = View.VISIBLE
    }

    private fun showNoTodayTransaction() {
        binding?.noTransaction?.visibility = View.VISIBLE
    }

    private fun hideNoTodayTransaction() {
        binding?.noTransaction?.visibility = View.INVISIBLE
    }

    private fun showNoCash() {
        binding?.noCash?.visibility = View.VISIBLE
    }

    private fun hideNoCash() {
        binding?.noCash?.visibility = View.INVISIBLE
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