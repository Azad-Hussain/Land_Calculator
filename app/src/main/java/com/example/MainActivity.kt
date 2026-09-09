package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.LandConverterEngine
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LandConverterViewModel

enum class NavigationTab(
    val titleEn: String,
    val titleHi: String,
    val icon: ImageVector,
    val tag: String
) {
    AREA("Area", "क्षेत्रफल", Icons.Default.Terrain, "tab_area"),
    LENGTH("Length", "लंबाई", Icons.Default.Straighten, "tab_length"),
    PLOT_SURVEY("Plot Survey", "खेत नापी", Icons.Default.SquareFoot, "tab_plot"),
    PARTITION("Partition", "बंटवारा", Icons.Default.Groups, "tab_partition"),
    REFERENCE("Reference", "स्थिरांक", Icons.AutoMirrored.Filled.MenuBook, "tab_reference")
}

class MainActivity : ComponentActivity() {

    private val viewModel: LandConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MyApplicationTheme(themeMode = uiState.themeMode) {
                MainLandConverterApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLandConverterApp(viewModel: LandConverterViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(NavigationTab.AREA) }
    val isHindi = uiState.isHindi

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isHindi) "भू-मापक एवं क्षेत्रफल परिवर्तक" else "Land Area Measurement Converter",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                        val laggiFt = LandConverterEngine.formatNumber(uiState.laggiHaath * 1.5, 2)
                        Text(
                            text = if (isHindi) "बिहार व भारतीय मानक | लग्गी: ${uiState.laggiHaath} हाथ ($laggiFt ft)" else "Bihar & National Standards | Laggi: ${uiState.laggiHaath} Haath",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Theme Switcher Button
                    IconButton(
                        onClick = { viewModel.cycleThemeMode() },
                        modifier = Modifier.testTag("theme_toggle_btn")
                    ) {
                        val (themeIcon, themeDesc) = when (uiState.themeMode) {
                            com.example.viewmodel.ThemeMode.SYSTEM -> Pair(Icons.Default.BrightnessAuto, if (isHindi) "सिस्टम थीम" else "System Theme")
                            com.example.viewmodel.ThemeMode.LIGHT -> Pair(Icons.Default.LightMode, if (isHindi) "लाइट मोड" else "Light Mode")
                            com.example.viewmodel.ThemeMode.DARK -> Pair(Icons.Default.DarkMode, if (isHindi) "डार्क मोड" else "Dark Mode")
                        }
                        Icon(
                            imageVector = themeIcon,
                            contentDescription = themeDesc,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Language Switch Pill
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("language_toggle_btn"),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        onClick = { viewModel.toggleLanguage() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isHindi) "English" else "हिन्दी",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = if (isHindi) tab.titleHi else tab.titleEn
                            )
                        },
                        label = {
                            Text(
                                text = if (isHindi) tab.titleHi else tab.titleEn,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp)
            ) {
                when (selectedTab) {
                    NavigationTab.AREA -> {
                        AreaConverterScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                    NavigationTab.LENGTH -> {
                        LengthConverterScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                    NavigationTab.PLOT_SURVEY -> {
                        PlotMeasurementScreen(
                            viewModel = viewModel,
                            uiState = uiState,
                            onNavigateToAreaConverter = {
                                selectedTab = NavigationTab.AREA
                            }
                        )
                    }
                    NavigationTab.PARTITION -> {
                        LandPartitionScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                    NavigationTab.REFERENCE -> {
                        ReferenceGuideScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                }
            }
        }
    }
}
