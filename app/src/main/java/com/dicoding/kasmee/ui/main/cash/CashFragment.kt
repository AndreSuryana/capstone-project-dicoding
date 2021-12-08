package com.dicoding.kasmee.ui.main.cash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.CashFragmentBinding
import com.dicoding.kasmee.ui.main.detail.cash.DetailCashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CashFragment : Fragment() {

    private var _binding: CashFragmentBinding? = null
    private val binding get() = _binding

    private val viewModel: CashViewModel by viewModels()

    private val cashPagingAdapter: CashPagingAdapter by lazy {
        CashPagingAdapter(::onCashClicked)
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
        binding?.rvCash?.apply {
            layoutManager =
                if (context?.applicationContext?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
            setHasFixedSize(true)
            adapter = cashPagingAdapter
        }

        lifecycleScope.launch {
            viewModel.cash.observe(viewLifecycleOwner) {
                cashPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun onCashClicked(cash: Cash) {
        Intent(activity, DetailCashActivity::class.java).also {
            it.putExtra(DetailCashActivity.EXTRA_CASH_ID, cash.id)
            startActivity(it)
        }
    }
}