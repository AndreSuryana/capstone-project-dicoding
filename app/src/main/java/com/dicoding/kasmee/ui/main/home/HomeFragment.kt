package com.dicoding.kasmee.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.HomeFragmentBinding
import com.dicoding.kasmee.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : Get username of currently login user
        val username = "Dummy"
        binding?.tvGreetings?.text = getString(R.string.home_greetings, username)
        setCash()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setCash() {
        val cashAdapter = CashAdapter(::onCashClicked)

        binding?.rvCash?.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = cashAdapter
        }

        viewModel.generateCash()
        viewModel.cash.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    hideNoTodayTransaction()
                    resource?.data?.data?.data?.let {
                        cashAdapter.submitList(it)
                    }
                }
                Status.ERROR -> {
                    showNoTodayTransaction()
                    resource?.message?.let { showSnackBar(it) }
                }
                Status.LOADING -> {
                    showProgressBar()
                    showNoTodayTransaction()
                }
            }
        }
    }

    private fun onCashClicked(cash: Cash) {
        // TODO : Create an intent to cash detail
    }

    private fun showProgressBar() {
        binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.progressBar?.isVisible = false
    }

    private fun showNoTodayTransaction() {
        binding?.noTransaction?.isVisible = true
    }

    private fun hideNoTodayTransaction() {
        binding?.noTransaction?.isVisible = false
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