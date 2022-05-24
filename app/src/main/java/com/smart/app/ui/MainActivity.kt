package com.smart.app.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smart.app.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*----------------------------*/

        setBottomNavigationViewStyle()
    }


    /*---------------------------------------------*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.findFragmentById(R.id.container_main)?.findNavController()?.popBackStack()
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*---------------------------------------------*/


    private fun setBottomNavigationViewStyle() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_main)
        bottomNavigationView.itemIconTintList = null

        setUpBottomNavController(bottomNavigationView)
    }

    private fun setUpBottomNavController(bottomNavigationView: BottomNavigationView) {
        val navController = supportFragmentManager.findFragmentById(R.id.container_main)?.findNavController()
        navController?.let {
            bottomNavigationView.setupWithNavController(it)
        }
    }

    /*---------------------------------------------*/

}