package com.cesar.examenmacropay.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cesar.examenmacropay.ui.pokemon.MainActivity
import com.cesar.examenmacropay.R
import com.cesar.examenmacropay.data.LocalData
import com.cesar.examenmacropay.databinding.ActivityLoginBinding
import com.cesar.examenmacropay.ui.login.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        loading = binding.loading
        val loginEmail = binding.loginEmail
        val loginGoogle = binding.loginGoogle

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            loginEmail?.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading?.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                LocalData.getInstance(this).setLogin(true)
                updateUiWithUser(loginResult.success)
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading?.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
            loginGoogle.setOnClickListener {
                loading?.visibility = View.VISIBLE
                val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("726837312131-o368g5pc07i0n8o602ofvim32f96gohm.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

                // Initialize sign in client
                val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)
                val intent = googleSignInClient.signInIntent
                // Start activity for result
                startActivityForResult(intent, 100)
            }
            loginEmail?.setOnClickListener {
                loading?.visibility = View.VISIBLE
                loginViewModel.loginGoogle(username.text.toString(), password.text.toString())
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            val signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            // check condition
            if (signInAccountTask.isSuccessful) {
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    val googleSignInAccount = signInAccountTask.getResult(
                        ApiException::class.java
                    )
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        val authCredential =
                            GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
                        // Check credential
                       loginViewModel.loginGoogle(this, authCredential)
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }else {
                showLoginFailed(R.string.login_failed)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        loading?.visibility = View.GONE
    }
    private fun updateUiWithUser(model: LoggedInUserView) {
        val displayName = model.displayName
        val home = Intent(this, MainActivity::class.java)
        home.putExtra("displayName", displayName)
        finish()
        startActivity(home)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}