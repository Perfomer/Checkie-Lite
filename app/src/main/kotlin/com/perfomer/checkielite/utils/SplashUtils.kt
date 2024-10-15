package com.perfomer.checkielite.utils

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.perfomer.checkielite.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun Activity.installSplashScreen() {
    setTheme(R.style.Theme_Checkie)
}

internal fun ComponentActivity.holdSplashFor(delayMs: Long) {
    if (delayMs <= 0) return

    val content: View = findViewById(android.R.id.content)
    var isSplashReady = false

    lifecycleScope.launch {
        delay(500L)
        isSplashReady = true
    }

    content.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (isSplashReady) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                }

                return isSplashReady
            }
        }
    )
}