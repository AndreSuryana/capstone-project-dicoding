package com.dicoding.kasmee.ui.add.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.FragmentAddTransactionBinding
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import com.dicoding.kasmee.util.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTransactionFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding
    private val viewModel: AddTransactionViewModel by viewModels()

    private var onTransactionAddedListener: OnTransactionAddedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button Listener
        binding?.apply {
            btnCancel.setOnClickListener(this@AddTransactionFragment)
            btnAdd.setOnClickListener(this@AddTransactionFragment)
            btnClose.setOnClickListener(this@AddTransactionFragment)
        }

        // Snackbar Text
        viewModel.toastText.observe(viewLifecycleOwner, Observer(this::showToast))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cancel -> {
                dismiss()
            }
            R.id.btn_add -> {
                addTransaction()
                onTransactionAddedListener?.onAdded(true)
                dismiss()
            }
            R.id.btn_close -> {
                dismiss()
            }
        }
    }

    private fun addTransaction() {
        // Get value from edit text
        val cashId = arguments?.getInt(DetailCashActivity.EXTRA_CASH_ID, 0)
        val income = binding?.etIncome?.text?.trim().toString()
        val outcome = binding?.etOutcome?.text?.trim().toString()
        var description = binding?.etDescription?.text?.trim().toString()

        when {
            income.isEmpty() -> binding?.etIncome?.error = getString(R.string.please_fill_income)
            outcome.isEmpty() -> binding?.etOutcome?.error = getString(R.string.please_fill_outcome)
            description.isEmpty() -> description = getString(R.string.dash)
            else -> cashId?.let { viewModel.addTransaction(it, income.toLong(), outcome.toLong(), description) }
        }
    }

    fun setOnTransactionAddedListener(onTransactionAddedListener: OnTransactionAddedListener) {
        this.onTransactionAddedListener = onTransactionAddedListener
    }

    interface OnTransactionAddedListener {
        fun onAdded(isAdded: Boolean)
    }

    private fun showToast(eventMessage: Event<Int>) {
        val message = eventMessage.getDataIfNotHandled() ?: return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}