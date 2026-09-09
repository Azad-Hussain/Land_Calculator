package com.example.ui.components

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.ui.window.Dialog
import com.example.model.BiharDistricts
import com.example.model.DistrictLaggi
import com.example.model.LandConverterEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaggiConfigDialog(
    isHindi: Boolean,
    currentHaath: Double,
    selectedDistrict: DistrictLaggi,
    isCustom: Boolean,
    customInput: String,
    onDistrictSelected: (DistrictLaggi) -> Unit,
    onHaathSelected: (Double) -> Unit,
    onCustomInputChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val quickHaathOptions = listOf(4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0)
    var selectedTab by remember { mutableStateOf(if (isCustom) 2 else 0) } // 0: Quick Haath, 1: Bihar District, 2: Custom

    val laggiFt = currentHaath * 1.5
    val laggiInches = currentHaath * 18.0
    val laggiKadi = laggiFt / 0.66
    val dhurSqFt = LandConverterEngine.getDhurSqFt(currentHaath)
    val kathaSqFt = LandConverterEngine.getKathaSqFt(currentHaath)
    val kathaDismil = kathaSqFt / LandConverterEngine.SQ_FT_PER_DISMIL
    val bighaSqFt = LandConverterEngine.getBighaSqFt(currentHaath)
    val bighaDismil = bighaSqFt / LandConverterEngine.SQ_FT_PER_DISMIL

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("laggi_config_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Straighten,
                            contentDescription = "Laggi Settings",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "लग्गी (काठा) सेटिंग" else "Laggi Length Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp).testTag("close_laggi_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Text(
                    text = if (isHindi) "जिले या क्षेत्र अनुसार लग्गी का मान चुनें:" else "Select Laggi size by Haath or Bihar District:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Tabs: Quick Haath / District / Custom
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                if (isHindi) "हाथ माप" else "Haath (Quick)",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                if (isHindi) "बिहार जिले" else "Districts",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Text(
                                if (isHindi) "कस्टम हाथ" else "Custom",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Content based on tab
                when (selectedTab) {
                    0 -> {
                        // Quick Haath selector chips
                        Text(
                            text = if (isHindi) "सामान्य लग्गी मान (हाथ में):" else "Standard Haath Sizes:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(quickHaathOptions) { haath ->
                                val isSelected = !isCustom && currentHaath == haath
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onHaathSelected(haath) },
                                    label = {
                                        Text(
                                            text = if (isHindi) "$haath हाथ" else "$haath Haath",
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null,
                                    modifier = Modifier.testTag("haath_chip_$haath")
                                )
                            }
                        }
                    }
                    1 -> {
                        // Bihar District list
                        Text(
                            text = if (isHindi) "जिले अनुसार मानक लग्गी:" else "District Standard Laggi:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            items(BiharDistricts.districts) { district ->
                                val isSelected = !isCustom && selectedDistrict.nameEn == district.nameEn && currentHaath == district.haath
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                                        )
                                        .clickable { onDistrictSelected(district) }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isHindi) district.nameHi else district.nameEn,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Surface(
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isHindi) "${district.haath} हाथ" else "${district.haath} Haath",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        // Custom Haath Input
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = if (isHindi) "कस्टम लग्गी (हाथ में दर्ज करें):" else "Enter custom Laggi length in Haath:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = customInput,
                                onValueChange = onCustomInputChanged,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                label = { Text(if (isHindi) "लग्गी (हाथ)" else "Laggi (Haath)") },
                                trailingIcon = {
                                    Text(
                                        text = if (isHindi) "हाथ" else "Haath",
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_laggi_input")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Realtime Laggi conversion summary preview
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isHindi) "वर्तमान लग्गी मान:" else "Active Laggi Specs:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${currentHaath} ${if (isHindi) "हाथ" else "Haath"} (${LandConverterEngine.formatNumber(laggiFt, 2)} ft)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• 1 ${if (isHindi) "लग्गी" else "Laggi"} = ${LandConverterEngine.formatNumber(laggiFt, 2)} ft = ${LandConverterEngine.formatNumber(laggiInches, 1)} in = ${LandConverterEngine.formatNumber(laggiKadi, 2)} ${if (isHindi) "कड़ी" else "Kadi"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "• 1 ${if (isHindi) "धुर (1 वर्ग लग्गी)" else "Dhur (1 Sq Laggi)"} = ${LandConverterEngine.formatNumber(dhurSqFt, 2)} sq ft",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "• 1 ${if (isHindi) "कट्ठा (20 धुर)" else "Katha (20 Dhur)"} = ${LandConverterEngine.formatNumber(kathaSqFt, 2)} sq ft = ${LandConverterEngine.formatNumber(kathaDismil, 3)} ${if (isHindi) "डिसमिल" else "Dismil"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "• 1 ${if (isHindi) "बीघा (20 कट्ठा)" else "Bigha (20 Katha)"} = ${LandConverterEngine.formatNumber(bighaSqFt, 2)} sq ft = ${LandConverterEngine.formatNumber(bighaDismil, 3)} ${if (isHindi) "डिसमिल" else "Dismil"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_laggi_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isHindi) "लागू करें (Apply)" else "Apply Selection",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
