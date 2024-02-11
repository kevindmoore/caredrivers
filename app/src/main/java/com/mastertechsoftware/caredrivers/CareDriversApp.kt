package com.mastertechsoftware.caredrivers

import android.app.Application
import timber.log.Timber

/**
 * App uses for setting up Timber
 */
class CareDriversApp : Application() {
  override fun onCreate() {
    super.onCreate()
    // Install a Timber tree.
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}