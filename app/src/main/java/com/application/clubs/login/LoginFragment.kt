package com.application.clubs.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.application.clubs.MainActivity
import com.application.clubs.MainActivityViewModel
import com.application.clubs.R
import com.application.clubs.databinding.FragmentLoginBinding
import com.application.clubs.login.sign_in.AppleAuthUiClient
import com.application.clubs.login.sign_in.GoogleAuthUiClient
import com.application.clubs.login.sign_in.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()
    private val viewModelMainActivity: MainActivityViewModel by activityViewModels<MainActivityViewModel>()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = requireContext(),
            oneTapClient = Identity.getSignInClient(requireContext())
        )
    }

    private val appleAuthUiClient by lazy {
        AppleAuthUiClient()
    }

    private val launcherGoogle = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch{
                val signInResult = googleAuthUiClient.signInWithIntent(
                    intent = result.data?: return@launch
                )
                viewModel.onSignInResult(signInResult)
            }
        } else {
            // Обрабатываем отмену или ошибку операции входа
            Log.e("debug", "Google sign in failed: resultCode ${result.resultCode}")
        }
    }

    private val launcherApple = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch{
/*                val signInResult = appleAuthUiClient.signInWithIntent(
                    intent = result.data?: return@launch
                )*/
                //viewModel.onSignInResult(signInResult)
            }
        } else {
            // Обрабатываем отмену или ошибку операции входа
            Log.e("debug", "Google sign in failed: resultCode ${result.resultCode}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelMainActivity.changeVisibilityBottomNavigation(false)



        lifecycleScope.launch {
            viewModel.state.collect { state ->
                // Обновляем UI в соответствии с новым состоянием
                if (state.isSignInSuccessful){
                    Log.d("debug", "Успешно вошли")
                    (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_calendarFragment)
                    viewModel.resetState()
                }
            }
        }

        binding.loginGoogle.setOnClickListener{
            lifecycleScope.launch{
                val signInIntentSender = googleAuthUiClient.signIn()
                launcherGoogle.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender?:return@launch
                    ).build()
                )
            }
        }

        binding.loginApple.setOnClickListener{
            lifecycleScope.launch{
                val  auth = Firebase.auth
                val provider = OAuthProvider.newBuilder("apple.com")
                provider.addCustomParameter("locale", "en")
                auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                    .addOnSuccessListener { authResult ->
                        // Вход выполнен успешно!
                        Log.d("debug", "activitySignIn:onSuccess:${authResult.user}")
                        val user = authResult.user
                        // ...
                    }
                    .addOnFailureListener { e ->
                        Log.w("debug", "activitySignIn:onFailure", e)
                    }
                //initAppleSignIn()
                //val signInIntentSender = appleAuthUiClient.signIn()
                //appleAuthUiClient.signIn()
/*                launcherApple.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender?:return@launch
                    ).build()
                )*/
            }
        }
    }

    private fun initAppleSignIn() {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.scopes = arrayOf("email", "name").toMutableList()
        provider.addCustomParameter("locale", "en")

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val pending = auth.pendingAuthResult

        Log.d("debug", "Pending")
        pending?.addOnSuccessListener { authResult ->
            //success
            Log.d("debug", "Успешно: $authResult")
        }?.addOnFailureListener { e ->
            Log.d("debug", "${e.message}")
        }

        /*auth.startActivityForSignInWithProvider(this@LoginFragment, provider.build())
            .addOnSuccessListener { authResult ->
                // Sign-in successful!

                val user = authResult.user
                val abc= user!!.providerData[1]
                if (abc.uid != "") {
                    id=abc.uid
                }
                if (!user!!.displayName.isNullOrEmpty()) {
                    userName = user!!.displayName!!
                }else{
                    userName=""
                }

                if (user!!.email != "") {
                    mail= user!!.email!!
                }

                // logA("Apple Sign In Success -> “
            }
            .addOnFailureListener { e ->
                // logA("Apple Sign In Fail -> " + e.message)
                e.printStackTrace()
            }*/
    }

    override fun onStart() {
        super.onStart()

        if (googleAuthUiClient.getSignerInUser()!= null){
            Log.d("debug", "Уже выполнен вход!")
            (activity as MainActivity).navController.navigate(R.id.action_loginFragment_to_calendarFragment)
            viewModel.resetState()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}