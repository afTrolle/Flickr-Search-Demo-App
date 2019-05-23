package dev.trolle.flickr.search.demo.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dev.trolle.flickr.search.demo.R
import dev.trolle.flickr.search.demo.extensions.observe
import dev.trolle.flickr.search.demo.repository.settings.SettingsRepository
import dev.trolle.flickr.search.demo.repository.settings.toNightMode
import dev.trolle.flickr.search.demo.view.application.injector
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injector.inject(this)

        //start set Night mode after set settings
        val nightMode = settingsRepository.getNightMode()
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode != currentMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }


        //if user change setting recreate activity with new mode
        settingsRepository.darkModeLiveData.observe(this) {
            val desiredMode = it.toNightMode()
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            if (desiredMode != currentMode) {
                recreate()
            }
        }
    }


}
