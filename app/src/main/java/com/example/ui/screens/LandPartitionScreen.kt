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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Remove
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
import com.example.model.LandAreaUnit
import com.example.model.LandConverterEngine
import com.example.viewmodel.LandConverterUiState
import com.example.viewmodel.LandConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandPartitionScreen(
    viewModel: LandConverterViewModel,
    uiState: LandConverterUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi = uiState.isHindi
    var showUnitPicker by remember { mutableStateOf(false) }

    val partitionUnits = listOf(
        LandAreaUnit.Bigha,
        LandAreaUnit.Katha,
        LandAreaUnit.Dhur,
        LandAreaUnit.Dismil,
        LandAreaUnit.Acre,
        LandAreaUnit.SquareFeet,
        LandAreaUnit.SquareMeter
    )

    if (showUnitPicker) {
        AlertDialog(
            onDismissRequest = { showUnitPicker = false },
            title = {
                Text(
                    text = if (isHindi) "इकाई चुनें" else "Select Unit",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                    items(partitionUnits) { u ->
                        val isSelected = u.id == uiState.partitionAreaUnit.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                )
                                .clickable {
                                    viewModel.onPartitionUnitSelected(u)
                                    showUnitPicker = false
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHindi) u.nameHi else u.nameEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
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
        // Banner Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("partition_intro_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "भूमि बंटवारा / हिस्सा कैलकुलेटर" else "Land Partition & Share Calculator",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = if (isHindi) "कुल जमीन को बराबर या अनुपात अनुसार हिस्सेदारों में बांटें" else "Divide total land equally or by custom ratio among shares",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("partition_input_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isHindi) "कुल जमीन दर्ज करें:" else "Enter Total Land Area:",
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
                            value = uiState.partitionAreaInput,
                            onValueChange = { viewModel.onPartitionAreaInputChanged(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("partition_area_val")
                        )

                        Surface(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showUnitPicker = true }
                                .testTag("partition_unit_selector"),
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
                                        text = if (isHindi) "इकाई" else "Unit",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = if (isHindi) uiState.partitionAreaUnit.nameHi else uiState.partitionAreaUnit.nameEn,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Shareholder Count Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "हिस्सेदारों की संख्या:" else "Number of Shares:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledIconButton(
                                onClick = { viewModel.onShareholderCountChanged(uiState.shareholderCount - 1) },
                                enabled = uiState.shareholderCount > 2,
                                modifier = Modifier.size(36.dp).testTag("decrease_shares_btn")
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "${uiState.shareholderCount}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp).testTag("shareholder_count_text")
                            )
                            FilledIconButton(
                                onClick = { viewModel.onShareholderCountChanged(uiState.shareholderCount + 1) },
                                enabled = uiState.shareholderCount < 10,
                                modifier = Modifier.size(36.dp).testTag("increase_shares_btn")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "प्रत्येक हिस्सेदार का भाग" else "Allocated Shares Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        val buffer = StringBuilder()
                        buffer.append(if (isHindi) "भूमि बंटवारा परिणाम:\n" else "Land Partition Summary:\n")
                        uiState.partitionResults.forEach { res ->
                            val name = if (isHindi) res.nameHi else res.nameEn
                            val bigha = if (isHindi) res.bighaCompound.toFormattedHi() else res.bighaCompound.toFormattedEn()
                            val dismil = "${LandConverterEngine.formatNumber(res.areaDismil, 3)} ${if (isHindi) "डिसमिल" else "Dismil"}"
                            buffer.append("• $name: $bigha ($dismil)\n")
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Land Partition", buffer.toString()))
                        Toast.makeText(context, if (isHindi) "पूरा बंटवारा कॉपी हो गया!" else "Copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp).testTag("copy_partition_all")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy All",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Shareholder cards
        itemsIndexed(uiState.partitionResults) { index, res ->
            val name = if (isHindi) res.nameHi else res.nameEn
            val bighaFormatted = if (isHindi) res.bighaCompound.toFormattedHi() else res.bighaCompound.toFormattedEn()
            val dismilFormatted = "${LandConverterEngine.formatNumber(res.areaDismil, 3)} ${if (isHindi) "डिसमिल" else "Dismil"}"
            val sqFtFormatted = "${LandConverterEngine.formatNumber(res.areaSqFt, 2)} ${if (isHindi) "वर्ग फीट" else "Sq Ft"}"

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shareholder_card_${res.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${res.id}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${LandConverterEngine.formatNumber(res.percentage, 1)}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = bighaFormatted,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dismilFormatted,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = sqFtFormatted,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
