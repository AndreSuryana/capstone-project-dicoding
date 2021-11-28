package com.dicoding.kasmee.ui.main.transactions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.kasmee.databinding.TransactionsFragmentBinding

class TransactionsFragment : Fragment() {

    private var _binding: TransactionsFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: TransactionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TransactionsFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test
        viewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]
        viewModel.text.observe(viewLifecycleOwner) {
            binding?.tvSample?.text = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}