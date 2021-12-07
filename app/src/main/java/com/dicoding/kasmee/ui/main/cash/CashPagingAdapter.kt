package com.dicoding.kasmee.ui.main.cash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.ItemCashWideBinding
import com.dicoding.kasmee.util.StringHelper

class CashPagingAdapter(
    private val onClick: (Cash) -> Unit
) : PagingDataAdapter<Cash, CashPagingAdapter.CashPagingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashPagingViewHolder {
        val binding = ItemCashWideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CashPagingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CashPagingViewHolder, position: Int) {
        val cash = getItem(position)
        cash?.let { holder.bind(it) }
    }

    inner class CashPagingViewHolder(private val binding: ItemCashWideBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cash: Cash) {
            binding.apply {
                tvCashTitle.text = cash.name
                tvIncome.text =
                    itemView.resources.getString(R.string.rupiah_value, StringHelper.formatIntoIDR(cash.totalIncome))
                tvOutcome.text =
                    itemView.resources.getString(R.string.rupiah_value, StringHelper.formatIntoIDR(cash.totalOutcome))
                tvProfit.text =
                    itemView.resources.getString(R.string.rupiah_value, StringHelper.formatIntoIDR(cash.totalProfit))
                tvTarget.text =
                    itemView.resources.getString(R.string.target_percentage, StringHelper.getTargetPercentage(cash.target, cash.totalProfit))

                itemView.setOnClickListener {
                    onClick(cash)
                }
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cash>() {
            override fun areItemsTheSame(oldItem: Cash, newItem: Cash): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cash, newItem: Cash): Boolean {
                return oldItem == newItem
            }
        }
    }
}