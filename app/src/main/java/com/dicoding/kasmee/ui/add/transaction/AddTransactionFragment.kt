package com.dicoding.kasmee.ui.add.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.FragmentAddTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTransactionFragment(private val cashId: Int) : DialogFragment(), View.OnClickListener {

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
        val income = binding?.etIncome?.text.toString().toLong()
        val outcome = binding?.etOutcome?.text.toString().toLong()
        val description = binding?.etDescription?.text.toString()

        Log.d("AddTransaction: ", "Triggered!")
        viewModel.addTransaction(cashId, income, outcome, description)
    }

    fun setOnTransactionAddedListener(onTransactionAddedListener: OnTransactionAddedListener) {
        this.onTransactionAddedListener = onTransactionAddedListener
    }

    interface OnTransactionAddedListener {
        fun onAdded(isAdded: Boolean)
    }
}