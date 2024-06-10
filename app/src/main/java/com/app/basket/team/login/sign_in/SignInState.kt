package com.app.basket.team.login.sign_in

data class SignInState(
    val isSignInSuccessful:Boolean = false,
    val signInError:String? = null
)
