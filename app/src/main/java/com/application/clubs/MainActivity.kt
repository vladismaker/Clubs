package com.application.clubs

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController
    //private var _binding: ActivityMainBinding? = null
    //private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //_binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_main)

        window.navigationBarColor = Color.WHITE

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        navController = Navigation.findNavController(this, R.id.nav_host)

        bottomNavigationView.visibility = View.GONE
        viewModel.liveDataDayOff.observe(this) {
            if(it){
                bottomNavigationView.selectedItemId = R.id.item_1
                bottomNavigationView.visibility = View.VISIBLE
            }else{
                bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView.setOnItemSelectedListener {item->

            when(item.itemId) {
                R.id.item_1 -> {
                    if (navController.currentDestination?.id != R.id.calendarFragment){
                        navController.navigate(R.id.action_global_to_calendarFragment)
                    }
                    true
                }
                R.id.item_2 -> {
                    if (navController.currentDestination?.id != R.id.playbookFragment){
                        navController.navigate(R.id.action_global_to_playbookFragment)
                    }
                    true
                }
                R.id.item_3 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 3")
                    false
                }
                R.id.item_4 -> {
                    if (navController.currentDestination?.id != R.id.settingsFragment){
                        navController.navigate(R.id.action_global_to_settingsFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

/*    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }*/
}