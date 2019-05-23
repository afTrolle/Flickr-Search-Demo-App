package dev.trolle.flickr.search.demo.view.application

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import dev.trolle.flickr.search.demo.dagger.ApplicationComponent
import dev.trolle.flickr.search.demo.dagger.ApplicationModule
import dev.trolle.flickr.search.demo.dagger.DaggerApplicationComponent
import java.lang.Exception

class MyApplication : Application(), ComponentProvider {

    override val component: ApplicationComponent by lazy {
        val applicationModule = ApplicationModule(this)
        DaggerApplicationComponent.builder().applicationModule(applicationModule).build()
    }

    //val applicationModule = ApplicationModule(this)
    // val applicationComponent = DaggerApplicationComponent.builder().applicationModule(applicationModule).build()


}

interface ComponentProvider {
    val component: ApplicationComponent
}

val Activity.injector get() = (application as ComponentProvider).component
val Fragment.injector get() = this.activity?.injector ?: throw Exception("Fragment not bound to activity")