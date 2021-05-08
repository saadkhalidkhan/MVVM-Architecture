package com.droidgeeks.groceryapp.ui.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.droidgeeks.groceryapp.R
import com.droidgeeks.groceryapp.utility.navigate

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        moveToDashboard()
    }

    private fun moveToDashboard() {
        Handler().postDelayed({
            navigate<MainActivity>()
        }, 3000)
    }
}