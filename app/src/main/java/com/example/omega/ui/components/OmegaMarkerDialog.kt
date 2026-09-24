package com.example.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import java.util.Locale

@Composable
fun OmegaMarkerDialog(
    latitude: Double,
    longitude: Double,
    onSaveMarker: (title: String, note: String, category: String, colorHex: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Point d'intérêt") }
    var selectedColor by remember { mutableStateOf("#00E5FF") }

    val categories = listOf("Point d'intérêt", "Étape", "Danger", "Bivouac", "Vue", "Ravitaillement")
    val colors = listOf("#00E5FF", "#38BDF8", "#FFB300", "#10B981", "#F43F5E", "#A855F7")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = OmegaDarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, OmegaDarkSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "NOUVEAU MARQUEUR OMEGA",
                    color = OmegaNeonCyan,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = String.format(Locale.US, "Coord: %.5f, %.5f", latitude, longitude),
                    color = OmegaTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nom du marqueur *") },
                    placeholder = { Text("Ex: Vue panoramique, Point de rendez-vous") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("marker_title_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OmegaNeonCyan,
                        unfocusedBorderColor = OmegaDarkSurfaceBorder,
                        focusedTextColor = OmegaTextPrimary,
                        unfocusedTextColor = OmegaTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Note Input
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note ou détails (optionnel)") },
                    placeholder = { Text("Ex: Piste praticable 4x4, eau disponible") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("marker_note_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OmegaNeonCyan,
                        unfocusedBorderColor = OmegaDarkSurfaceBorder,
                        focusedTextColor = OmegaTextPrimary,
                        unfocusedTextColor = OmegaTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category chips
                Text(
                    text = "CATÉGORIE :",
                    color = OmegaTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        CategoryChip(cat, selectedCategory == cat) { selectedCategory = cat }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.drop(3).forEach { cat ->
                        CategoryChip(cat, selectedCategory == cat) { selectedCategory = cat }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Color selector
                Text(
                    text = "COULEUR DE REPÈRE :",
                    color = OmegaTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colors.forEach { hex ->
                        val parsedColor = Color(android.graphics.Color.parseColor(hex))
                        val isSelected = hex == selectedColor
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                                .border(
                                    if (isSelected) 2.5.dp else 0.dp,
                                    if (isSelected) OmegaTextPrimary else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OmegaTextSecondary)
                    ) {
                        Text("Annuler")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSaveMarker(title.trim(), note.trim(), selectedCategory, selectedColor)
                            }
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OmegaNeonCyan,
                            contentColor = OmegaDarkBackground
                        ),
                        modifier = Modifier.testTag("save_marker_button")
                    ) {
                        Text("Enregistrer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) OmegaNeonCyan.copy(alpha = 0.2f) else OmegaDarkSurfaceElevated)
            .border(1.dp, if (isSelected) OmegaNeonCyan else OmegaDarkSurfaceBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = name,
            color = if (isSelected) OmegaNeonCyan else OmegaTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
