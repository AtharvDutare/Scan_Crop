package com.example.trysample.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for handling authentication operations and state
 */
class AuthViewModel : ViewModel() {
    // Authentication service
    private val authService = AuthService()
    
    // Authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    // Current user
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    
    init {
        // Check if user is already signed in
        _currentUser.value = authService.getCurrentUser()
        if (_currentUser.value != null) {
            _authState.value = AuthState.SignedIn(_currentUser.value!!)
        }
        
        // Observe authentication state changes
        viewModelScope.launch {
            authService.observeAuthStateChanges().collect { user ->
                _currentUser.value = user
                _authState.value = if (user != null) {
                    AuthState.SignedIn(user)
                } else {
                    AuthState.SignedOut
                }
            }
        }
    }
    
    /**
     * Creates a new user account with email and password
     * 
     * @param email User's email address
     * @param password User's password
     */
    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authService.createUserWithEmailAndPassword(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.SignedIn(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                }
        }
    }
    
    /**
     * Signs in a user with email and password
     * 
     * @param email User's email address
     * @param password User's password
     */
    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authService.signInWithEmailAndPassword(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.SignedIn(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                }
        }
    }
    
    /**
     * Signs out the current user
     * This method updates the authentication state to SignedOut
     */
    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authService.signOut()
            _authState.value = AuthState.SignedOut
            _currentUser.value = null
        }
    }
    
    /**
     * Sends email verification to the current user
     */
    fun sendEmailVerification() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            
            authService.sendEmailVerification()
                .onSuccess {
                    _authState.update { currentState ->
                        if (currentState is AuthState.SignedIn) {
                            AuthState.EmailVerificationSent(currentState.user)
                        } else {
                            currentState
                        }
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Failed to send verification email")
                }
        }
    }
    
    /**
     * Clears any error state
     */
    fun clearError() {
        _authState.update { currentState ->
            when (currentState) {
                is AuthState.Error -> AuthState.Initial
                else -> currentState
            }
        }
    }
}

/**
 * Represents the different states of authentication
 */
sealed class AuthState {
    /**
     * Initial state before any authentication attempt
     */
    object Initial : AuthState()
    
    /**
     * Loading state during authentication operations
     */
    object Loading : AuthState()
    
    /**
     * State when user is signed in
     * 
     * @param user The FirebaseUser object representing the signed-in user
     */
    data class SignedIn(val user: FirebaseUser) : AuthState()
    
    /**
     * State when user is signed out
     */
    object SignedOut : AuthState()
    
    /**
     * State when email verification has been sent
     * 
     * @param user The FirebaseUser object that verification was sent to
     */
    data class EmailVerificationSent(val user: FirebaseUser) : AuthState()
    
    /**
     * State when an error occurs during authentication
     * 
     * @param message The error message
     */
    data class Error(val message: String) : AuthState()
} 