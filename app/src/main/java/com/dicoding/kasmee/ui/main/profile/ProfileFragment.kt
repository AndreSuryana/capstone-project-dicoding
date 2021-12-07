package com.dicoding.kasmee.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.databinding.ProfileFragmentBinding
import com.dicoding.kasmee.ui.auth.login.LoginActivity
import com.dicoding.kasmee.ui.main.setting.SettingsActivity
import com.dicoding.kasmee.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModels()

    private val menu = arrayListOf<String>()

    private val profileAdapter: ProfileAdapter by lazy {
        ProfileAdapter(menu, ::onMenuClicked)
    }

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menu.add(getString(R.string.edit_profile))
        menu.add(getString(R.string.settings))

        binding?.apply {
            rvProfile.adapter = profileAdapter
            rvProfile.layoutManager = LinearLayoutManager(context)

            btnLogout.setOnClickListener {
                sessionManager.clearToken()
                Intent(requireContext(), LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onMenuClicked(menu: String) {
        view?.let {
            when (menu) {
                getString(R.string.edit_profile) -> {
                    Snackbar.make(
                        it,
                        "Edit",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                getString(R.string.settings) -> {
                    Intent(requireContext(), SettingsActivity::class.java).also { intent ->
                        startActivity(intent)
                    }
                }
                else -> {}
            }
        }
    }
}