package com.example.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.omega.data.local.entity.HistoryEntity
import com.example.omega.data.local.entity.SearchPoiEntity
import com.example.omega.gps.GpsData
import com.example.omega.routing.MadagascarRoadData
import com.example.ui.theme.OmegaAmberGold
import com.example.ui.theme.OmegaDarkBackground
import com.example.ui.theme.OmegaDarkSurface
import com.example.ui.theme.OmegaDarkSurfaceBorder
import com.example.ui.theme.OmegaDarkSurfaceElevated
import com.example.ui.theme.OmegaEmeraldGreen
import com.example.ui.theme.OmegaNeonCyan
import com.example.ui.theme.OmegaTextPrimary
import com.example.ui.theme.OmegaTextSecondary
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun OmegaSearchDialog(
    gpsData: GpsData,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    searchResults: List<SearchPoiEntity>,
    searchHistory: List<HistoryEntity>,
    isSearching: Boolean,
    onSelectPoi: (SearchPoiEntity, navigate: Boolean) -> Unit,
    onAddFavorite: (SearchPoiEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val categories = listOf("TOUT", "VILLE", "RUE", "VILLAGE", "LIEU", "TOURISME", "TRANSPORT", "NATURE")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = OmegaDarkBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Search Bar Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(OmegaDarkSurfaceElevated)
                            .testTag("search_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = OmegaNeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .testTag("search_input_field"),
                        placeholder = {
                            Text(
                                text = "Villes, rues, villages, POI...",
                                color = OmegaTextSecondary,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = OmegaNeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Effacer",
                                        tint = OmegaTextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OmegaNeonCyan,
                            unfocusedBorderColor = OmegaDarkSurfaceBorder,
                            focusedTextColor = OmegaTextPrimary,
                            unfocusedTextColor = OmegaTextPrimary,
                            cursorColor = OmegaNeonCyan,
                            focusedContainerColor = OmegaDarkSurface,
                            unfocusedContainerColor = OmegaDarkSurface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                    )
                }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) OmegaNeonCyan.copy(alpha = 0.2f)
                                    else OmegaDarkSurface
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) OmegaNeonCyan else OmegaDarkSurfaceBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { onCategorySelected(cat) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = getCategoryLabel(cat),
                                color = if (isSelected) OmegaNeonCyan else OmegaTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = OmegaNeonCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else if (searchQuery.isBlank()) {
                    // Search history & Popular Madagascar suggestions
                    Column {
                        if (searchHistory.isNotEmpty()) {
                            Text(
                                text = "RECHERCHES RÉCENTES :",
                                color = OmegaTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            searchHistory.take(5).forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onQueryChange(item.query) }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = OmegaTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = item.query,
                                        color = OmegaTextPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(
                            text = "DESTINATIONS PHARES MADAGASCAR :",
                            color = OmegaTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )

                        // Show default curated results
                        SearchResultsList(
                            results = searchResults.take(15),
                            gpsData = gpsData,
                            onSelectPoi = onSelectPoi,
                            onAddFavorite = onAddFavorite
                        )
                    }
                } else if (searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucun lieu trouvé pour « $searchQuery »",
                            color = OmegaTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    SearchResultsList(
                        results = searchResults,
                        gpsData = gpsData,
                        onSelectPoi = onSelectPoi,
                        onAddFavorite = onAddFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<SearchPoiEntity>,
    gpsData: GpsData,
    onSelectPoi: (SearchPoiEntity, navigate: Boolean) -> Unit,
    onAddFavorite: (SearchPoiEntity) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(results) { poi ->
            val dist = MadagascarRoadData.calculateHaversineDistance(
                gpsData.latitude, gpsData.longitude,
                poi.latitude, poi.longitude
            )
            val distFormatted = if (dist >= 1000) {
                String.format(Locale.US, "%.0f km", dist / 1000.0)
            } else {
                "${dist.roundToInt()} m"
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = OmegaDarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectPoi(poi, false) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category icon badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(OmegaDarkSurfaceElevated)
                            .border(0.8.dp, OmegaDarkSurfaceBorder, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getCategoryEmoji(poi.category),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = poi.name,
                            color = OmegaTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (poi.localName.isNotEmpty() && poi.localName != poi.name) {
                            Text(
                                text = poi.localName,
                                color = OmegaNeonCyan.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${poi.region} • $distFormatted",
                            color = OmegaTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Action buttons: Favorite & Direct Route
                    Row {
                        IconButton(
                            onClick = { onAddFavorite(poi) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favori",
                                tint = OmegaAmberGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { onSelectPoi(poi, true) },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(OmegaNeonCyan.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = "Itinéraire",
                                tint = OmegaNeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getCategoryEmoji(cat: String): String {
    return when (cat) {
        "VILLE" -> "🏙️"
        "RUE" -> "🛣️"
        "VILLAGE" -> "🏡"
        "LIEU" -> "📍"
        "TOURISME" -> "🌴"
        "TRANSPORT" -> "✈️"
        "NATURE" -> "🏞️"
        else -> "📌"
    }
}

private fun getCategoryLabel(cat: String): String {
    return when (cat) {
        "TOUT" -> "Tout"
        "VILLE" -> "Villes"
        "RUE" -> "Rues & RN"
        "VILLAGE" -> "Villages"
        "LIEU" -> "Lieux"
        "TOURISME" -> "Tourisme"
        "TRANSPORT" -> "Transports"
        "NATURE" -> "Nature & Parcs"
        else -> cat
    }
}
