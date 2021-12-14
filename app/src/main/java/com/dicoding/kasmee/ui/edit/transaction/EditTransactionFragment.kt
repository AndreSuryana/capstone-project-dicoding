package com.dicoding.kasmee.ui.edit.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.FragmentEditTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTransactionFragment(
    private val transaction: Transaction
) : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentEditTransactionBinding? = null
    private val binding get() = _binding
    private val viewModel: EditTransactionViewModel by viewModels()

    private var onTransactionChangeListener: OnTransactionChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTransactionBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set current value of edit text
        binding?.apply {
            etIncome.setText(transaction.income.toString())
            etOutcome.setText(transaction.outcome.toString())
            etDescription.setText(transaction.description)
        }

        // Button Listener
        binding?.apply {
            btnCancel.setOnClickListener(this@EditTransactionFragment)
            btnSave.setOnClickListener(this@EditTransactionFragment)
            btnClose.setOnClickListener(this@EditTransactionFragment)
        }
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
            R.id.btn_save -> {
                editTransaction()
                onTransactionChangeListener?.onChanged(true)
                dismiss()
            }
            R.id.btn_close -> {
                dismiss()
            }
        }
    }

    private fun editTransaction() {
        // Get valu efrom edit text
        val income = binding?.etIncome?.text?.trim().toString().toLong()
        val outcome = binding?.etOutcome?.text?.trim().toString().toLong()
        val description = binding?.etDescription?.text?.trim().toString()

        viewModel.editTransaction(transaction.id, transaction.cashId, income, outcome, description)
    }

    fun setOnTransactionChangeListener(onTransactionChangeListener: OnTransactionChangeListener) {
        this.onTransactionChangeListener = onTransactionChangeListener
    }

    interface OnTransactionChangeListener {
        fun onChanged(isChanged: Boolean)
    }
}