package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainHomeBinding

class MainHomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout principal com a navegação lateral
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Define a toolbar como ActionBar
        setSupportActionBar(binding.appBarMainActivityHome.toolbar)

        // Botão de adicionar espécie
        binding.appBarMainActivityHome.buttonAdd.setOnClickListener {
            val viewAddEspecie = Intent(this, MainActivityAddEspecie::class.java)
            startActivity(viewAddEspecie)
        }
        // Configuração do Navigation Drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_home)

        // Definindo os destinos principais do Drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_especie, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        // Conectando Navigation Controller com ActionBar e NavigationView
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_activity_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}