package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LandConverterEngine
import com.example.model.LengthUnit
import com.example.viewmodel.LandConverterUiState
import com.example.viewmodel.LandConverterViewModel
import com.example.viewmodel.PlotShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlotMeasurementScreen(
    viewModel: LandConverterViewModel,
    uiState: LandConverterUiState,
    onNavigateToAreaConverter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi = uiState.isHindi

    val sqFt = uiState.calculatedPlotSqFt
    val dismil = sqFt / LandConverterEngine.SQ_FT_PER_DISMIL
    val acre = sqFt / LandConverterEngine.SQ_FT_PER_ACRE
    val sqMeter = sqFt / LandConverterEngine.SQ_FT_PER_SQ_METER
    val sqKadi = sqFt / LandConverterEngine.SQ_FT_PER_SQ_KADI
    val bighaCompound = LandConverterEngine.getBighaCompoundBreakdown(sqFt, uiState.laggiHaath)

    val unitOptions = listOf(LengthUnit.Kadi, LengthUnit.Feet, LengthUnit.Haath, LengthUnit.Meter, LengthUnit.Gaj)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Intro Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("plot_intro_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SquareFoot,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "भूखंड / खेत नापी कैलकुलेटर (अमीन टूल)" else "Land Plot Survey Calculator",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isHindi) "कड़ी (Link), फीट या हाथ में नापकर सीधा डिसमिल, कट्ठा, बीघा निकालें" else "Measure plot sides in Links, Feet, Haath and calculate exact area",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Plot Shape Selection Chips
        item {
            Column {
                Text(
                    text = if (isHindi) "खेत का आकार चुनें:" else "Select Field Shape:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(PlotShape.values()) { shape ->
                        val isSelected = shape == uiState.plotShape
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onPlotShapeSelected(shape) },
                            label = { Text(if (isHindi) shape.titleHi else shape.titleEn) },
                            modifier = Modifier.testTag("plot_shape_${shape.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Measurement Unit Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "नाप की इकाई (Input Unit):" else "Measurement Unit:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(unitOptions) { unit ->
                        val isSelected = unit.id == uiState.plotInputUnit.id
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.onPlotInputUnitSelected(unit) }
                                .testTag("plot_unit_${unit.id}")
                        ) {
                            Text(
                                text = if (isHindi) unit.symbolHi else unit.symbolEn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Dimension Input Fields
        item {
            val uSym = if (isHindi) uiState.plotInputUnit.symbolHi else uiState.plotInputUnit.symbolEn

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dimensions_input_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (isHindi) "भुजाओं की माप दर्ज करें ($uSym):" else "Enter Side Dimensions ($uSym):",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    when (uiState.plotShape) {
                        PlotShape.RECTANGLE -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = uiState.plotL1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l1 = it) },
                                    label = { Text(if (isHindi) "लंबाई ($uSym)" else "Length ($uSym)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("rect_l")
                                )
                                OutlinedTextField(
                                    value = uiState.plotW1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w1 = it) },
                                    label = { Text(if (isHindi) "चौड़ाई ($uSym)" else "Width ($uSym)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("rect_w")
                                )
                            }
                        }
                        PlotShape.IRREGULAR_4_SIDED_AVG -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = uiState.plotL1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l1 = it) },
                                    label = { Text(if (isHindi) "लंबाई 1 (उत्तर)" else "Length 1") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotL2,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l2 = it) },
                                    label = { Text(if (isHindi) "लंबाई 2 (दक्षिण)" else "Length 2") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = uiState.plotW1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w1 = it) },
                                    label = { Text(if (isHindi) "चौड़ाई 1 (पूरब)" else "Width 1") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotW2,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w2 = it) },
                                    label = { Text(if (isHindi) "चौड़ाई 2 (पश्चिम)" else "Width 2") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        PlotShape.IRREGULAR_4_SIDED_HERON -> {
                            Text(
                                text = if (isHindi) "त्रिभुजीकरण विधि (2 त्रिभुज + 1 विकर्ण):" else "Triangulation (2 Triangles + 1 Diagonal):",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = uiState.plotL1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l1 = it) },
                                    label = { Text(if (isHindi) "भुजा A" else "Side A") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotW1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w1 = it) },
                                    label = { Text(if (isHindi) "भुजा B" else "Side B") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = uiState.plotL2,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l2 = it) },
                                    label = { Text(if (isHindi) "भुजा C" else "Side C") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotW2,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w2 = it) },
                                    label = { Text(if (isHindi) "भुजा D" else "Side D") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            OutlinedTextField(
                                value = uiState.plotDiagonal,
                                onValueChange = { viewModel.onPlotDimensionsChanged(diag = it) },
                                label = { Text(if (isHindi) "विकर्ण नाप (Diagonal)" else "Diagonal (D)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        PlotShape.TRIANGLE -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = uiState.plotL1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(l1 = it) },
                                    label = { Text(if (isHindi) "भुजा A" else "Side A") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotW1,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(w1 = it) },
                                    label = { Text(if (isHindi) "भुजा B" else "Side B") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = uiState.plotSide3,
                                    onValueChange = { viewModel.onPlotDimensionsChanged(side3 = it) },
                                    label = { Text(if (isHindi) "भुजा C" else "Side C") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Calculated Result Card
        item {
            val strokeColor = MaterialTheme.colorScheme.primary
            val cardBg = MaterialTheme.colorScheme.surface

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("plot_results_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, strokeColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "नापी परिणाम (Calculated Area)" else "Plot Survey Results",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = {
                                val copyText = "${LandConverterEngine.formatNumber(dismil, 3)} Dismil\n${bighaCompound.toFormattedHi()}\n${LandConverterEngine.formatNumber(sqFt, 2)} Sq Ft"
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Plot Area", copyText))
                                Toast.makeText(context, if (isHindi) "परिणाम कॉपी हो गया!" else "Copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp).testTag("copy_plot_result")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Result",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Prominent Highlight: Dismil & Katha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isHindi) "कुल डिसमिल" else "Total Dismil",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = LandConverterEngine.formatNumber(dismil, 3),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.testTag("plot_dismil_val")
                                )
                                Text(
                                    text = if (isHindi) "डिसमिल (डेसमिल)" else "Dismil",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1.2f),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isHindi) "बीघा-कट्ठा-धुर (लग्गी अनुसार)" else "Bigha-Katha-Dhur",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = if (isHindi) bighaCompound.toFormattedHi() else bighaCompound.toFormattedEn(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.testTag("plot_bigha_val")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlotMetricBadge(
                            label = if (isHindi) "वर्ग फीट" else "Sq Ft",
                            value = LandConverterEngine.formatNumber(sqFt, 2),
                            modifier = Modifier.weight(1f)
                        )
                        PlotMetricBadge(
                            label = if (isHindi) "वर्ग कड़ी" else "Sq Kadi",
                            value = LandConverterEngine.formatNumber(sqKadi, 2),
                            modifier = Modifier.weight(1f)
                        )
                        PlotMetricBadge(
                            label = if (isHindi) "एकड़" else "Acre",
                            value = LandConverterEngine.formatNumber(acre, 4),
                            modifier = Modifier.weight(1f)
                        )
                        PlotMetricBadge(
                            label = if (isHindi) "वर्ग मीटर" else "Sq Meter",
                            value = LandConverterEngine.formatNumber(sqMeter, 2),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.applyPlotAreaToConverter()
                            onNavigateToAreaConverter()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("apply_plot_to_converter_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isHindi) "मुख्य कनवर्टर में सभी इकाइयों में देखें" else "View in All Units in Area Converter",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlotMetricBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
