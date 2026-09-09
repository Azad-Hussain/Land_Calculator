package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LandAreaUnit
import com.example.model.UnitCategory
import com.example.ui.theme.*
import com.example.viewmodel.ConversionResultItem

@Composable
fun UnitResultCard(
    isHindi: Boolean,
    item: ConversionResultItem,
    isSourceUnit: Boolean,
    onSetAsSource: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val unit = item.unit
    val unitName = if (isHindi) unit.nameHi else unit.nameEn
    val unitSymbol = if (isHindi) unit.symbolHi else unit.symbolEn
    val explanation = if (isHindi) unit.explanationHi else unit.explanationEn

    val (badgeBg, badgeText) = when (unit.category) {
        UnitCategory.BIHAR_REGIONAL -> Pair(BadgeBiharBg, BadgeBiharText)
        UnitCategory.STANDARD_INDIAN -> Pair(BadgeIndianBg, BadgeIndianText)
        UnitCategory.INTERNATIONAL -> Pair(BadgeIntlBg, BadgeIntlText)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("result_card_${unit.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSourceUnit) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSourceUnit) 2.dp else 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSourceUnit) MaterialTheme.colorScheme.primary else SlateBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Unit Title & Category Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = unitName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (unit.isLaggiDependent) {
                        Surface(
                            color = AmberLightContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (isHindi) "लग्गी आधारित" else "Laggi Based",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberDarkContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Surface(
                            color = badgeBg,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (isHindi) unit.category.titleHi else unit.category.titleEn,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeText,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Copy Button & Use as Source
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isSourceUnit) {
                        FilledTonalButton(
                            onClick = onSetAsSource,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(30.dp).testTag("set_source_${unit.id}"),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = if (isHindi) "स्रोत बनाएं" else "Use Source",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isHindi) "स्रोत इकाई" else "Current Source",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            val copyText = "${item.formattedValue} $unitSymbol"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Unit Value", copyText))
                            Toast.makeText(context, "$unitSymbol ${if (isHindi) "कॉपी हो गया!" else "copied!"}", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp).testTag("copy_${unit.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Value",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Value Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = item.formattedValue,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("value_${unit.id}")
                )
                Text(
                    text = unitSymbol,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Explanation / Formula hint
            if (explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}

