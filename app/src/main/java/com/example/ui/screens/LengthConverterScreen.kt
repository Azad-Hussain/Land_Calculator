package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LengthUnit
import com.example.viewmodel.LandConverterUiState
import com.example.viewmodel.LandConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LengthConverterScreen(
    viewModel: LandConverterViewModel,
    uiState: LandConverterUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi = uiState.isHindi
    var showUnitPicker by remember { mutableStateOf(false) }

    val quickLengthPresets = listOf("1", "2", "5", "10", "44", "66", "100")

    if (showUnitPicker) {
        AlertDialog(
            onDismissRequest = { showUnitPicker = false },
            title = {
                Text(
                    text = if (isHindi) "लंबाई स्रोत इकाई चुनें" else "Select Length Source Unit",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp)) {
                    items(LengthUnit.allUnits) { u ->
                        val isSelected = u.id == uiState.selectedSourceLengthUnit.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                                .clickable {
                                    viewModel.onSourceLengthUnitSelected(u)
                                    showUnitPicker = false
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isHindi) u.nameHi else u.nameEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isHindi) u.descriptionHi else u.descriptionEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUnitPicker = false }) {
                    Text(if (isHindi) "बंद करें" else "Close")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Quick Amin Survey Standards Highlight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("length_constants_banner"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Straighten,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "प्रमुख लंबाई स्थिरांक (अमीन संदर्भ)" else "Key Survey Length Constants",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = if (isHindi) "• 1 जरीब = 100 कड़ी = 66 फीट = 22 गज = 44 हाथ\n• 1 हाथ = 18 इंच = 1.5 फीट = 2.27 कड़ी = 2 बित्ता = 3 मुट्ठी\n• 1 बित्ता = 9 इंच (0.75 फीट) | 1 मुट्ठी = 6 इंच (0.5 फीट)\n• 1 कड़ी = 7.92 इंच = 0.66 फीट = 0.201168 मीटर"
                        else "• 1 Jarib = 100 Kadi = 66 Feet = 22 Yards = 44 Haath\n• 1 Haath = 18 Inches = 1.5 Feet = 2.27 Kadi = 2 Bitta = 3 Mutthi\n• 1 Bitta = 9 Inches (0.75 Ft) | 1 Mutthi = 6 Inches (0.5 Ft)\n• 1 Kadi = 7.92 Inches = 0.66 Feet = 0.201168 m",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("length_input_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isHindi) "लंबाई मान दर्ज करें (Enter Length Value)" else "Enter Length Value",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.lengthInput,
                            onValueChange = { viewModel.onLengthInputChanged(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            trailingIcon = {
                                if (uiState.lengthInput.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onLengthInputChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("length_value_input")
                        )

                        Surface(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showUnitPicker = true }
                                .testTag("length_unit_selector"),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isHindi) "स्रोत इकाई" else "Source Unit",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = if (isHindi) uiState.selectedSourceLengthUnit.nameHi else uiState.selectedSourceLengthUnit.nameEn,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Unit",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "त्वरित मान:" else "Presets:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(quickLengthPresets) { preset ->
                                val isSelected = uiState.lengthInput == preset
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onLengthInputChanged(preset) }
                                        .testTag("length_preset_$preset")
                                ) {
                                    Text(
                                        text = preset,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = if (isHindi) "रूपांतरित लंबाई मान" else "Converted Length Values",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Converted items
        items(uiState.lengthResults, key = { it.unit.id }) { item ->
            val u = item.unit
            val isSource = u.id == uiState.selectedSourceLengthUnit.id
            val uName = if (isHindi) u.nameHi else u.nameEn
            val uDesc = if (isHindi) u.descriptionHi else u.descriptionEn
            val uSymbol = if (isHindi) u.symbolHi else u.symbolEn

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("length_result_${u.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSource) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSource) 3.dp else 1.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSource) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!isSource) {
                                TextButton(
                                    onClick = {
                                        viewModel.onSourceLengthUnitSelected(u)
                                        viewModel.onLengthInputChanged(item.formattedValue)
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(32.dp).testTag("set_len_source_${u.id}")
                                ) {
                                    Text(
                                        text = if (isHindi) "स्रोत बनाएं" else "Convert From",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    val copyText = "${item.formattedValue} $uSymbol"
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Length Value", copyText))
                                    Toast.makeText(context, "$uSymbol ${if (isHindi) "कॉपी हो गया!" else "copied!"}", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp).testTag("copy_length_${u.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = item.formattedValue,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("length_val_${u.id}")
                        )
                        Text(
                            text = uSymbol,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = uDesc,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
