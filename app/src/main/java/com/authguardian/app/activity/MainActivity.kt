package com.authguardian.app.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.authguardian.mobileapp.R

class MainActivity : AppCompatActivity() {

    // region Data

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem) = item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)
}