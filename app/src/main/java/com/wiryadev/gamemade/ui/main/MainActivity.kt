package com.wiryadev.gamemade.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wiryadev.gamemade.R
import com.wiryadev.gamemade.core.utils.Constant.Companion.DEEPLINK_LIBRARY
import com.wiryadev.gamemade.core.utils.gone
import com.wiryadev.gamemade.core.utils.visible
import com.wiryadev.gamemade.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_library -> {
                    val uriNav = Uri.parse(DEEPLINK_LIBRARY)
                    navController.navigate(uriNav)
                    true
                }
                else -> true
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detail_fragment
                || destination.id == R.id.review_reader_fragment
            ) {
                binding.navView.gone()
            } else {
                binding.navView.visible()
            }
        }

        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (isTaskRoot
            && navHostFragment.childFragmentManager.backStackEntryCount == 0
            && supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

}