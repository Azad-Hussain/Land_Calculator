package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LandAreaUnit
import com.example.model.LandConverterEngine
import com.example.model.UnitCategory
import com.example.ui.components.CompoundAreaCard
import com.example.ui.components.LaggiConfigDialog
import com.example.ui.components.UnitResultCard
import com.example.viewmodel.LandConverterUiState
import com.example.viewmodel.LandConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaConverterScreen(
    viewModel: LandConverterViewModel,
    uiState: LandConverterUiState,
    modifier: Modifier = Modifier
) {
    val isHindi = uiState.isHindi
    var showLaggiDialog by remember { mutableStateOf(false) }
    var showUnitPicker by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf<UnitCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val quickAreaPresets = listOf("1", "5", "10", "20", "50", "100", "247", "500")

    val filteredResults = uiState.areaResults.filter { item ->
        val matchesCategory = selectedCategoryFilter == null || item.unit.category == selectedCategoryFilter
        val matchesSearch = searchQuery.isBlank() ||
                item.unit.nameEn.contains(searchQuery, ignoreCase = true) ||
                item.unit.nameHi.contains(searchQuery, ignoreCase = true) ||
                item.unit.symbolEn.contains(searchQuery, ignoreCase = true) ||
                item.unit.symbolHi.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    if (showLaggiDialog) {
        LaggiConfigDialog(
            isHindi = isHindi,
            currentHaath = uiState.laggiHaath,
            selectedDistrict = uiState.selectedDistrict,
            isCustom = uiState.isCustomLaggi,
            customInput = uiState.customLaggiInput,
            onDistrictSelected = { district ->
                viewModel.onDistrictSelected(district)
            },
            onHaathSelected = { haath ->
                viewModel.onLaggiHaathSelected(haath)
            },
            onCustomInputChanged = { input ->
                viewModel.onCustomLaggiChanged(input)
            },
            onDismiss = { showLaggiDialog = false }
        )
    }

    // Source Unit Picker Bottom Sheet / Dialog
    if (showUnitPicker) {
        AlertDialog(
            onDismissRequest = { showUnitPicker = false },
            title = {
                Text(
                    text = if (isHindi) "स्रोत इकाई चुनें (Source Unit)" else "Select Source Unit",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        UnitCategory.values().forEach { cat ->
                            item {
                                Text(
                                    text = if (isHindi) cat.titleHi else cat.titleEn,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                                )
                            }
                            val unitsInCat = LandAreaUnit.allUnits.filter { it.category == cat }
                            items(unitsInCat) { u ->
                                val isSelected = u.id == uiState.selectedSourceUnit.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                        .clickable {
                                            viewModel.onSourceUnitSelected(u)
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
        // Top Laggi & District Selector Badge Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showLaggiDialog = true }
                    .testTag("laggi_banner"),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Straighten,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "लग्गी माप (कट्ठा/बीघा आधार)" else "Laggi Length Standard",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                            val distName = if (uiState.isCustomLaggi) {
                                if (isHindi) "कस्टम लग्गी" else "Custom Laggi"
                            } else {
                                if (isHindi) uiState.selectedDistrict.nameHi else uiState.selectedDistrict.nameEn
                            }
                            Text(
                                text = "$distName: ${uiState.laggiHaath} ${if (isHindi) "हाथ" else "Haath"} (${LandConverterEngine.formatNumber(uiState.laggiHaath * 1.5, 2)} ft)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isHindi) "बदलें" else "Change",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                    .testTag("area_input_card"),
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
                        text = if (isHindi) "क्षेत्रफल दर्ज करें (Enter Area Value)" else "Enter Area Value",
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
                        // Numeric Input Field
                        OutlinedTextField(
                            value = uiState.areaInput,
                            onValueChange = { viewModel.onAreaInputChanged(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            trailingIcon = {
                                if (uiState.areaInput.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onAreaInputChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear Input",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("area_value_input")
                        )

                        // Source Unit Selector Button
                        Surface(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showUnitPicker = true }
                                .testTag("source_unit_selector"),
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
                                        text = if (isHindi) uiState.selectedSourceUnit.nameHi else uiState.selectedSourceUnit.nameEn,
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

                    // Quick Presets Row
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
                            items(quickAreaPresets) { preset ->
                                val isSelected = uiState.areaInput == preset
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.onAreaInputChanged(preset) }
                                        .testTag("preset_$preset")
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

        // Prominent Traditional Compound Breakdown Card
        item {
            CompoundAreaCard(
                isHindi = isHindi,
                bighaCompound = uiState.bighaCompound,
                acreCompound = uiState.acreCompound,
                totalSqFt = uiState.currentSqFt,
                laggiHaath = uiState.laggiHaath,
                onSaveHistory = { viewModel.saveCurrentToHistory() }
            )
        }

        // Category Filter Chips and Search
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "सभी इकाइयों में रूपांतरण" else "All Converted Units",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredResults.size} ${if (isHindi) "इकाइयाँ" else "Units"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategoryFilter == null,
                            onClick = { selectedCategoryFilter = null },
                            label = { Text(if (isHindi) "सभी (All)" else "All Units") },
                            modifier = Modifier.testTag("filter_all")
                        )
                    }
                    items(UnitCategory.values()) { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(if (isHindi) cat.titleHi else cat.titleEn) },
                            modifier = Modifier.testTag("filter_${cat.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Converted Results List
        items(filteredResults, key = { it.unit.id }) { item ->
            UnitResultCard(
                isHindi = isHindi,
                item = item,
                isSourceUnit = item.unit.id == uiState.selectedSourceUnit.id,
                onSetAsSource = {
                    viewModel.onSourceUnitSelected(item.unit)
                    viewModel.onAreaInputChanged(item.formattedValue)
                }
            )
        }
    }
}
