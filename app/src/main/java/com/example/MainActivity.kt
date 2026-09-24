package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.omega.ui.screens.OmegaMainScreen
import com.example.ui.theme.MyApplicationTheme
import org.osmdroid.config.Configuration
import java.io.File

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Pre-initialize OSMDroid configuration before MapView components instantiate
    val prefs = getSharedPreferences("omega_osmdroid", Context.MODE_PRIVATE)
    Configuration.getInstance().load(applicationContext, prefs)
    val basePath = File(filesDir, "osmdroid")
    val tilePath = File(basePath, "tiles")
    if (!basePath.exists()) basePath.mkdirs()
    if (!tilePath.exists()) tilePath.mkdirs()

    // Clean up any old blocked or watermarked tile cache
    for (oldDir in listOf("Mapnik", "CartoVoyager")) {
      val oldCache = File(tilePath, oldDir)
      if (oldCache.exists()) {
        oldCache.deleteRecursively()
      }
    }

    Configuration.getInstance().osmdroidBasePath = basePath
    Configuration.getInstance().osmdroidTileCache = tilePath
    Configuration.getInstance().userAgentValue = "OmegaMapsApp/1.0 ($packageName; Android)"

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        OmegaMainScreen()
      }
    }
  }
}

