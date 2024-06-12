package com.cesar.examenmacropay.ui.login.viewmodel

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.LocalData
import com.cesar.examenmacropay.data.repository.LoginRepository
import com.cesar.examenmacropay.data.Result
import com.cesar.examenmacropay.ui.login.LoggedInUserView
import com.cesar.examenmacropay.ui.login.LoginFormState
import com.cesar.examenmacropay.ui.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }
    fun loginGoogle(username: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
            .addOnSuccessListener(OnSuccessListener<AuthResult?> {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = username))
            }).addOnFailureListener(OnFailureListener {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            })
    }
    fun loginGoogle(activity: Activity, authCredential: AuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
            .addOnCompleteListener(activity,
                OnCompleteListener<AuthResult?> { task ->
                    // Check condition
                    if (task.isSuccessful) {
                        _loginResult.value =
                            LoginResult(success = task.result.user?.displayName?.let { LoggedInUserView(displayName = it) })
                    } else {
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                })

    }
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun logout() {

    }
}