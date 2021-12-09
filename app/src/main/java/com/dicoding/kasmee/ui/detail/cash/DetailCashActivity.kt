package com.dicoding.kasmee.ui.detail.cash

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.ActivityDetailCashBinding
import com.dicoding.kasmee.ui.detail.transaction.DetailTransactionFragment
import com.dicoding.kasmee.ui.main.home.TransactionAdapter
import com.dicoding.kasmee.util.Event
import com.dicoding.kasmee.util.Status
import com.dicoding.kasmee.util.StringHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCashActivity : AppCompatActivity(), View.OnClickListener {

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

        initSwipeDeleteAction()

        // Get data from intent and set cash in view model
        val cashId = intent.getIntExtra(EXTRA_CASH_ID, 0)
        viewModel.setCashId(cashId)

        // Set content of cash
        setCash()

        // Get transaction item
        setTransaction()

        // Delete and Back Button
        binding?.btnDelete?.setOnClickListener(this)
        binding?.btnBack?.setOnClickListener(this)

        // Observe snackbar if there is deleted transaction
        viewModel.snackBarText.observe(this, Observer(this::showSnackBarDeleted))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                finish()
            }
//            R.id.btn_delete -> {
//                AlertDialog.Builder(this).apply {
//                    setMessage(getString(R.string.delete_cash_alert))
//                    setNegativeButton(getString(R.string.no), null)
//                    setPositiveButton(getString(R.string.yes)) { _, _ ->
//                        viewModel.deleteCash()
//                        finish()
//                    }
//                }.show()
//            }
        }
    }

    private fun setCash() {
        viewModel.getCash()
        viewModel.cash.observe(this) { cash ->
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
    }

    private fun setTransaction() {

        // RecyclerView Setup
        binding?.rvTransaction?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        // Observe List of Transaction
        lifecycleScope.launch {
            viewModel.getTransaction()
            viewModel.transaction.observe(this@DetailCashActivity, { resource ->
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
            })
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        val dialog = DetailTransactionFragment(transaction)
        dialog.show(supportFragmentManager, DetailTransactionFragment::class.java.simpleName)
    }

    private fun initSwipeDeleteAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val transaction =
                    (viewHolder as TransactionAdapter.TransactionViewHolder).getTransaction
                viewModel.deleteTransaction(transaction)

                // Refresh cash content and list transaction
                setCash()
                setTransaction()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding?.rvTransaction)
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showSnackBarDeleted(event: Event<Int>) {
        val message = event.getDataIfNotHandled() ?: return
        binding?.root?.let {
            Snackbar.make(
                it,
                getString(message),
                Snackbar.LENGTH_SHORT
            ).setAction("Undo") {
                viewModel.addTransaction(
                    viewModel.undoDelete.value?.getDataIfNotHandled() as Transaction
                )
                // Refresh cash content and list transaction
                setCash()
                setTransaction()
            }.show()
        }
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

    companion object {
        const val EXTRA_CASH_ID = "extra_cash_id"
    }
}