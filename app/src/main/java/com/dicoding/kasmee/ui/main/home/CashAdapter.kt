package com.dicoding.kasmee.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.ItemCashBinding
import com.dicoding.kasmee.util.StringHelper

class CashAdapter(
    private val onClick: (Cash) -> Unit
) : ListAdapter<Cash, CashAdapter.CashViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashViewHolder {
        val binding = ItemCashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CashViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CashViewHolder, position: Int) {
        val cash = currentList[position]
        holder.bind(cash)
    }

    override fun getItemCount(): Int = currentList.size

    inner class CashViewHolder(private val binding: ItemCashBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cash: Cash) {
            binding.apply {
                /* TODO : Add and then change tvCashTitle value to cash.title,
                 * currently there is no "title" in the cash model
                 */
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