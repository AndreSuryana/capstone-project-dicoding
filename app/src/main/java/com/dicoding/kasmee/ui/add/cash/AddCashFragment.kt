package com.dicoding.kasmee.ui.add.cash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.FragmentAddCashBinding
import com.dicoding.kasmee.util.Event
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCashFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentAddCashBinding? = null
    private val binding get() = _binding
    private val viewModel: AddCashViewModel by viewModels()

    private var onCashAddedListener: OnCashAddedListener? = null

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
        }

        // Snackbar Observer
        viewModel.snackBarText.observe(this, Observer(this::showSnackBar))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_cancel -> {
                dismiss()
            }
            R.id.btn_add -> {
                addCash()
                onCashAddedListener?.onAdded(true)
                dismiss()
            }
        }
    }

    private fun addCash() {
        // Get value from edit text
        val name = binding?.etName?.text.toString()
        val target = binding?.etTarget?.text.toString().toLong()

        viewModel.addCash(name, target)
    }

    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getDataIfNotHandled() ?: return
        binding?.root?.let {
            Snackbar.make(
                it,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    fun setOnCashAddedListener(onCashAddedListener: OnCashAddedListener) {
        this.onCashAddedListener = onCashAddedListener
    }

    interface OnCashAddedListener {
        fun onAdded(isAdded: Boolean)
    }
}