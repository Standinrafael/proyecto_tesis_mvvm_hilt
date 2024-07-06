package com.example.proyecto_tesis.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_tesis.logic.usercases.AuthUseCases
import com.example.proyecto_tesis.utils.AuthRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

// AuthViewModel.kt
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _authState = MutableLiveData<AuthRes<FirebaseUser?>>()
    val authState: LiveData<AuthRes<FirebaseUser?>> get() = _authState

    fun createUserWithEmailandPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = authUseCases.createUserWithEmailandPassword(email, password)
            _authState.value = result
        }
    }


    fun signInWithEmailandPassword(email: String, password: String): LiveData<AuthRes<FirebaseUser?>> {
        val authResult = MutableLiveData<AuthRes<FirebaseUser?>>()
        viewModelScope.launch {
            val result = authUseCases.signInWithEmailandPassword(email, password)
            authResult.value = result
        }
        return authResult
    }

    fun getCurrentUser(): FirebaseUser? {
        return authUseCases.getCurrentUser()
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            authUseCases.deleteCurrentUser()
        }
    }

    fun signOut() {
        authUseCases.signOut()
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            authUseCases.resetPassword(email)
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return authUseCases.handleSignInResult(task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun signInWithGoogleCredential(credential: AuthCredential): LiveData<AuthRes<FirebaseUser>> {
        val authResult = MutableLiveData<AuthRes<FirebaseUser>>()
        viewModelScope.launch {
            val result = authUseCases.signInWithGoogleCredential(credential)
            authResult.value = result
        }
        return authResult
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createUser() {
        viewModelScope.launch {
            authUseCases.createUser()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createGoogleUser(firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            authUseCases.createGoogleUser(firebaseUser)
        }
    }

    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}
