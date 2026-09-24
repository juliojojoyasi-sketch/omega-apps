package com.example.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.omega.data.local.entity.OfflineRegionEntity
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

@Composable
fun OmegaOfflineRegionsDialog(
    regions: List<OfflineRegionEntity>,
    downloadingRegionId: String?,
    downloadProgress: Float,
    cacheSizeBytes: Long,
    onDownloadRegion: (OfflineRegionEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val cacheMb = cacheSizeBytes / (1024.0 * 1024.0)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp)),
            color = OmegaDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.2.dp, OmegaDarkSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CARTES HORS LIGNE OMEGA",
                            color = OmegaNeonCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Téléchargez les zones pour une utilisation 100% sans Internet",
                            color = OmegaTextSecondary,
                            fontSize = 11.sp
                        )
                    }

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

                Spacer(modifier = Modifier.height(14.dp))

                // Storage usage card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = OmegaDarkBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = OmegaEmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "STOCKAGE CARTOGRAPHIQUE LOCAL",
                                color = OmegaTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f Mo utilisés dans le cache hors ligne", cacheMb),
                                color = OmegaTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of regions
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(regions) { region ->
                        val isDownloading = downloadingRegionId == region.id

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = OmegaDarkBackground,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (region.isDownloaded) OmegaEmeraldGreen.copy(alpha = 0.5f) else OmegaDarkSurfaceBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = region.name,
                                                color = OmegaTextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (region.isDownloaded) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Téléchargé",
                                                    tint = OmegaEmeraldGreen,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = region.description,
                                            color = OmegaTextSecondary,
                                            fontSize = 11.sp,
                                            maxLines = 2
                                        )
                                        Text(
                                            text = "Taille estimée : ~${region.estimatedSizeMb} Mo • Zoom ${region.minZoom}-${region.maxZoom}",
                                            color = OmegaAmberGold,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    if (isDownloading) {
                                        CircularProgressIndicator(
                                            color = OmegaNeonCyan,
                                            modifier = Modifier.size(28.dp),
                                            strokeWidth = 2.5.dp
                                        )
                                    } else {
                                        Button(
                                            onClick = { onDownloadRegion(region) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (region.isDownloaded) OmegaDarkSurfaceElevated else OmegaNeonCyan,
                                                contentColor = if (region.isDownloaded) OmegaEmeraldGreen else OmegaDarkBackground
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("download_region_${region.id}")
                                        ) {
                                            Text(
                                                text = if (region.isDownloaded) "Prêt" else "Télécharger",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                if (isDownloading) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { downloadProgress },
                                        color = OmegaNeonCyan,
                                        trackColor = OmegaDarkSurfaceBorder,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Téléchargement des tuiles en cours : ${(downloadProgress * 100).toInt()}%",
                                        color = OmegaNeonCyan,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Legal and open source note
                Text(
                    text = "Sources : OpenStreetMap © Contributeurs (ODbL), Imagerie aérienne Esri World Imagery, Données altimétriques SRTM / OpenTopoMap.",
                    color = OmegaTextSecondary.copy(alpha = 0.7f),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
