package com.droidgeeks.groceryapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.droidgeeks.groceryapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    private fun initData() {
        navController = Navigation.findNavController(this@MainActivity, R.id.home_fragment)

    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (navController.currentDestination?.id == R.id.pendingFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}