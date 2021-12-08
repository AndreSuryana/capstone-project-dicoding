package com.dicoding.kasmee.ui.main.detail.cash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.ActivityDetailCashBinding
import com.dicoding.kasmee.ui.main.home.TransactionAdapter
import com.dicoding.kasmee.util.Event
import com.dicoding.kasmee.util.Status
import com.dicoding.kasmee.util.StringHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCashActivity : AppCompatActivity() {

    private var _binding: ActivityDetailCashBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailCashViewModel by viewModels()

    private val transactionAdapter: TransactionAdapter by lazy {
        TransactionAdapter(::onTransactionClicked, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailCashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Get data from intent
        val cash = intent.getParcelableExtra<Cash>(EXTRA_CASH)

        // Set content of cash
        setContentCash(cash)

        // Delete Button
        binding?.btnDelete?.setOnClickListener {
            cash?.let { cash -> viewModel.deleteCash(cash) }
        }

        // Get transaction item
        cash?.let { setTransactionByCashId(it.id) }

        viewModel.snackBarText.observe(this, Observer(this::showSnackBar))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setContentCash(cash: Cash?) {

        binding?.apply {
            tvCashTitle.text = cash?.name
            tvIncome.text = getString(
                R.string.rupiah_value,
                cash?.let { StringHelper.formatIntoIDR(it.totalIncome) }
            )
            tvOutcome.text = getString(
                R.string.rupiah_value,
                cash?.let { StringHelper.formatIntoIDR(it.totalOutcome) }
            )
            tvProfit.text = getString(
                R.string.rupiah_value,
                cash?.let { StringHelper.formatIntoIDR(it.totalProfit) }
            )
            tvTarget.text = getString(
                R.string.rupiah_value,
                cash?.let { StringHelper.formatIntoIDR(it.target) }
            )
            tvPercentage.text = getString(
                R.string.percentage_detail,
                cash?.let { StringHelper.getTargetPercentage(it.target, it.totalProfit) }
            )
        }
    }

    private fun setTransactionByCashId(cashId: Int?) {

        // RecyclerView Setup
        binding?.rvTransaction?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        // Observe List of Transaction
        lifecycleScope.launch {
            cashId?.let { viewModel.getTransaction(it) }
            viewModel.transaction.observe(this@DetailCashActivity, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideProgressBar()
                        transactionAdapter.submitList(resource.data?.listTransaction)
                    }
                    Status.ERROR -> {
                        hideProgressBar()
                        viewModel.snackBarText.observe(
                            this@DetailCashActivity,
                            Observer(this@DetailCashActivity::showSnackBar)
                        )
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            })
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        // TODO : Get rid of the snackbar and create an intent of detail transaction
        binding?.root?.let {
            Snackbar.make(
                it,
                transaction.id.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showSnackBar(event: Event<Int>) {
        val message = event.getDataIfNotHandled() ?: return
        binding?.root?.let {
            Snackbar.make(
                it,
                getString(message),
                Snackbar.LENGTH_SHORT
            ).setAction("Undo") {
                viewModel.addCash(viewModel.undoDelete.value?.getDataIfNotHandled() as Cash)
            }.show()
        }
    }

    companion object {
        const val EXTRA_CASH = "extra_cash"
    }
}