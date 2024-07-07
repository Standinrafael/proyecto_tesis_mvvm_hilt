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
import com.example.proyecto_tesis.data.repository.AuthRepository
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {


    private val _authState = MutableLiveData<AuthRes<FirebaseUser?>>()
    val authState: LiveData<AuthRes<FirebaseUser?>> get() = _authState

    val currentUser: StateFlow<FirebaseUser?> = authUseCases.getCurrentUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun createUserWithEmailandPassword(email: String, password: String): LiveData<AuthRes<FirebaseUser?>> {
        val authResult = MutableLiveData<AuthRes<FirebaseUser?>>()
        viewModelScope.launch {
            val result = authUseCases.createUserWithEmailandPassword(email, password)
            authResult.value = result
        }
        return authResult
    }



    fun signInWithEmailandPassword(email: String, password: String): LiveData<AuthRes<FirebaseUser?>> {
        val authResult = MutableLiveData<AuthRes<FirebaseUser?>>()
        viewModelScope.launch {
            val result = authUseCases.signInWithEmailandPassword(email, password)
            authResult.value = result
        }
        return authResult
    }



    suspend fun deleteCurrentUser(): Boolean {
        return authUseCases.deleteCurrentUser()
    }

    fun getCurrentUid(): String? {
        return authUseCases.getCurrentUid()
    }

    fun signOut() {
        authUseCases.signOut()
    }

    fun resetPassword(email: String): LiveData<AuthRes<Unit>> {
        val resetResult = MutableLiveData<AuthRes<Unit>>()
        viewModelScope.launch {
            val result = authUseCases.resetPassword(email)
            resetResult.value = result
        }
        return resetResult
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(email: String, password: String): LiveData<AuthRes<FirebaseUser?>> {
        val signUpResult = MutableLiveData<AuthRes<FirebaseUser?>>()
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
                val result = authUseCases.createUserWithEmailandPassword(email, password)
                if (result is AuthRes.Success) {
                    authUseCases.createUser()
                }
                signUpResult.value = result
            } else if ((password.length < 6 && password.length > 0) && email.isNotEmpty()) {
                signUpResult.value = AuthRes.Error("La contraseña debe tener al menos 6 caracteres")
            } else {
                signUpResult.value = AuthRes.Error("Existen campos vacíos")
            }
        }
        return signUpResult
    }

    fun getCurrentUserUid(): String? {
        return currentUser.value?.uid
    }


}
