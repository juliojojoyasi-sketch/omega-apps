package com.example.omega.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega.map.MapLayerType
import com.example.ui.theme.GMapBlue
import com.example.ui.theme.GMapBlueLight
import com.example.ui.theme.GMapBorder
import com.example.ui.theme.GMapSurface
import com.example.ui.theme.GMapTextPrimary
import com.example.ui.theme.GMapTextSecondary

/**
 * Google Maps style layer chip selector (Par défaut, Satellite, Relief)
 */
@Composable
fun OmegaLayerSelector(
    selectedLayer: MapLayerType,
    onLayerSelected: (MapLayerType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = GMapSurface,
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, GMapBorder.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MapLayerType.values().forEach { layer ->
                val isSelected = layer == selectedLayer
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) GMapBlueLight else Color.Transparent,
                    label = "layerBg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) GMapBlue else GMapTextSecondary,
                    label = "layerText"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(bgColor)
                        .clickable { onLayerSelected(layer) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("layer_button_${layer.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val icon = when (layer) {
                            MapLayerType.CARTE -> Icons.Default.Map
                            MapLayerType.SATELLITE -> Icons.Default.SatelliteAlt
                            MapLayerType.RELIEF -> Icons.Default.Terrain
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val label = when (layer) {
                            MapLayerType.CARTE -> "Plan"
                            MapLayerType.SATELLITE -> "Satellite"
                            MapLayerType.RELIEF -> "Relief"
                        }
                        Text(
                            text = label,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
