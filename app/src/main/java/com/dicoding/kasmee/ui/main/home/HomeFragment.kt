package com.dicoding.kasmee.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.HomeFragmentBinding
import com.dicoding.kasmee.ui.add.cash.AddCashFragment
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import com.dicoding.kasmee.ui.detail.transaction.DetailTransactionFragment
import com.dicoding.kasmee.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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

        // Button Listener
        binding?.btnAddCash?.setOnClickListener {
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
            // Refresh cash and transaction
            setUserInfo()
            setTodayTransaction()
            setCash()
            setTransaction()
        }
    }

    private fun setTodayTransaction() {
        // Get today date
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.DATE_PATTERN, Locale.getDefault())
        val today = dateFormat.format(currentTime)

        // Observe view model
        viewModel.generateTodayTransaction(today)
        viewModel.todayTransaction.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    // Set today transaction content
                    resource.data?.let { setTodayTransactionContent(it) }
                }
                Status.ERROR -> {
                    hideTodayTransaction()
                }
                Status.LOADING -> {
                    // TODO : Should show shimmer (currently no shimmer for this layout)
                }
            }
        }
    }

    private fun setTodayTransactionContent(transaction: Transaction) {
        binding?.todayTransaction?.apply {
            tvTransactionDate.text = dateFormat(transaction.createdAt)
            tvIncome.text = getString(
                R.string.rupiah_value,
                StringHelper.formatIntoIDR(transaction.income)
            )
            tvOutcome.text = getString(
                R.string.rupiah_value,
                StringHelper.formatIntoIDR(transaction.outcome)
            )

            if (transaction.profit <= 0) {
                tvProfit.text = getString(
                    R.string.loss_text,
                    StringHelper.formatIntoIDR(transaction.profit)
                )
                tvProfit.setTextColor(getColor(requireContext(), R.color.red))
            } else {
                tvProfit.text = getString(
                    R.string.profit_text,
                    StringHelper.formatIntoIDR(transaction.profit)
                )
                tvProfit.setTextColor(getColor(requireContext(), R.color.green))
            }
        }
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
//                        setWorker(requireContext())
//                        val intent = Intent(requireContext(), TargetReminder::class.java)
//                        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 101, intent, PendingIntent.FLAG_IMMUTABLE)
//                        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
//                        alarmManager.setRepeating(
//                            AlarmManager.RTC_WAKEUP,
//                            System.currentTimeMillis() + 1000,
//                            AlarmManager.INTERVAL_DAY,
//                            pendingIntent
//                        )
//                        setReminder()
                        cashAdapter.submitList(it)
                    }
                }
                Status.ERROR -> {
                    hideShimmerCash()
                    showNoCash()
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
                        hideNoTransaction()
                        transactionAdapter.submitList(resource.data)
                    }
                    Status.ERROR -> {
                        hideShimmerTransaction()
                        showNoTransaction()
                    }
                    Status.LOADING -> {
                        showShimmerTransaction()
                    }
                }
            }
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        val dialog = DetailTransactionFragment(transaction)
        dialog.show(childFragmentManager, DetailTransactionFragment::class.java.simpleName)
        dialog.setOnTransactionDelted(object : DetailTransactionFragment.OnTransactionDeleted {
            override fun onDeleted(isDeleted: Boolean) {
                if (isDeleted) {
                    // Refresh cash and transaction
                    setTodayTransaction()
                    setCash()
                    setTransaction()
                }
            }
        })
    }

    private fun showDialogAddCash() {
        val dialog = AddCashFragment()
        dialog.show(childFragmentManager, AddCashFragment::class.java.simpleName)
        dialog.setOnCashAddedListener(object : AddCashFragment.OnCashAddedListener {
            override fun onAdded(isAdded: Boolean) {
                if (isAdded) {
                    // Refresh cash and transaction
                    setTodayTransaction()
                    setCash()
                    setTransaction()
                }
            }
        })
    }

    private fun showShimmerUser() {
        binding?.apply {
            shimmerUser.visibility = View.VISIBLE
            shimmerRecentTransaction.visibility = View.VISIBLE
            contentUser.visibility = View.INVISIBLE
            contentRecentTransaction.visibility = View.INVISIBLE
            shimmerUser.startShimmer()
            shimmerRecentTransaction.startShimmer()
        }
    }

    private fun hideShimmerUser() {
        binding?.apply {
            shimmerUser.visibility = View.INVISIBLE
            shimmerRecentTransaction.visibility = View.INVISIBLE
            shimmerUser.stopShimmer()
            shimmerRecentTransaction.stopShimmer()
            contentUser.visibility = View.VISIBLE
            contentRecentTransaction.visibility = View.VISIBLE
        }
    }

    private fun showShimmerCash() {
        binding?.apply {
            shimmerCash.visibility = View.VISIBLE
            contentCash.visibility = View.INVISIBLE
            shimmerCash.startShimmer()
        }
    }

    private fun hideShimmerCash() {
        binding?.apply {
            shimmerCash.visibility = View.INVISIBLE
            shimmerCash.stopShimmer()
            contentCash.visibility = View.VISIBLE
        }
    }

    private fun showShimmerTransaction() {
        binding?.apply {
            shimmerTransaction.visibility = View.VISIBLE
            contentTransaction.visibility = View.INVISIBLE
            shimmerTransaction.startShimmer()
        }
    }

    private fun hideShimmerTransaction() {
        binding?.apply {
            shimmerTransaction.visibility = View.INVISIBLE
            shimmerTransaction.stopShimmer()
            contentTransaction.visibility = View.VISIBLE
        }
    }

    private fun hideTodayTransaction() {
        binding?.apply {
            todayTransactionGroup.visibility = View.INVISIBLE
            noTransaction.visibility = View.VISIBLE
        }
    }

    private fun showNoCash() {
        binding?.noCash?.visibility = View.VISIBLE
    }

    private fun hideNoCash() {
        binding?.noCash?.visibility = View.INVISIBLE
    }

    private fun showNoTransaction() {
        binding?.noTransactionList?.visibility = View.VISIBLE
    }

    private fun hideNoTransaction() {
        binding?.noTransactionList?.visibility = View.GONE
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