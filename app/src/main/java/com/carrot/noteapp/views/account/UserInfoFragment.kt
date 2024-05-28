package com.carrot.noteapp.views.account

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.carrot.noteapp.R
import com.carrot.noteapp.databinding.FragmentUserInfoBinding
import com.carrot.noteapp.utils.Result
import com.carrot.noteapp.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private var _binding: FragmentUserInfoBinding? = null
    val binding: FragmentUserInfoBinding? get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding?.registerButton?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfoFragment_to_createAccountFragment)
        }

        binding?.loginButton?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfoFragment_to_loginFragment)
        }

        binding?.logoutButton?.setOnClickListener {
            userViewModel.logout()
        }
        subscribeToCurrentUserEvents()
    }

    private fun subscribeToCurrentUserEvents() = lifecycleScope.launch {
        userViewModel.currentUserState.collect { result ->
            when (result) {
                is Result.Success -> {
                    userLoggedIn()
                    binding?.userTxt?.text = result.data?.username ?: "No Name"
                    binding?.userEmail?.text = result.data?.email ?: "No Email"
                }

                is Result.Error -> {
                    userNotLoggedIn()
                    "Not logged in".also { binding?.userTxt?.text = it }
                }

                is Result.Loading -> {
                    binding?.userProgressBar?.isVisible = true
                }
            }
        }
    }

    private fun userLoggedIn() {
        binding?.userProgressBar?.isVisible = false
        binding?.loginButton?.isVisible = false
        binding?.registerButton?.isVisible = false
        binding?.logoutButton?.isVisible = true
        binding?.userEmail?.isVisible = true
    }

    private fun userNotLoggedIn() {
        binding?.userProgressBar?.isVisible = false
        binding?.loginButton?.isVisible = true
        binding?.registerButton?.isVisible = true
        binding?.logoutButton?.isVisible = false
        binding?.userEmail?.isVisible = false
    }


    override fun onStart() {
        super.onStart()
        userViewModel.getCurrentUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}