package com.dicoding.kasmee.ui.edit.cash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.FragmentEditCashBinding
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import com.dicoding.kasmee.util.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCashFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentEditCashBinding? = null
    private val binding get() = _binding
    private val viewModel: EditCashViewModel by viewModels()

    private var onCashChangedListener: OnCashChangedListener? = null

    private var cashId: Int? = null

    // Variable data validation
    private var isValid: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditCashBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get cash id from bundle
        cashId = arguments?.getInt(DetailCashActivity.EXTRA_CASH_ID, 0)

        // Observe cash
        cashId?.let { viewModel.setCash(it) }
        viewModel.cash.observe(this, Observer(this::setCurrentCashValue))

        // Button Listener
        binding?.apply {
            btnCancel.setOnClickListener(this@EditCashFragment)
            btnAdd.setOnClickListener(this@EditCashFragment)
            btnClose.setOnClickListener(this@EditCashFragment)
        }

        // Snackbar Observer
        viewModel.toastText.observe(this, Observer(this::showToast))
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
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
                editCash()
            }
            R.id.btn_close -> {
                dismiss()
            }
        }
    }

    private fun setCurrentCashValue(cash: Cash) {
        // Set current value of edit text
        binding?.apply {
            etName.setText(cash.name)
            etTarget.setText(cash.target.toString())
        }
    }

    private fun editCash() {
        // Get value from edit text
        val name = binding?.etName?.text?.trim().toString()
        val target = binding?.etTarget?.text?.trim().toString()

        isValid = true

        when {
            name.isEmpty() -> {
                binding?.etName?.error = getString(R.string.empty_cash_name)
                isValid = false
            }
            target.isEmpty() -> {
                binding?.etTarget?.error = getString(R.string.zero_target_error)
                isValid = false
            }
            else -> cashId?.let { cashId ->
                if (isValid) {
                    viewModel.editCash(cashId, name, target.toLong())
                    this.onCashChangedListener?.onChanged(true)
                    dismiss()
                }
            }
        }
    }

    private fun showToast(eventMessage: Event<Int>) {
        val message = eventMessage.getDataIfNotHandled() ?: return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setOnCashChangedListener(onCashChangedListener: OnCashChangedListener) {
        this.onCashChangedListener = onCashChangedListener
    }

    interface OnCashChangedListener {
        fun onChanged(isChanged: Boolean)
    }
}