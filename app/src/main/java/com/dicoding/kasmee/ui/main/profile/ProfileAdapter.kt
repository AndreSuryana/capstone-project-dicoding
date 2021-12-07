package com.dicoding.kasmee.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kasmee.databinding.ItemProfileBinding

class ProfileAdapter(
    private val listMenu: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val menu = listMenu[position]
        holder.bind(menu)
    }

    override fun getItemCount(): Int = listMenu.size

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: String) {
            binding.tvItemProfile.text = menu
            itemView.setOnClickListener {
                onClick(menu)
            }
        }
    }
}