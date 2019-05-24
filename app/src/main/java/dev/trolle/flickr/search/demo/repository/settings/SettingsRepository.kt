package dev.trolle.flickr.search.demo.repository.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun onSharedPreferenceChangeListener(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "nightMode" -> {
                darkModeLiveData.postValue(sharedPreferences?.getBoolean("nightMode", false))
            }
            "presentation" -> {
                layoutLiveData.postValue(getPresentationMode())
            }
            "sortby" -> {
                sortByLiveData.postValue(getSortedBy())
            }
        }
    }

    fun getNightMode() = preferences.getBoolean("nightMode", false).toNightMode()

    fun getSortedBy(): Int = preferences.getString("sortby", "2")?.toIntOrNull() ?: 2

    fun getLicence(): String? = preferences.getString("licence", "")

    enum class PresentationMode {
        List, Grid
    }

    fun getPresentationMode(): PresentationMode = when (preferences.getString("presentation", "0")) {
        "1" -> PresentationMode.Grid
        else -> PresentationMode.List
    }

    val darkModeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val layoutLiveData = MutableLiveData<PresentationMode>()

    val sortByLiveData = MutableLiveData<Int>()
}


fun Boolean?.toNightMode(): Int = when (this) {
    true -> AppCompatDelegate.MODE_NIGHT_YES
    else -> AppCompatDelegate.MODE_NIGHT_NO
}