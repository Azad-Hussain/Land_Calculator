package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BiharDistricts
import com.example.viewmodel.LandConverterUiState
import com.example.viewmodel.LandConverterViewModel

@Composable
fun ReferenceGuideScreen(
    viewModel: LandConverterViewModel,
    uiState: LandConverterUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi = uiState.isHindi

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Banner - Bihar Survey 2026
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ref_banner"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "जमीन मापन गाइड: नए बिहार सर्वे 2026" else "Land Measurement Guide: Bihar Survey 2026",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (isHindi) "डिजिटल अमीन नियम, लग्गी व डिसमिल गणित" else "Digital Amin Rules, Laggi & Dismil Math",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isHindi)
                            "वर्तमान बिहार विशेष सर्वेक्षण 2026 के अंतर्गत जमीन का सही रकबा (बीघा, कट्ठा, धुर, डिसमिल) निकालने के लिए पारंपरिक लग्गी व वैज्ञानिक पद्धति का सटीक संयोजन अनिवार्य है।"
                        else
                            "Under the ongoing Bihar Land Survey 2026, accurate land area calculation requires understanding traditional Laggi dynamics alongside standard revenue metrics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Section 0: 1 धुर निकालने का फॉर्मूला (Laggi Math)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ref_bihar_survey_math"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isHindi) "🎯 1 धुर निकालने का प्रामाणिक फॉर्मूला" else "🎯 Formula for 1 Dhur (Laggi Based)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (isHindi) "फार्मूला: (लग्गी x 1.5)² = वर्ग फीट (1 धुर)" else "Formula: (Laggi in Haath x 1.5)² = Sq Ft (1 Dhur)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi)
                                    "• 6 हाथ की लग्गी: (6 x 1.5) = 9 फीट ➔ 9 x 9 = 81 वर्ग फीट (1 धुर = 81 sq ft)\n• 5.5 हाथ की लग्गी: (5.5 x 1.5) = 8.25 फीट ➔ 8.25 x 8.25 = 68.0625 वर्ग फीट"
                                else
                                    "• 6 Haath Laggi: (6 x 1.5) = 9 ft ➔ 9 x 9 = 81 sq ft (1 Dhur = 81 sq ft)\n• 5.5 Haath Laggi: (5.5 x 1.5) = 8.25 ft ➔ 8.25 x 8.25 = 68.0625 sq ft",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi)
                            "• लग्गी आमतौर पर 4 हाथ से 9 हाथ तक होती है (सबसे प्रचलित 5.5 या 6 हाथ)।\n• 1 डिसमिल = 435.6 वर्ग फीट (यह मान लग्गी बदलने पर भी स्थिर रहता है)।\n• 100 डिसमिल = 1 एकड़ (43,560 वर्ग फीट)।"
                        else
                            "• Laggi ranges between 4 to 9 Haath (most common 5.5 or 6 Haath).\n• 1 Dismil = 435.6 Sq Ft (constant, does not change with Laggi).\n• 100 Dismil = 1 Acre (43,560 sq ft).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section 1: प्रमुख क्षेत्र स्थिरांक
        item {
            ReferenceSectionCard(
                title = if (isHindi) "१. प्रमुख क्षेत्र स्थिरांक (Area Constants)" else "1. Major Land Area Constants",
                testTag = "ref_area_constants",
                items = listOf(
                    "1 डिसमिल (Dismil)" to "1000 वर्ग कड़ी (Sq Links)",
                    "1 डिसमिल (Dismil)" to "435.6 वर्ग फीट (Sq Ft)",
                    "1 डिसमिल (Dismil)" to "48.4 वर्ग गज (Sq Yards / Gaj)",
                    "1 डिसमिल (Dismil)" to "40.47 वर्ग मीटर (Sq Meters)",
                    "100 डिसमिल" to "1 एकड़ (Acre) = 43,560 वर्ग फीट",
                    "247.1 डिसमिल" to "1 हेक्टेयर (Hectare) = 10,000 वर्ग मीटर"
                )
            )
        }

        // Section 2: धुर श्रृंखला
        item {
            ReferenceSectionCard(
                title = if (isHindi) "२. धुर श्रृंखला (Dhur Chain Hierarchy)" else "2. Dhur Chain (Local Units)",
                testTag = "ref_dhur_chain",
                items = listOf(
                    "20 फुरकी (Furki)" to "1 धुरकी (Dhurki)",
                    "20 धुरकी (Dhurki)" to "1 धुर (Dhur)",
                    "20 धुर (Dhur)" to "1 कट्ठा (Katha / Kattha)",
                    "20 कट्ठा (Katha)" to "1 बीघा (Bigha = 400 धुर)",
                    "1 धुर (Dhur)" to "1 वर्ग लग्गी (1 Square Laggi)"
                )
            )
        }

        // Section 3: लंबाई स्थिरांक
        item {
            ReferenceSectionCard(
                title = if (isHindi) "३. लंबाई स्थिरांक (Length Constants)" else "3. Survey Length Constants",
                testTag = "ref_length_constants",
                items = listOf(
                    "1 जरीब (Gunter Chain)" to "100 कड़ी = 66 फीट = 22 गज = 20.1168 m",
                    "1 कड़ी (Link)" to "7.92 इंच = 0.66 फीट = 0.201168 m",
                    "1 हाथ (Haath / Cubit)" to "18 इंच = 1.5 फीट = 2.27 कड़ी",
                    "1 बित्ता (Span)" to "9 इंच = 0.75 फीट = 1.136 कड़ी",
                    "1 मुट्ठी (Fist)" to "6 इंच = 0.5 फीट = 0.757 कड़ी",
                    "1 गज (Yard)" to "3 फीट = 36 इंच = 2 हाथ",
                    "1 मीटर (Meter)" to "3.28084 फीट = 39.37 इंच"
                )
            )
        }

        // Section 4: बिहार जिले अनुसार लग्गी
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ref_district_laggi"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isHindi) "४. लग्गी की लंबाई (जिले अनुसार)" else "4. Laggi Length by Bihar District",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isHindi) "लग्गी की लंबाई जिले के अनुसार भिन्न होती है (1 धुर = लग्गी²):" else "Laggi varies by region (1 Dhur = Laggi²):",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    BiharDistricts.districts.take(12).forEach { dist ->
                        val dhurSqFt = (dist.haath * 1.5) * (dist.haath * 1.5)
                        val kathaSqFt = dhurSqFt * 20.0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isHindi) dist.nameHi else dist.nameEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${dist.haath} ${if (isHindi) "हाथ" else "Haath"} (${dhurSqFt} sq ft/धुर)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    }
                }
            }
        }

        // Section 5: Recent Conversions History
        if (uiState.historyList.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isHindi) "हाल के रूपांतरण (History)" else "Recent Calculations",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = { viewModel.clearHistory() },
                        modifier = Modifier.size(32.dp).testTag("clear_history_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear History",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            items(uiState.historyList) { hist ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = hist.inputFormatted,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi) hist.bighaCompoundHi else hist.bighaCompoundEn,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isHindi) hist.summaryHi else hist.summaryEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReferenceSectionCard(
    title: String,
    testTag: String,
    items: List<Pair<String, String>>
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = {
                        val text = items.joinToString("\n") { "${it.first} = ${it.second}" }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Reference", text))
                        Toast.makeText(context, "कॉपी हो गया!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Section",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            items.forEach { (k, v) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = k,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = v,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            }
        }
    }
}
