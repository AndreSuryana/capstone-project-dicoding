package com.dicoding.kasmee.ui.main.cash

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.CashFragmentBinding
import com.dicoding.kasmee.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashFragment : Fragment() {

    private var _binding: CashFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: CashViewModel by viewModels()
    private val wideCashAdapter: WideCashAdapter by lazy {
        WideCashAdapter(::onCashClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CashFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCash()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setCash() {

        // RecyclerView Setup
        if (context?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding?.rvCash?.layoutManager = GridLayoutManager(context, 2)
        } else {
            binding?.rvCash?.layoutManager = LinearLayoutManager(context)
        }
        binding?.rvCash?.apply {
            setHasFixedSize(true)
            adapter = wideCashAdapter
        }

        lifecycleScope.launch {
            viewModel.generateCash()
            viewModel.cash.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideProgressBar()
                        resource?.data?.listCash?.let {
                            wideCashAdapter.submitList(it)
                        }
                    }
                    Status.ERROR -> {
                        resource?.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun onCashClicked(cash: Cash) {
        binding?.root?.let {
            Snackbar.make(
                it,
                cash.name,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
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
}