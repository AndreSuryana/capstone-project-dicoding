package com.dicoding.kasmee.ui.add.cash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.FragmentAddCashBinding
import com.dicoding.kasmee.util.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCashFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentAddCashBinding? = null
    private val binding get() = _binding
    private val viewModel: AddCashViewModel by viewModels()

    private var onCashAddedListener: OnCashAddedListener? = null

    // Variable data validation
    private var isValid: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button Listener
        binding?.apply {
            btnCancel.setOnClickListener(this@AddCashFragment)
            btnAdd.setOnClickListener(this@AddCashFragment)
            btnClose.setOnClickListener(this@AddCashFragment)
        }

        // Snackbar Observer
        viewModel.toastText.observe(this, Observer(this::showToast))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cancel -> {
                dismiss()
            }
            R.id.btn_add -> {
                addCash()
            }
            R.id.btn_close -> {
                dismiss()
            }
        }
    }

    private fun addCash() {
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
            else -> {
                if(isValid) {
                    viewModel.addCash(name, target.toLong())
                    this.onCashAddedListener?.onAdded(true)
                    dismiss()
                }
            }
        }
    }

    private fun showToast(eventMessage: Event<Int>) {
        val message = eventMessage.getDataIfNotHandled() ?: return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setOnCashAddedListener(onCashAddedListener: OnCashAddedListener) {
        this.onCashAddedListener = onCashAddedListener
    }

    interface OnCashAddedListener {
        fun onAdded(isAdded: Boolean)
    }
}