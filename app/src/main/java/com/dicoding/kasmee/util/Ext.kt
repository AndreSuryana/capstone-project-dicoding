package com.dicoding.kasmee.util

import com.bumptech.glide.Glide
import com.dicoding.kasmee.R
import de.hdodenhof.circleimageview.CircleImageView

object Ext {

    fun CircleImageView.loadImage(image: String?) {
        Glide.with(this.context)
            .load(image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(this)
    }
}