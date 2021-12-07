package com.dicoding.kasmee.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.ItemTransactionBinding
import com.dicoding.kasmee.util.StringHelper

class TransactionAdapter(
    private val onClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = currentList[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = currentList.size

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.apply {
                tvIncome.text = itemView.resources.getString(
                    R.string.rupiah_value,
                    StringHelper.formatIntoIDR(transaction.income)
                )
                tvOutcome.text = itemView.resources.getString(
                    R.string.rupiah_value,
                    StringHelper.formatIntoIDR(transaction.outcome)
                )
                tvProfit.text = itemView.resources.getString(
                    R.string.profit_text,
                    StringHelper.formatIntoIDR(transaction.profit)
                )
                tvTransactionDate.text = transaction.createdAt

                itemView.setOnClickListener {
                    onClick(transaction)
                }
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }
        }
    }
}