package com.example.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega.data.local.entity.FavoriteEntity
import com.example.omega.data.local.entity.HistoryEntity
import com.example.omega.data.local.entity.MarkerEntity
import com.example.ui.theme.OmegaAmberGold
import com.example.ui.theme.OmegaCrimson
import com.example.ui.theme.OmegaDarkBackground
import com.example.ui.theme.OmegaDarkSurface
import com.example.ui.theme.OmegaDarkSurfaceBorder
import com.example.ui.theme.OmegaDarkSurfaceElevated
import com.example.ui.theme.OmegaEmeraldGreen
import com.example.ui.theme.OmegaNeonCyan
import com.example.ui.theme.OmegaTextPrimary
import com.example.ui.theme.OmegaTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmegaFavoritesSheet(
    markers: List<MarkerEntity>,
    favorites: List<FavoriteEntity>,
    history: List<HistoryEntity>,
    onSelectLocation: (lat: Double, lon: Double, name: String, navigate: Boolean) -> Unit,
    onDeleteMarker: (MarkerEntity) -> Unit,
    onDeleteFavorite: (FavoriteEntity) -> Unit,
    onClearHistory: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Marqueurs (${markers.size})", "Favoris (${favorites.size})", "Historique")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = OmegaDarkSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(OmegaDarkSurfaceBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.78f)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "BIBLIOTHÈQUE LOCALE HORS LIGNE",
                    color = OmegaNeonCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = OmegaTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = OmegaDarkSurfaceElevated,
                contentColor = OmegaNeonCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = OmegaNeonCyan
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, OmegaDarkSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    // Markers list
                    if (markers.isEmpty()) {
                        EmptyStateMessage("Aucun marqueur enregistré", "Appuyez sur 📍+ sur la carte pour créer un repère hors ligne.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(markers) { marker ->
                                val markerColor = try {
                                    Color(android.graphics.Color.parseColor(marker.colorHex))
                                } catch (e: Exception) {
                                    OmegaNeonCyan
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = OmegaDarkBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(CircleShape)
                                                .background(markerColor)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    onSelectLocation(marker.latitude, marker.longitude, marker.title, false)
                                                }
                                        ) {
                                            Text(
                                                text = marker.title,
                                                color = OmegaTextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (marker.note.isNotEmpty()) {
                                                Text(
                                                    text = marker.note,
                                                    color = OmegaTextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                            Text(
                                                text = "${marker.category} • Lat: ${String.format(Locale.US, "%.4f", marker.latitude)}, Lon: ${String.format(Locale.US, "%.4f", marker.longitude)}",
                                                color = OmegaTextSecondary.copy(alpha = 0.7f),
                                                fontSize = 10.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                onSelectLocation(marker.latitude, marker.longitude, marker.title, true)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Directions,
                                                contentDescription = "Naviguer",
                                                tint = OmegaNeonCyan,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { onDeleteMarker(marker) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Supprimer",
                                                tint = OmegaCrimson.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Favorites list
                    if (favorites.isEmpty()) {
                        EmptyStateMessage("Aucun lieu favori", "Ajoutez vos villes, plages ou étapes favorites depuis la recherche ou la carte.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(favorites) { fav ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = OmegaDarkBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = OmegaAmberGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    onSelectLocation(fav.latitude, fav.longitude, fav.title, false)
                                                }
                                        ) {
                                            Text(
                                                text = fav.title,
                                                color = OmegaTextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (fav.subtitle.isNotEmpty()) {
                                                Text(
                                                    text = fav.subtitle,
                                                    color = OmegaTextSecondary,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = {
                                                onSelectLocation(fav.latitude, fav.longitude, fav.title, true)
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Directions,
                                                contentDescription = "Naviguer",
                                                tint = OmegaNeonCyan,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { onDeleteFavorite(fav) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Supprimer",
                                                tint = OmegaCrimson.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // History list
                    if (history.isEmpty()) {
                        EmptyStateMessage("Historique vide", "Vos itinéraires calculés et destinations récentes apparaîtront ici.")
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "TOUT EFFACER",
                                    color = OmegaCrimson,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier
                                        .clickable { onClearHistory() }
                                        .padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(history) { item ->
                                    val dateStr = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = OmegaDarkBackground,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (item.type == "ROUTE") Icons.Default.Directions else Icons.Default.History,
                                                contentDescription = null,
                                                tint = if (item.type == "ROUTE") OmegaEmeraldGreen else OmegaTextSecondary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                        if (item.latitude != 0.0 && item.longitude != 0.0) {
                                                            onSelectLocation(item.latitude, item.longitude, item.destinationTitle.ifEmpty { item.query }, item.type == "ROUTE")
                                                        }
                                                    }
                                            ) {
                                                Text(
                                                    text = item.destinationTitle.ifEmpty { item.query },
                                                    color = OmegaTextPrimary,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                if (item.type == "ROUTE" && item.distanceMeters > 0) {
                                                    val km = item.distanceMeters / 1000.0
                                                    val mins = item.durationSeconds / 60
                                                    Text(
                                                        text = "${String.format(Locale.US, "%.1f km", km)} • ${mins} min • $dateStr",
                                                        color = OmegaTextSecondary,
                                                        fontSize = 11.sp,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                } else {
                                                    Text(
                                                        text = "Recherche • $dateStr",
                                                        color = OmegaTextSecondary,
                                                        fontSize = 11.sp,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateMessage(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = OmegaTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = OmegaTextSecondary,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}
