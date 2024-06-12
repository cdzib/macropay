package com.cesar.examenmacropay.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.cesar.examenmacropay.databinding.ActivityFullscreenBinding
import com.cesar.examenmacropay.data.LocalData
import com.cesar.examenmacropay.ui.login.LoginActivity
import com.cesar.examenmacropay.ui.pokemon.MainActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var fullscreenContent: TextView
    private val hideHandler = Handler(Looper.myLooper()!!)


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        show()
    }

    private fun show() {
        val isLogin = LocalData.getInstance(this).getLogin()
        if(isLogin){
            hideHandler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }else {
            hideHandler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }

}