package com.example.tdd

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.EmptyFragmentActivity.Companion.THEME_EXTRAS_BUNDLE_KEY
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * creating a fragment launcher that helps...
 * ..to attach our fragment to custom hilt activity
 */
@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int = androidx.fragment.testing.manifest.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    /**
     * defined activity because each application..
     * ..needs a activity and in test environment we
     * ..don't have it in default
     */
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(THEME_EXTRAS_BUNDLE_KEY, themeResId)

    /**
     * Created ActivityScenario to launch our activity with..
     * the mainActivityIntent, and in onActivity function we
     * ..get reference to that activity and instantiate our
     * fragment if we have one
     */

    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
        fragmentFactory?.let {
            activity.supportFragmentManager.fragmentFactory = it
        }
        val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader) as ClassLoader,
            T::class.java.name
        )

        fragment.arguments = fragmentArgs

        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
        (fragment as T).action()
    }
}