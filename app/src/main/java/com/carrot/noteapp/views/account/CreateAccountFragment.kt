package com.carrot.noteapp.views.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.carrot.noteapp.R
import com.carrot.noteapp.databinding.FragmentCreateAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.carrot.noteapp.utils.Result
import com.carrot.noteapp.viewmodels.UserViewModel

@AndroidEntryPoint
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    private var _binding: FragmentCreateAccountBinding? = null
    val binding: FragmentCreateAccountBinding? get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateAccountBinding.bind(view)

        subscribeToRegisterEvents()
        binding?.createAccountBtn?.setOnClickListener {
            val username = binding!!.userNameEditTxt.text.toString()
            val email = binding!!.emailEditTxt.text.toString()
            val password = binding!!.passwordEditTxt.text.toString()
            val confirmPassword = binding!!.passwordReEnterEditTxt.text.toString()

            userViewModel.createUser(username.trim(), email.trim(), password.trim(), confirmPassword.trim())
        }
    }

    private fun subscribeToRegisterEvents() = lifecycleScope.launch {
        userViewModel.registerState.collect { result ->

            when (result) {
                is Result.Success -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Account Successfully Created!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    showProgressBar()
                }
            }
        }

    }

    private fun showProgressBar() {
        binding?.createUserProgressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        binding?.createUserProgressBar?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}