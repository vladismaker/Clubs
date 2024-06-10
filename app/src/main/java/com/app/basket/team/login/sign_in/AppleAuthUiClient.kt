package com.app.basket.team.login.sign_in

import android.util.Log
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.concurrent.CancellationException

class AppleAuthUiClient {
    private val  auth = Firebase.auth

/*    suspend fun signInWithIntent(intent: Intent):SignInResult{
*//*        val credential =  OAuthProvider.newCredentialBuilder("apple.com")
            .setIdTokenWithRawNonce(appleIdToken, rawUnhashedNonce)
            .build()*//*
*//*        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)*//*

        return try {
            val user = auth.signInWithCredential(credential).await().user
            //val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }*/

    suspend fun signIn(){
        val result = try{
            val provider = OAuthProvider.newBuilder("apple.com")
            provider.scopes = arrayOf("email", "name").toMutableList()
            provider.addCustomParameter("locale", "en")
            val pending = auth.pendingAuthResult
            pending?.addOnSuccessListener { authResult ->
                //success
                authResult
            }?.addOnFailureListener { e ->
                Log.d("debug", "${e.message}")
            }
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }

        //return result?.pendingIntent?.intentSender
    }

    suspend fun signOut(){
        try {
            auth.signOut()
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }
}