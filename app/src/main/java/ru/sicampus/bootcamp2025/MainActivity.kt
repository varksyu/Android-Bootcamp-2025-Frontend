package ru.sicampus.bootcamp2025

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userRole = intent.getStringExtra("USER_ROLE")

        setContentView(R.layout.activity_main)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if (userRole == "ROLE_ADMIN") {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_menu_admin)
            navController.setGraph(R.navigation.main_admin_nav_graph)

        } else {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_menu)
            navController.setGraph(R.navigation.main_nav_graph)
        }


        bottomNavigationView.setupWithNavController(navController)

    }
}