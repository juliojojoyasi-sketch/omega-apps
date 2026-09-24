package com.example.omega.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega.gps.GpsData
import com.example.ui.theme.GMapBlue
import com.example.ui.theme.GMapBorder
import com.example.ui.theme.GMapGreen
import com.example.ui.theme.GMapSurface
import com.example.ui.theme.GMapTextPrimary
import com.example.ui.theme.GMapTextSecondary
import com.example.ui.theme.GMapYellow

/**
 * Google Maps style search bar & header card
 * Clean floating white card with pill shadow, search input, offline indicator, and quick actions
 */
@Composable
fun OmegaTopBar(
    gpsData: GpsData,
    compassAzimuth: Float,
    isEcoMode: Boolean,
    onSearchClick: () -> Unit,
    onEcoToggle: () -> Unit,
    onFavoritesClick: () -> Unit,
    onOfflineRegionsClick: () -> Unit,
    onLandingPageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // 1. Google Maps style Floating Search Pill
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .clickable { onSearchClick() }
                .testTag("search_bar_trigger"),
            shape = RoundedCornerShape(26.dp),
            color = GMapSurface,
            shadowElevation = 4.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search icon
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher",
                    tint = GMapTextSecondary,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Placeholder text
                Text(
                    text = "Rechercher ici (hors ligne)...",
                    color = GMapTextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )

                // Offline badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GMapGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OfflinePin,
                        contentDescription = "Hors ligne",
                        tint = GMapGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Hors ligne",
                        color = GMapGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Favorites button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(GMapBlue)
                        .clickable { onFavoritesClick() }
                        .testTag("favorites_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Favoris",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Google Maps Category / Status Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Website / Download Page chip
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = GMapSurface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBlue.copy(alpha = 0.5f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onLandingPageClick() }
                    .testTag("landing_page_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = GMapBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Site & APK",
                        color = GMapBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Offline packs chip
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = GMapSurface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.8f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onOfflineRegionsClick() }
                    .testTag("offline_regions_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = GMapBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Packs",
                        color = GMapTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // GPS Status Chip
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = GMapSurface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.8f)),
                modifier = Modifier.clip(RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (gpsData.isFixAcquired) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                        contentDescription = null,
                        tint = if (gpsData.isFixAcquired) GMapGreen else GMapYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (gpsData.isFixAcquired) "GPS Actif" else "Recherche GPS",
                        color = GMapTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Eco toggle chip
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (isEcoMode) GMapGreen.copy(alpha = 0.15f) else GMapSurface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isEcoMode) GMapGreen else GMapBorder.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onEcoToggle() }
                    .testTag("eco_toggle_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEcoMode) "Éco: On" else "Éco: Off",
                        color = if (isEcoMode) GMapGreen else GMapTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
