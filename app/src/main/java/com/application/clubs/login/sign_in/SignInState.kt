package com.application.clubs.login.sign_in

data class SignInState(
    val isSignInSuccessful:Boolean = false,
    val signInError:String? = null
)
