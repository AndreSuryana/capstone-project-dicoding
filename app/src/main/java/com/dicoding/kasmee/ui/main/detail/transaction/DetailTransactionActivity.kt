package com.dicoding.kasmee.ui.main.detail.transaction

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.dicoding.kasmee.databinding.ActivityDetailTransactionBinding

class DetailTransactionActivity : AppCompatActivity() {

    private var _binding: ActivityDetailTransactionBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}