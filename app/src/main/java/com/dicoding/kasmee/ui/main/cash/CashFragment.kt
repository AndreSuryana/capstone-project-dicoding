package com.dicoding.kasmee.ui.main.cash

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.CashFragmentBinding

class CashFragment : Fragment() {

    private var _binding: CashFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: CashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test
        viewModel = ViewModelProvider(this)[CashViewModel::class.java]
        viewModel.text.observe(viewLifecycleOwner) {
            binding?.tvSample?.text = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}