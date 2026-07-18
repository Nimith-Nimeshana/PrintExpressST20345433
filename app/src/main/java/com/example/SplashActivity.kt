package com.example

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.utils.AnimationHelper
import com.example.databinding.ActivitySplashBinding
import com.example.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Run animations on logo and title
        AnimationHelper.fadeIn(binding.imgLogo, 1000)
        AnimationHelper.slideUp(binding.txtTitle, 1200)
        AnimationHelper.fadeIn(binding.txtSubtitle, 1500)

        // 2 second delay to proceed
        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2200)
    }
}
