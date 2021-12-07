package com.dicoding.kasmee.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.HomeFragmentBinding
import com.dicoding.kasmee.util.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()
    private val cashAdapter: CashAdapter by lazy {
        CashAdapter(::onCashClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodayTransaction()
        setUserInfo()
        setCash()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setTodayTransaction() {
        // TODO : Check if there is transaction today. If there is no transaction showNoTodayTransaction
        showNoTodayTransaction()
    }

    private fun setUserInfo(){

        lifecycleScope.launch {
            viewModel.getUserInfo()
            viewModel.user.observe(viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val userName = getString(R.string.home_greetings, resource.data?.name)
                        binding?.tvGreetings?.text = userName
                        binding?.ivProfileImage?.let {
                            Glide.with(this@HomeFragment)
                                .load(resource.data?.profilePhotoPath)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into(it)
                        }
                    }
                    Status.ERROR -> {
                        resource?.message?.let { showSnackBar(it) }
                    }
                    Status.LOADING -> Any() // Do nothing
                }
            }
        }
    }

    private fun setCash() {

        // RecyclerView Setup
        binding?.rvCash?.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
            adapter = cashAdapter
        }

        // Observe List of Cash
        viewModel.generateCash()
        viewModel.cash.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    hideNoCash()
                    resource?.data?.listCash?.let {
                        cashAdapter.submitList(it)
                    }
                }
                Status.ERROR -> {
                    showNoCash()
                    resource?.message?.let { showSnackBar(it) }
                }
                Status.LOADING -> {
                    showProgressBar()
                    showNoCash()
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

    private fun showNoTodayTransaction() {
        binding?.noTransaction?.isVisible = true
    }

    private fun hideNoTodayTransaction() {
        binding?.noTransaction?.isVisible = false
    }

    private fun showNoCash() {
        binding?.noCash?.isVisible = true
    }

    private fun hideNoCash() {
        binding?.noCash?.isVisible = false
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