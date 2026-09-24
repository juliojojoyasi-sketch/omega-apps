package com.example.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega.gps.GpsData
import com.example.ui.theme.GMapBlue
import com.example.ui.theme.GMapBorder
import com.example.ui.theme.GMapRed
import com.example.ui.theme.GMapSurface
import com.example.ui.theme.GMapTextPrimary
import com.example.ui.theme.GMapTextSecondary
import com.example.ui.theme.GMapTextSubtle
import kotlin.math.roundToInt

/**
 * Google Maps style Map HUD controls:
 * - Floating clean white circular action buttons (Recenter, Compass, Zoom In/Out, Add Pin)
 * - Minimal speed badge in the bottom-left corner
 */
@Composable
fun OmegaCompassHud(
    gpsData: GpsData,
    compassAzimuth: Float,
    isCompassFollowing: Boolean,
    onRecenterClick: () -> Unit,
    onCompassClick: () -> Unit,
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
    onAddMarkerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Left: Clean Google Maps style Speedometer Pill (appears when moving)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = GMapSurface,
            shadowElevation = 3.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${gpsData.speedKmh.roundToInt()}",
                    color = GMapTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "km/h",
                        color = GMapTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    gpsData.altitude?.let { alt ->
                        Text(
                            text = "${alt.toInt()} m",
                            color = GMapTextSubtle,
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }

        // Right: Google Maps Vertical Floating Circular Action Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Compass Button (Rotate with map azimuth)
            Surface(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable { onCompassClick() }
                    .testTag("compass_button"),
                shape = CircleShape,
                color = GMapSurface,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "Boussole",
                        tint = if (isCompassFollowing) GMapRed else GMapTextSecondary,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(-compassAzimuth)
                    )
                }
            }

            // Add Marker / Place Pin Button
            Surface(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable { onAddMarkerClick() }
                    .testTag("add_marker_button"),
                shape = CircleShape,
                color = GMapSurface,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLocationAlt,
                        contentDescription = "Ajouter un repère",
                        tint = GMapRed,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Zoom In / Zoom Out pill
            Surface(
                shape = RoundedCornerShape(23.dp),
                color = GMapSurface,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { onZoomInClick() }
                            .testTag("zoom_in_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Zoom avant",
                            tint = GMapTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(1.dp)
                            .background(GMapBorder)
                    )
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { onZoomOutClick() }
                            .testTag("zoom_out_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Zoom arrière",
                            tint = GMapTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Google Maps GPS Recenter Button (Blue target / my location)
            Surface(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable { onRecenterClick() }
                    .testTag("recenter_gps_button"),
                shape = CircleShape,
                color = GMapSurface,
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Centrer sur ma position",
                        tint = GMapBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
