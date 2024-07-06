package com.example.proyecto_tesis.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_tesis.logic.usercases.FirestoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val firestoreUseCases: FirestoreUseCases
) : ViewModel() {

    private val _nextId = MutableLiveData<Int>()
    val nextId: LiveData<Int> get() = _nextId

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> get() = _userId

    private val _deleteUserStatus = MutableLiveData<Boolean>()
    val deleteUserStatus: LiveData<Boolean> get() = _deleteUserStatus

    private val _userPassword = MutableLiveData<String>()
    val userPassword: LiveData<String> get() = _userPassword

    fun getNextIdFromFirestore() {
        viewModelScope.launch {
            val id = firestoreUseCases.getNextIdFromFirestore()
            _nextId.value = id
        }
    }

    fun getUserId(authUID: String?) {
        viewModelScope.launch {
            val id = firestoreUseCases.userId(authUID)
            _userId.value = id
        }
    }

    suspend fun deleteUserByAuthUID(authUID: String):Boolean {
        return firestoreUseCases.deleteUserByAuthUID(authUID)
    }

    fun getUserPassword(id: Int?) {
        viewModelScope.launch {
            val password = firestoreUseCases.userPassword(id)
            _userPassword.value = password
        }
    }
}