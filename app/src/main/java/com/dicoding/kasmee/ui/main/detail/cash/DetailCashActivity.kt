package com.dicoding.kasmee.ui.main.detail.cash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.ActivityDetailCashBinding
import com.dicoding.kasmee.util.Event
import com.dicoding.kasmee.util.StringHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCashActivity : AppCompatActivity() {

    private var _binding: ActivityDetailCashBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailCashViewModel by viewModels()

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
            cash?.let { it1 -> viewModel.deleteCash(it1) }
        }

        // Get transaction item
//        viewModel.getTransaction()

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