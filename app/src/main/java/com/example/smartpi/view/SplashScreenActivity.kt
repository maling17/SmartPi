package com.example.smartpi.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.smartpi.MainActivity
import com.example.smartpi.R
import com.example.smartpi.utils.Preferences
import com.example.smartpi.view.sign.SignInActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        preferences = Preferences(this)

        val handler = Handler()
        handler.postDelayed({
            if (preferences.getValues("status").equals("1")) {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(
                    this@SplashScreenActivity,
                    SignInActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

}