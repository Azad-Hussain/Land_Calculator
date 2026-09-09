package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.flow.*

enum class PlotShape(val titleEn: String, val titleHi: String) {
    RECTANGLE("Rectangle (L × W)", "आयताकार (लंबाई × चौड़ाई)"),
    IRREGULAR_4_SIDED_AVG("4-Sided Irregular (Average Method)", "चार भुजा वाला खेत (औसत विधि)"),
    IRREGULAR_4_SIDED_HERON("4-Sided (Precise Diagonal / Heron)", "चार भुजा + 1 विकर्ण (अमीन सटीक विधि)"),
    TRIANGLE("Triangle (3 Sides / Heron)", "त्रिभुजाकार खेत (3 भुजाएँ)")
}

data class ConversionResultItem(
    val unit: LandAreaUnit,
    val value: Double,
    val formattedValue: String
)

data class LengthResultItem(
    val unit: LengthUnit,
    val value: Double,
    val formattedValue: String
)

data class ShareholderResult(
    val id: Int,
    val nameEn: String,
    val nameHi: String,
    val shareRatio: Double,
    val percentage: Double,
    val areaSqFt: Double,
    val areaDismil: Double,
    val bighaCompound: BighaCompoundResult
)

enum class ThemeMode(val titleEn: String, val titleHi: String) {
    SYSTEM("System Default", "सिस्टम अनुसार"),
    LIGHT("Light Mode", "लाइट मोड"),
    DARK("Dark Mode", "डार्क मोड")
}

data class HistoryItem(
    val timestamp: Long = System.currentTimeMillis(),
    val inputFormatted: String,
    val summaryEn: String,
    val summaryHi: String,
    val bighaCompoundEn: String,
    val bighaCompoundHi: String
)

data class LandConverterUiState(
    val isHindi: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Area Converter
    val areaInput: String = "10",
    val selectedSourceUnit: LandAreaUnit = LandAreaUnit.Dismil,
    val selectedDistrict: DistrictLaggi = BiharDistricts.districts[0],
    val laggiHaath: Double = 6.0,
    val isCustomLaggi: Boolean = false,
    val customLaggiInput: String = "6.0",
    val currentSqFt: Double = 4356.0,
    val areaResults: List<ConversionResultItem> = emptyList(),
    val bighaCompound: BighaCompoundResult = BighaCompoundResult(0, 0, 0, 0, 0.0, 0.0),
    val acreCompound: AcreCompoundResult = AcreCompoundResult(0, 0, 0.0, 0.0),

    // Length Converter
    val lengthInput: String = "1",
    val selectedSourceLengthUnit: LengthUnit = LengthUnit.Jarib,
    val lengthResults: List<LengthResultItem> = emptyList(),

    // Plot Calculator
    val plotShape: PlotShape = PlotShape.RECTANGLE,
    val plotInputUnit: LengthUnit = LengthUnit.Feet,
    val plotL1: String = "100",
    val plotL2: String = "100",
    val plotW1: String = "50",
    val plotW2: String = "50",
    val plotDiagonal: String = "111.8",
    val plotSide3: String = "60",
    val calculatedPlotSqFt: Double = 5000.0,

    // Partition Calculator
    val partitionAreaInput: String = "1",
    val partitionAreaUnit: LandAreaUnit = LandAreaUnit.Bigha,
    val shareholderCount: Int = 3,
    val shareholderRatios: List<Double> = listOf(1.0, 1.0, 1.0),
    val partitionResults: List<ShareholderResult> = emptyList(),

    // History
    val historyList: List<HistoryItem> = emptyList()
)

class LandConverterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LandConverterUiState())
    val uiState: StateFlow<LandConverterUiState> = _uiState.asStateFlow()

    init {
        recalculateArea()
        recalculateLength()
        recalculatePlot()
        recalculatePartition()
    }

    fun toggleLanguage() {
        _uiState.update { it.copy(isHindi = !it.isHindi) }
    }

    fun setLanguage(isHindi: Boolean) {
        _uiState.update { it.copy(isHindi = isHindi) }
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun cycleThemeMode() {
        _uiState.update { state ->
            val nextMode = when (state.themeMode) {
                ThemeMode.SYSTEM -> ThemeMode.LIGHT
                ThemeMode.LIGHT -> ThemeMode.DARK
                ThemeMode.DARK -> ThemeMode.SYSTEM
            }
            state.copy(themeMode = nextMode)
        }
    }

    // AREA CONVERTER ACTIONS
    fun onAreaInputChanged(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(areaInput = sanitized) }
        recalculateArea()
    }

    fun onSourceUnitSelected(unit: LandAreaUnit) {
        _uiState.update { it.copy(selectedSourceUnit = unit) }
        recalculateArea()
    }

    fun onDistrictSelected(district: DistrictLaggi) {
        _uiState.update {
            it.copy(
                selectedDistrict = district,
                laggiHaath = district.haath,
                isCustomLaggi = false,
                customLaggiInput = district.haath.toString()
            )
        }
        recalculateArea()
        recalculatePlot()
        recalculatePartition()
    }

    fun onLaggiHaathSelected(haath: Double) {
        val matchingDistrict = BiharDistricts.districts.firstOrNull { it.haath == haath } ?: _uiState.value.selectedDistrict
        _uiState.update {
            it.copy(
                laggiHaath = haath,
                selectedDistrict = matchingDistrict,
                isCustomLaggi = false,
                customLaggiInput = haath.toString()
            )
        }
        recalculateArea()
        recalculatePlot()
        recalculatePartition()
    }

    fun onCustomLaggiChanged(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        val haathVal = sanitized.toDoubleOrNull() ?: 6.0
        val boundedHaath = if (haathVal in 2.0..20.0) haathVal else 6.0
        _uiState.update {
            it.copy(
                customLaggiInput = sanitized,
                laggiHaath = boundedHaath,
                isCustomLaggi = true
            )
        }
        recalculateArea()
        recalculatePlot()
        recalculatePartition()
    }

    private fun recalculateArea() {
        val state = _uiState.value
        val numericVal = state.areaInput.toDoubleOrNull() ?: 0.0
        val sqFt = LandConverterEngine.toSquareFeet(numericVal, state.selectedSourceUnit, state.laggiHaath)

        val results = LandAreaUnit.allUnits.map { unit ->
            val converted = LandConverterEngine.fromSquareFeet(sqFt, unit, state.laggiHaath)
            ConversionResultItem(
                unit = unit,
                value = converted,
                formattedValue = LandConverterEngine.formatNumber(converted, if (unit == LandAreaUnit.Hectare || unit == LandAreaUnit.SquareKilometer || unit == LandAreaUnit.SquareMile) 6 else 4)
            )
        }

        val bighaBreakdown = LandConverterEngine.getBighaCompoundBreakdown(sqFt, state.laggiHaath)
        val acreBreakdown = LandConverterEngine.getAcreCompoundBreakdown(sqFt)

        _uiState.update {
            it.copy(
                currentSqFt = sqFt,
                areaResults = results,
                bighaCompound = bighaBreakdown,
                acreCompound = acreBreakdown
            )
        }
    }

    fun saveCurrentToHistory() {
        val state = _uiState.value
        val inputStr = "${state.areaInput} ${if (state.isHindi) state.selectedSourceUnit.nameHi else state.selectedSourceUnit.nameEn}"
        val summaryEn = "${LandConverterEngine.formatNumber(state.currentSqFt, 2)} Sq Ft | ${state.acreCompound.toFormattedEn()}"
        val summaryHi = "${LandConverterEngine.formatNumber(state.currentSqFt, 2)} वर्ग फीट | ${state.acreCompound.toFormattedHi()}"

        val newItem = HistoryItem(
            inputFormatted = inputStr,
            summaryEn = summaryEn,
            summaryHi = summaryHi,
            bighaCompoundEn = state.bighaCompound.toFormattedEn(),
            bighaCompoundHi = state.bighaCompound.toFormattedHi()
        )

        _uiState.update {
            val updated = listOf(newItem) + it.historyList.take(29)
            it.copy(historyList = updated)
        }
    }

    fun clearHistory() {
        _uiState.update { it.copy(historyList = emptyList()) }
    }

    // LENGTH CONVERTER ACTIONS
    fun onLengthInputChanged(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(lengthInput = sanitized) }
        recalculateLength()
    }

    fun onSourceLengthUnitSelected(unit: LengthUnit) {
        _uiState.update { it.copy(selectedSourceLengthUnit = unit) }
        recalculateLength()
    }

    private fun recalculateLength() {
        val state = _uiState.value
        val numericVal = state.lengthInput.toDoubleOrNull() ?: 0.0
        val feet = LengthConverterEngine.toFeet(numericVal, state.selectedSourceLengthUnit)

        val results = LengthUnit.allUnits.map { unit ->
            val converted = LengthConverterEngine.fromFeet(feet, unit)
            LengthResultItem(
                unit = unit,
                value = converted,
                formattedValue = LengthConverterEngine.formatNumber(converted, if (unit == LengthUnit.Kilometer || unit == LengthUnit.Meter) 4 else 4)
            )
        }

        _uiState.update { it.copy(lengthResults = results) }
    }

    // PLOT CALCULATOR ACTIONS
    fun onPlotShapeSelected(shape: PlotShape) {
        _uiState.update { it.copy(plotShape = shape) }
        recalculatePlot()
    }

    fun onPlotInputUnitSelected(unit: LengthUnit) {
        _uiState.update { it.copy(plotInputUnit = unit) }
        recalculatePlot()
    }

    fun onPlotDimensionsChanged(
        l1: String? = null,
        l2: String? = null,
        w1: String? = null,
        w2: String? = null,
        diag: String? = null,
        side3: String? = null
    ) {
        _uiState.update {
            it.copy(
                plotL1 = l1?.filter { c -> c.isDigit() || c == '.' } ?: it.plotL1,
                plotL2 = l2?.filter { c -> c.isDigit() || c == '.' } ?: it.plotL2,
                plotW1 = w1?.filter { c -> c.isDigit() || c == '.' } ?: it.plotW1,
                plotW2 = w2?.filter { c -> c.isDigit() || c == '.' } ?: it.plotW2,
                plotDiagonal = diag?.filter { c -> c.isDigit() || c == '.' } ?: it.plotDiagonal,
                plotSide3 = side3?.filter { c -> c.isDigit() || c == '.' } ?: it.plotSide3
            )
        }
        recalculatePlot()
    }

    private fun recalculatePlot() {
        val state = _uiState.value
        val unit = state.plotInputUnit
        val l1Ft = LengthConverterEngine.toFeet(state.plotL1.toDoubleOrNull() ?: 0.0, unit)
        val l2Ft = LengthConverterEngine.toFeet(state.plotL2.toDoubleOrNull() ?: 0.0, unit)
        val w1Ft = LengthConverterEngine.toFeet(state.plotW1.toDoubleOrNull() ?: 0.0, unit)
        val w2Ft = LengthConverterEngine.toFeet(state.plotW2.toDoubleOrNull() ?: 0.0, unit)
        val diagFt = LengthConverterEngine.toFeet(state.plotDiagonal.toDoubleOrNull() ?: 0.0, unit)
        val s3Ft = LengthConverterEngine.toFeet(state.plotSide3.toDoubleOrNull() ?: 0.0, unit)

        var totalSqFt = 0.0

        when (state.plotShape) {
            PlotShape.RECTANGLE -> {
                totalSqFt = l1Ft * w1Ft
            }
            PlotShape.IRREGULAR_4_SIDED_AVG -> {
                val avgL = (l1Ft + l2Ft) / 2.0
                val avgW = (w1Ft + w2Ft) / 2.0
                totalSqFt = avgL * avgW
            }
            PlotShape.IRREGULAR_4_SIDED_HERON -> {
                // Triangle 1: l1, w1, diag
                val area1 = heronArea(l1Ft, w1Ft, diagFt)
                // Triangle 2: l2, w2, diag
                val area2 = heronArea(l2Ft, w2Ft, diagFt)
                totalSqFt = area1 + area2
            }
            PlotShape.TRIANGLE -> {
                totalSqFt = heronArea(l1Ft, w1Ft, s3Ft)
            }
        }

        _uiState.update { it.copy(calculatedPlotSqFt = if (totalSqFt > 0.0) totalSqFt else 0.0) }
    }

    private fun heronArea(a: Double, b: Double, c: Double): Double {
        if (a <= 0.0 || b <= 0.0 || c <= 0.0) return 0.0
        if (a + b <= c || a + c <= b || b + c <= a) return 0.0
        val s = (a + b + c) / 2.0
        val valUnderSqrt = s * (s - a) * (s - b) * (s - c)
        return if (valUnderSqrt > 0.0) Math.sqrt(valUnderSqrt) else 0.0
    }

    fun applyPlotAreaToConverter() {
        val sqFt = _uiState.value.calculatedPlotSqFt
        if (sqFt <= 0.0) return
        val dismil = sqFt / LandConverterEngine.SQ_FT_PER_DISMIL
        _uiState.update {
            it.copy(
                areaInput = LandConverterEngine.formatNumber(dismil, 4),
                selectedSourceUnit = LandAreaUnit.Dismil
            )
        }
        recalculateArea()
    }

    // PARTITION CALCULATOR ACTIONS
    fun onPartitionAreaInputChanged(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(partitionAreaInput = sanitized) }
        recalculatePartition()
    }

    fun onPartitionUnitSelected(unit: LandAreaUnit) {
        _uiState.update { it.copy(partitionAreaUnit = unit) }
        recalculatePartition()
    }

    fun onShareholderCountChanged(count: Int) {
        val bounded = count.coerceIn(2, 10)
        val currentRatios = _uiState.value.shareholderRatios
        val newRatios = (0 until bounded).map { idx ->
            if (idx < currentRatios.size) currentRatios[idx] else 1.0
        }
        _uiState.update {
            it.copy(
                shareholderCount = bounded,
                shareholderRatios = newRatios
            )
        }
        recalculatePartition()
    }

    fun onShareRatioChanged(index: Int, ratioInput: String) {
        val sanitized = ratioInput.filter { it.isDigit() || it == '.' }
        val r = sanitized.toDoubleOrNull() ?: 1.0
        val updated = _uiState.value.shareholderRatios.toMutableList()
        if (index in updated.indices) {
            updated[index] = if (r > 0.0) r else 1.0
        }
        _uiState.update { it.copy(shareholderRatios = updated) }
        recalculatePartition()
    }

    private fun recalculatePartition() {
        val state = _uiState.value
        val numericVal = state.partitionAreaInput.toDoubleOrNull() ?: 0.0
        val totalSqFt = LandConverterEngine.toSquareFeet(numericVal, state.partitionAreaUnit, state.laggiHaath)

        val count = state.shareholderCount
        val ratios = state.shareholderRatios.take(count)
        val sumRatios = ratios.sum().let { if (it > 0.0) it else 1.0 }

        val results = ratios.mapIndexed { index, ratio ->
            val fraction = ratio / sumRatios
            val shareSqFt = totalSqFt * fraction
            val shareDismil = shareSqFt / LandConverterEngine.SQ_FT_PER_DISMIL
            val compound = LandConverterEngine.getBighaCompoundBreakdown(shareSqFt, state.laggiHaath)
            ShareholderResult(
                id = index + 1,
                nameEn = "Shareholder ${index + 1}",
                nameHi = "हिस्सेदार ${index + 1}",
                shareRatio = ratio,
                percentage = fraction * 100.0,
                areaSqFt = shareSqFt,
                areaDismil = shareDismil,
                bighaCompound = compound
            )
        }

        _uiState.update { it.copy(partitionResults = results) }
    }
}
