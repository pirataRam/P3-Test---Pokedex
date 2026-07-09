package com.example.p3test_pokedex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity

/**
 * Main Activity of the Pokédex application hosting the Navigation Host Fragment.
 * Installs the official Android SplashScreen before super.onCreate to display a themed Pokeball screen.
 */
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }
}
