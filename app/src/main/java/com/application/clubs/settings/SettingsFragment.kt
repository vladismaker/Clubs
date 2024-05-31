package com.application.clubs.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.application.clubs.MainActivity
import com.application.clubs.R
import com.application.clubs.databinding.FragmentPlaybookBinding
import com.application.clubs.databinding.FragmentSettingsBinding
import com.application.clubs.login.sign_in.AppleAuthUiClient
import com.application.clubs.login.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireContext())
        )
    }

    private val appleAuthUiClient by lazy {
        AppleAuthUiClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonExit.setOnClickListener{
            lifecycleScope.launch {
                googleAuthUiClient.signOut()
                appleAuthUiClient.signOut()
                (activity as MainActivity).navController.navigate(R.id.action_settingsFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}