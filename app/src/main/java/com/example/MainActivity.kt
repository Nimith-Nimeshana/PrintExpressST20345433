package com.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.databinding.ActivityMainBinding
import com.example.fragment.HomeFragment
import com.example.fragment.OrdersFragment
import com.example.fragment.NotificationsFragment
import com.example.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_orders -> OrdersFragment()
                R.id.nav_notifications -> NotificationsFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            // back stack when navigating to a primary tab
            supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            replaceFragment(selectedFragment)
            true
        }
    }

    /**
     * Helper method to dynamically switch inside main container
     */
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}
