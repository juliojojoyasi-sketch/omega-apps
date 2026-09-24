package com.example.omega.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega.routing.NavigationStep
import com.example.omega.routing.RouteResult
import com.example.omega.routing.TurnType
import com.example.ui.theme.GMapBlue
import com.example.ui.theme.GMapBorder
import com.example.ui.theme.GMapGreen
import com.example.ui.theme.GMapRed
import com.example.ui.theme.GMapSurface
import com.example.ui.theme.GMapTextPrimary
import com.example.ui.theme.GMapTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Google Maps style Turn-by-Turn Navigation Header Banner (Green Card)
 */
@Composable
fun OmegaNavigationBanner(
    route: RouteResult,
    currentStepIndex: Int,
    onStopNavigation: () -> Unit,
    onNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val safeIndex = currentStepIndex.coerceIn(0, (route.steps.size - 1).coerceAtLeast(0))
    val currentStep = if (route.steps.isNotEmpty()) route.steps[safeIndex] else null

    // Google Maps signature Green Navigation Header
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0F9D58), // Google Navigation Green
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Main instruction row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Turn Icon Circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    val turnAngle = when (currentStep?.turnType) {
                        TurnType.TURN_LEFT, TurnType.SHARP_LEFT -> -90f
                        TurnType.TURN_RIGHT, TurnType.SHARP_RIGHT -> 90f
                        TurnType.SLIGHT_LEFT -> -45f
                        TurnType.SLIGHT_RIGHT -> 45f
                        TurnType.ROUNDABOUT -> 180f
                        else -> 0f
                    }
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Direction",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(turnAngle)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Instruction & Street Name
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentStep?.instruction ?: "Suivre la route",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    val road = currentStep?.roadName?.ifEmpty { "Vers ${route.destinationName}" } ?: route.destinationName
                    Text(
                        text = road,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }

                // Close / Exit Navigation Button
                IconButton(
                    onClick = onStopNavigation,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .testTag("stop_navigation_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Arrêter l'itinéraire",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Google Maps Bottom info strip: Remaining time (large) • Distance • ETA
            val minutes = (route.totalDurationSeconds / 60).toInt()
            val hours = minutes / 60
            val remMinutes = minutes % 60
            val timeText = if (hours > 0) "${hours} h ${remMinutes} min" else "$minutes min"
            val distanceKm = String.format(Locale.US, "%.1f km", route.totalDistanceMeters / 1000.0)

            val etaDate = Date(System.currentTimeMillis() + (route.totalDurationSeconds * 1000).toLong())
            val etaTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(etaDate)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = timeText,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  $distanceKm",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  $etaTime",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Next step button
                    IconButton(
                        onClick = onNextStep,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Étape suivante",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Expand / Collapse Route sheet
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Détails étapes",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Expanded Steps Sheet
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Étapes de l'itinéraire :",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((route.steps.size * 48).coerceAtMost(160).dp)
                    ) {
                        itemsIndexed(route.steps) { index, step ->
                            val isCurrent = index == safeIndex
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isCurrent) Color.White else Color.White.copy(alpha = 0.5f))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step.instruction,
                                    color = if (isCurrent) Color.White else Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${step.distanceMeters.toInt()} m",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
