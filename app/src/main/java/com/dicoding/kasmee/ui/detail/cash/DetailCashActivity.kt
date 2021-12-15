package com.dicoding.kasmee.ui.detail.cash

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.ActivityDetailCashBinding
import com.dicoding.kasmee.ui.add.transaction.AddTransactionFragment
import com.dicoding.kasmee.ui.detail.transaction.DetailTransactionFragment
import com.dicoding.kasmee.ui.edit.cash.EditCashFragment
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

    private var cashId: Int = 0

    private val transactionAdapter: TransactionAdapter by lazy {
        TransactionAdapter(::onTransactionClicked, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailCashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initSwipeDeleteAction()

        // Get cashId from intent
        cashId = intent.getIntExtra(EXTRA_CASH_ID, 0)

        // Set content of cash
        setCash(cashId)

        // Get transaction item
        setTransaction(cashId)

        // Delete and Back Button
        binding?.btnBack?.setOnClickListener(this)
        binding?.btnAddTransaction?.setOnClickListener(this)
        binding?.btnEditCash?.setOnClickListener(this)
        binding?.btnDeleteCash?.setOnClickListener(this)

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
            R.id.btn_add_transaction -> {
                showDialogAddTransaction()
            }
            R.id.btn_edit_cash -> {
                showDialogEditCash()
            }
            R.id.btn_delete_cash -> {
                deleteCash()
            }
        }
    }

    private fun showDialogAddTransaction() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_CASH_ID, cashId)

        val dialog = AddTransactionFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, AddTransactionFragment::class.java.simpleName)
        dialog.setOnTransactionAddedListener(object :
            AddTransactionFragment.OnTransactionAddedListener {
            override fun onAdded(isAdded: Boolean) {
                if (isAdded) {
                    refreshActivity()
                }
            }
        })
    }

    private fun showDialogEditCash() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_CASH_ID, cashId)

        val dialog = EditCashFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, EditCashFragment::class.java.simpleName)
        dialog.setOnCashChangedListener(object : EditCashFragment.OnCashChangedListener {
            override fun onChanged(isChanged: Boolean) {
                refreshActivity()
            }
        })
    }

    private fun deleteCash() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.delete_cash_alert))
            setNegativeButton(getString(R.string.no), null)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteCash()
                finish()
            }
        }.show()
    }

    fun setCash(cashId: Int) {
        showShimmerCash()
        viewModel.getCash(cashId)
        viewModel.cash.observe(this) { cash ->
            hideShimmerCash()
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

    fun setTransaction(cashId: Int) {

        // RecyclerView Setup
        binding?.rvTransaction?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        // Observe List of Transaction
        lifecycleScope.launch {
            viewModel.getTransaction(cashId)
            viewModel.transaction.observe(this@DetailCashActivity, { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideShimmerTransaction()
                        hideNoTransaction()
                        transactionAdapter.submitList(resource.data?.listTransaction)
                    }
                    Status.ERROR -> {
                        hideShimmerTransaction()
                        showNoTransaction()
                    }
                    Status.LOADING -> {
                        showShimmerTransaction()
                    }
                }
            })
        }
    }

    private fun onTransactionClicked(transaction: Transaction) {
        val dialog = DetailTransactionFragment(transaction)
        dialog.show(supportFragmentManager, DetailTransactionFragment::class.java.simpleName)
        dialog.setOnTransactionChangeListener(object :
            DetailTransactionFragment.OnTransactionChangeListener {
            override fun onChanged(isChanged: Boolean) {
                if (isChanged) {
                    refreshActivity()
                }
            }
        })
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
                refreshActivity()
            }
        })

        itemTouchHelper.attachToRecyclerView(binding?.rvTransaction)
    }

    private fun refreshActivity() {
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun showShimmerCash() {
        binding?.shimmerCash?.startShimmer()
    }

    private fun hideShimmerCash() {
        binding?.shimmerCash?.visibility = View.INVISIBLE
        binding?.shimmerCash?.stopShimmer()
    }

    private fun showShimmerTransaction() {
        binding?.shimmerTransaction?.startShimmer()
    }

    private fun hideShimmerTransaction() {
        binding?.shimmerTransaction?.visibility = View.INVISIBLE
        binding?.shimmerTransaction?.stopShimmer()
    }

    private fun showNoTransaction() {
        binding?.noTransaction?.visibility = View.VISIBLE
    }

    private fun hideNoTransaction() {
        binding?.noTransaction?.visibility = View.INVISIBLE
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
                setCash(cashId)
                setTransaction(cashId)
            }.show()
        }
    }

    companion object {
        const val EXTRA_CASH_ID = "extra_cash_id"
    }
}