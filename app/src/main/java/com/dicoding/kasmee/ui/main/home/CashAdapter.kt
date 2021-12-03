package com.dicoding.kasmee.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.databinding.ItemCashBinding

class CashAdapter(
    private val onCLick: (Cash) -> Unit
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

    inner class CashViewHolder(private val binding: ItemCashBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cash: Cash) {
            binding.apply {
                /* TODO : Add and then change tvCashTitle value to cash.title,
                 * currently there is no "title" in the cash model
                 */
                tvCashTitle.text = cash.id.toString()
                tvIncome.text = cash.transaksi.pemasukan.toString()
                tvOutcome.text = cash.transaksi.pengeluaran.toString()
                tvProfit.text = cash.transaksi.keuntungan.toString()
                tvTarget.text = cash.target.toString()
                itemView.setOnClickListener {
                    onCLick(cash)
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