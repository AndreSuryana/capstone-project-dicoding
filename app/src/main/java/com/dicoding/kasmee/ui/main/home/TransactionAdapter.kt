package com.dicoding.kasmee.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.transaction.Transaction
import com.dicoding.kasmee.databinding.ItemTransactionBinding
import com.dicoding.kasmee.util.StringHelper
import com.dicoding.kasmee.util.dateFormat
import dagger.hilt.android.qualifiers.ApplicationContext

class TransactionAdapter(
    private val onClick: (Transaction) -> Unit,
    @ApplicationContext private val context: Context
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

        lateinit var getTransaction: Transaction

        fun bind(transaction: Transaction) {
            getTransaction = transaction
            binding.apply {
                tvIncome.text = itemView.resources.getString(
                    R.string.rupiah_value,
                    StringHelper.formatIntoIDR(transaction.income)
                )
                tvOutcome.text = itemView.resources.getString(
                    R.string.rupiah_value,
                    StringHelper.formatIntoIDR(transaction.outcome)
                )

                if (transaction.profit <= 0) {
                    tvProfit.text = itemView.resources.getString(
                        R.string.loss_text,
                        StringHelper.formatIntoIDR(transaction.profit)
                    )
                    tvProfit.setTextColor(ContextCompat.getColor(context, R.color.red))
                } else {
                    tvProfit.text = itemView.resources.getString(
                        R.string.profit_text,
                        StringHelper.formatIntoIDR(transaction.profit)
                    )
                    tvProfit.setTextColor(ContextCompat.getColor(context, R.color.green))
                }

                tvTransactionDate.text = dateFormat(transaction.createdAt)

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