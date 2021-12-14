package com.dicoding.kasmee.ui.detail.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.FragmentDetailTransactionBinding
import com.dicoding.kasmee.ui.edit.transaction.EditTransactionFragment
import com.dicoding.kasmee.util.StringHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTransactionFragment(
    private val transaction: Transaction
) : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentDetailTransactionBinding? = null
    private val binding get() = _binding
    private val viewModel: DetailTransactionViewModel by viewModels()

    private var onTransactionChangeListener: OnTransactionChangeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTransactionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Transaction Content
        viewModel.setTransaction(transaction)
        viewModel.transaction.observe(viewLifecycleOwner, Observer(this::setTransaction))

        // Button Listener
        binding?.apply {
            btnClose.setOnClickListener(this@DetailTransactionFragment)
            btnEdit.setOnClickListener(this@DetailTransactionFragment)
            btnDelete.setOnClickListener(this@DetailTransactionFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setTransaction(transaction: Transaction) {
        binding?.apply {
            tvIncome.text = getString(
                R.string.rupiah_value,
                StringHelper.formatIntoIDR(transaction.income)
            )
            tvOutcome.text = getString(
                R.string.rupiah_value_minus,
                StringHelper.formatIntoIDR(transaction.outcome)
            )
            tvProfit.text = getString(
                R.string.rupiah_value,
                StringHelper.formatIntoIDR(transaction.profit)
            )
            tvDescription.text = transaction.description
        }
    }

    fun setOnTransactionChangeListener(onTransactionChangeListener: OnTransactionChangeListener) {
        this.onTransactionChangeListener = onTransactionChangeListener
    }

    interface OnTransactionChangeListener {
        fun onChanged(isChanged: Boolean)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_close -> {
                dismiss()
            }
            R.id.btn_edit -> {
                val dialog = EditTransactionFragment(transaction)
                dialog.show(childFragmentManager, EditTransactionFragment::class.java.simpleName)
                dialog.setOnTransactionChangeListener(object :
                    EditTransactionFragment.OnTransactionChangeListener {
                    override fun onChanged(isChanged: Boolean) {
                        if (isChanged) {
                            // Close the dialog fragment
                            onTransactionChangeListener?.onChanged(true)
                            dismiss()
                        }
                    }
                })
            }
            R.id.btn_delete -> {
                viewModel.deleteTransaction()
                onTransactionChangeListener?.onChanged(true)
                dismiss()
            }
        }
    }
}