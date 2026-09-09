package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AcreCompoundResult
import com.example.model.BighaCompoundResult
import com.example.model.LandConverterEngine
import com.example.ui.theme.*

@Composable
fun CompoundAreaCard(
    isHindi: Boolean,
    bighaCompound: BighaCompoundResult,
    acreCompound: AcreCompoundResult,
    totalSqFt: Double,
    laggiHaath: Double,
    onSaveHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val bighaFormatted = if (isHindi) bighaCompound.toFormattedHi() else bighaCompound.toFormattedEn()
    val acreFormatted = if (isHindi) acreCompound.toFormattedHi() else acreCompound.toFormattedEn()
    val sqFtFormatted = "${LandConverterEngine.formatNumber(totalSqFt, 2)} ${if (isHindi) "वर्ग फीट" else "Sq Ft"}"
    val dismilVal = totalSqFt / LandConverterEngine.SQ_FT_PER_DISMIL
    val dismilFormatted = "${LandConverterEngine.formatNumber(dismilVal, 3)} ${if (isHindi) "डिसमिल" else "Dismil"}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("compound_area_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Terrain,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "पारंपरिक भू-मान विखंडन (रकबा)" else "Land Area Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isHindi) "लग्गी: $laggiHaath हाथ | $sqFtFormatted" else "Laggi: $laggiHaath Haath | $sqFtFormatted",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Copy & Action Buttons
                Row {
                    IconButton(
                        onClick = {
                            val copyText = "$bighaFormatted\n$acreFormatted\n$dismilFormatted\n$sqFtFormatted"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Land Area", copyText))
                            Toast.makeText(context, if (isHindi) "कॉपी हो गया!" else "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                            onSaveHistory()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("copy_compound_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Summary",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary Executive Gradient Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                NavyPrimary,
                                Color(0xFF1E40AF) // Royal Blue
                            )
                        )
                    )
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "धुर श्रृंखला विखंडन (Bihar Standard)" else "Dhur Chain Breakdown",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isHindi) "20 के अनुपात में" else "1:20 Base",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Segmented visual pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ExecutiveCompoundPill(
                            label = if (isHindi) "बीघा" else "Bigha",
                            value = "${bighaCompound.bigha}",
                            modifier = Modifier.weight(1f)
                        )
                        ExecutiveCompoundPill(
                            label = if (isHindi) "कट्ठा" else "Katha",
                            value = "${bighaCompound.katha}",
                            modifier = Modifier.weight(1f)
                        )
                        ExecutiveCompoundPill(
                            label = if (isHindi) "धुर" else "Dhur",
                            value = "${bighaCompound.dhur}",
                            modifier = Modifier.weight(1f)
                        )
                        ExecutiveCompoundPill(
                            label = if (isHindi) "धुरकी" else "Dhurki",
                            value = "${bighaCompound.dhurki}",
                            modifier = Modifier.weight(1f)
                        )
                        if (bighaCompound.furki > 0.001) {
                            ExecutiveCompoundPill(
                                label = if (isHindi) "फुरकी" else "Furki",
                                value = LandConverterEngine.formatNumber(bighaCompound.furki, 1),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = bighaFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Acre - Dismil Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isHindi) "एकड़ - डिसमिल:" else "Acre - Dismil:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = acreFormatted,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = if (isHindi) "कुल डिसमिल:" else "Total Dismil:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dismilFormatted,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExecutiveCompoundPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

