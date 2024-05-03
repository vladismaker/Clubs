package com.application.clubs

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    //private val sharedViewModel: CalendarViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController
/*    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //_binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_main)

        window.navigationBarColor = Color.WHITE

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        navController = Navigation.findNavController(this, R.id.nav_host)

        //NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView.setOnItemSelectedListener {item->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    Log.d("debug", "click 1")
                    navController.navigate(R.id.action_global_to_calendarFragment)
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 2")
                    //navController.navigate(R.id.action_calendarFragment_to_playbookFragment)
                    navController.navigate(R.id.action_global_to_playbookFragment)
                    true
                }
                R.id.item_3 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 3")
                    false
                }
                R.id.item_4 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 4")
                    false
                }
                else -> false
            }
        }
        bottomNavigationView.setOnItemReselectedListener  {item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    Log.d("debug", "click 01")
                    false
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 02")
                    false
                }
                R.id.item_3 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 03")
                    false
                }
                R.id.item_4 -> {
                    // Respond to navigation item 2 click
                    Log.d("debug", "click 04")
                    false
                }
                else -> {
                    Log.d("debug", "Не кликаем второй раз")
                    false
                }
            }
        }
    }

/*    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }*/
}