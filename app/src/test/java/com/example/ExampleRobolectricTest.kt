package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.omega.routing.OfflineRoutingEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cartes", appName)
  }

  @Test
  fun `test offline routing engine from Antananarivo to Antsirabe`() {
    val engine = OfflineRoutingEngine()
    // Antananarivo (-18.9102, 47.5255) to Antsirabe (-19.8659, 47.0333)
    val route = engine.calculateRoute(
        startLat = -18.9102,
        startLon = 47.5255,
        destLat = -19.8659,
        destLon = 47.0333,
        destinationName = "Antsirabe"
    )

    assertNotNull("Route should be successfully calculated", route)
    assertTrue("Route must contain navigation steps", route!!.steps.isNotEmpty())
    assertTrue("Route distance should be > 100 km", route.totalDistanceMeters > 100_000)
    assertTrue("Route duration should be positive", route.totalDurationSeconds > 0)
    assertEquals("Antsirabe", route.destinationName)
  }
}

