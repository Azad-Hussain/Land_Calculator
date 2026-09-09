package com.example.model

import java.math.BigDecimal
import java.math.RoundingMode

enum class UnitCategory(val titleEn: String, val titleHi: String) {
    BIHAR_REGIONAL("Regional / Bihar", "बिहार व स्थानीय इकाइयाँ"),
    STANDARD_INDIAN("Standard Indian", "भारतीय मानक इकाइयाँ"),
    INTERNATIONAL("International", "अंतर्राष्ट्रीय इकाइयाँ")
}

data class DistrictLaggi(
    val nameEn: String,
    val nameHi: String,
    val haath: Double,
    val descriptionEn: String = "",
    val descriptionHi: String = ""
)

object BiharDistricts {
    val districts = listOf(
        DistrictLaggi("Standard Bihar", "मानक बिहार (6 हाथ)", 6.0, "Standard 6 Haath (9.0 ft)", "मानक 6 हाथ (9.0 फीट)"),
        DistrictLaggi("Patna", "पटना", 6.0, "6 Haath (9.0 ft)", "6 हाथ (9.0 फीट)"),
        DistrictLaggi("Gaya", "गया", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)"),
        DistrictLaggi("Muzaffarpur", "मुजफ्फरपुर", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Bhagalpur", "भागलपुर", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)"),
        DistrictLaggi("Darbhanga", "दरभंगा", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Purnia", "पूर्णिया", 7.0, "7.0 Haath (10.5 ft)", "7.0 हाथ (10.5 फीट)"),
        DistrictLaggi("Saran (Chapra)", "सारण (छपरा)", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Siwan", "सीवान", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Gopalganj", "गोपालगंज", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Vaishali (Hajipur)", "वैशाली (हाजीपुर)", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Samastipur", "समस्तीपुर", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Madhubani", "मधुबनी", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Bhojpur (Ara)", "भोजपुर (आरा)", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Rohtas (Sasaram)", "रोहतास (सासाराम)", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Buxar", "बक्सर", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Kaimur (Bhabua)", "कैमूर (भभुआ)", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Munger", "मुंगेर", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)"),
        DistrictLaggi("Begusarai", "बेगूसराय", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Saharsa", "सहरसा", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Katihar", "कटिहार", 7.0, "7.0 Haath (10.5 ft)", "7.0 हाथ (10.5 फीट)"),
        DistrictLaggi("Araria", "अररिया", 7.0, "7.0 Haath (10.5 ft)", "7.0 हाथ (10.5 फीट)"),
        DistrictLaggi("Kishanganj", "किशनगंज", 7.0, "7.0 Haath (10.5 ft)", "7.0 हाथ (10.5 फीट)"),
        DistrictLaggi("West Champaran (Bettiah)", "प. चंपारण (बेतिया)", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("East Champaran (Motihari)", "पू. चंपारण (मोतिहारी)", 6.5, "6.5 Haath (9.75 ft)", "6.5 हाथ (9.75 फीट)"),
        DistrictLaggi("Nalanda (Bihar Sharif)", "नालंदा (बिहारशरीफ)", 6.0, "6.0 Haath (9.0 ft)", "6.0 हाथ (9.0 फीट)"),
        DistrictLaggi("Nawada", "नवादा", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)"),
        DistrictLaggi("Jehanabad", "जहानाबाद", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)"),
        DistrictLaggi("Aurangabad", "औरंगाबाद", 5.5, "5.5 Haath (8.25 ft)", "5.5 हाथ (8.25 फीट)")
    )
}

sealed class LandAreaUnit(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val symbolEn: String,
    val symbolHi: String,
    val category: UnitCategory,
    val isLaggiDependent: Boolean = false,
    val explanationEn: String = "",
    val explanationHi: String = ""
) {
    // Bihar Regional Units
    object Dismil : LandAreaUnit(
        id = "dismil",
        nameEn = "Dismil (Decimal / Cent)",
        nameHi = "डिसमिल (डेसमिल / सेंट)",
        symbolEn = "Dismil",
        symbolHi = "डिसमिल",
        category = UnitCategory.BIHAR_REGIONAL,
        explanationEn = "1 Dismil = 435.6 sq ft = 1000 sq links = 48.4 sq yards",
        explanationHi = "1 डिसमिल = 435.6 वर्ग फीट = 1000 वर्ग कड़ी = 48.4 वर्ग गज"
    )

    object Bigha : LandAreaUnit(
        id = "bigha",
        nameEn = "Bigha",
        nameHi = "बीघा",
        symbolEn = "Bigha",
        symbolHi = "बीघा",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "1 Bigha = 20 Katha = 400 Dhur (based on Laggi)",
        explanationHi = "1 बीघा = 20 कट्ठा = 400 धुर (लग्गी अनुसार)"
    )

    object Katha : LandAreaUnit(
        id = "katha",
        nameEn = "Katha (Kattha)",
        nameHi = "कट्ठा (कठ्ठा)",
        symbolEn = "Katha",
        symbolHi = "कट्ठा",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "1 Katha = 20 Dhur = 400 Dhurki",
        explanationHi = "1 कट्ठा = 20 धुर = 400 धुरकी"
    )

    object Dhur : LandAreaUnit(
        id = "dhur",
        nameEn = "Dhur",
        nameHi = "धुर",
        symbolEn = "Dhur",
        symbolHi = "धुर",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "1 Dhur = 1 Square Laggi (वर्ग लग्गी) = 20 Dhurki",
        explanationHi = "1 धुर = 1 वर्ग लग्गी = 20 धुरकी"
    )

    object Dhurki : LandAreaUnit(
        id = "dhurki",
        nameEn = "Dhurki",
        nameHi = "धुरकी",
        symbolEn = "Dhurki",
        symbolHi = "धुरकी",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "20 Dhurki = 1 Dhur (1 Dhurki = 20 Furki)",
        explanationHi = "20 धुरकी = 1 धुर (1 धुरकी = 20 फुरकी)"
    )

    object Furki : LandAreaUnit(
        id = "furki",
        nameEn = "Furki",
        nameHi = "फुरकी",
        symbolEn = "Furki",
        symbolHi = "फुरकी",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "20 Furki = 1 Dhurki (400 Furki = 1 Dhur)",
        explanationHi = "20 फुरकी = 1 धुरकी (400 फुरकी = 1 धुर)"
    )

    object SquareLaggi : LandAreaUnit(
        id = "sq_laggi",
        nameEn = "Square Laggi (वर्ग लग्गी)",
        nameHi = "वर्ग लग्गी",
        symbolEn = "Sq Laggi",
        symbolHi = "वर्ग लग्गी",
        category = UnitCategory.BIHAR_REGIONAL,
        isLaggiDependent = true,
        explanationEn = "1 Sq Laggi = 1 Dhur = (Laggi in Haath × 1.5 ft)²",
        explanationHi = "1 वर्ग लग्गी = 1 धुर = (लग्गी हाथ × 1.5 फीट)²"
    )

    // Standard Indian Units
    object SquareKadi : LandAreaUnit(
        id = "sq_kadi",
        nameEn = "Square Kadi (वर्ग कड़ी / Sq Link)",
        nameHi = "वर्ग कड़ी (कड़ी²)",
        symbolEn = "Sq Kadi",
        symbolHi = "वर्ग कड़ी",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Sq Kadi = 0.4356 sq ft (1000 Sq Kadi = 1 Dismil)",
        explanationHi = "1 वर्ग कड़ी = 0.4356 वर्ग फीट (1000 वर्ग कड़ी = 1 डिसमिल)"
    )

    object SquareGaj : LandAreaUnit(
        id = "sq_gaj",
        nameEn = "Square Gaj (वर्ग गज / Sq Yard)",
        nameHi = "वर्ग गज (गज²)",
        symbolEn = "Sq Gaj / Yd",
        symbolHi = "वर्ग गज",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Sq Gaj = 9 sq ft (48.4 Sq Gaj = 1 Dismil)",
        explanationHi = "1 वर्ग गज = 9 वर्ग फीट (48.4 वर्ग गज = 1 डिसमिल)"
    )

    object Guntha : LandAreaUnit(
        id = "guntha",
        nameEn = "Guntha (गुंठा)",
        nameHi = "गुंठा",
        symbolEn = "Guntha",
        symbolHi = "गुंठा",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Guntha = 1,089 sq ft = 1/40 Acre = 2.5 Dismil",
        explanationHi = "1 गुंठा = 1,089 वर्ग फीट = 1/40 एकड़ = 2.5 डिसमिल"
    )

    object Ground : LandAreaUnit(
        id = "ground",
        nameEn = "Ground (ग्राउंड)",
        nameHi = "ग्राउंड",
        symbolEn = "Ground",
        symbolHi = "ग्राउंड",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Ground = 2,400 sq ft ≈ 5.51 Dismil",
        explanationHi = "1 ग्राउंड = 2,400 वर्ग फीट ≈ 5.51 डिसमिल"
    )

    object Kanal : LandAreaUnit(
        id = "kanal",
        nameEn = "Kanal (कनाल)",
        nameHi = "कनाल",
        symbolEn = "Kanal",
        symbolHi = "कनाल",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Kanal = 5,445 sq ft = 12.5 Dismil (8 Kanal = 1 Acre)",
        explanationHi = "1 कनाल = 5,445 वर्ग फीट = 12.5 डिसमिल (8 कनाल = 1 एकड़)"
    )

    object Marla : LandAreaUnit(
        id = "marla",
        nameEn = "Marla (मरला)",
        nameHi = "मरला",
        symbolEn = "Marla",
        symbolHi = "मरला",
        category = UnitCategory.STANDARD_INDIAN,
        explanationEn = "1 Marla = 272.25 sq ft = 0.625 Dismil (20 Marla = 1 Kanal)",
        explanationHi = "1 मरला = 272.25 वर्ग फीट = 0.625 डिसमिल (20 मरला = 1 कनाल)"
    )

    // International Standard Units
    object Acre : LandAreaUnit(
        id = "acre",
        nameEn = "Acre (एकड़)",
        nameHi = "एकड़",
        symbolEn = "Acre",
        symbolHi = "एकड़",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Acre = 100 Dismil = 43,560 sq ft = 4,046.86 sq m",
        explanationHi = "1 एकड़ = 100 डिसमिल = 43,560 वर्ग फीट = 4,046.86 वर्ग मीटर"
    )

    object Hectare : LandAreaUnit(
        id = "hectare",
        nameEn = "Hectare (हेक्टेयर)",
        nameHi = "हेक्टेयर",
        symbolEn = "Ha",
        symbolHi = "हेक्टेयर",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Hectare = 10,000 sq m = 247.1 Dismil ≈ 2.471 Acres",
        explanationHi = "1 हेक्टेयर = 10,000 वर्ग मीटर = 247.1 डिसमिल ≈ 2.471 एकड़"
    )

    object SquareFeet : LandAreaUnit(
        id = "sq_ft",
        nameEn = "Square Feet (वर्ग फीट)",
        nameHi = "वर्ग फीट (फीट²)",
        symbolEn = "Sq Ft",
        symbolHi = "वर्ग फीट",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "Standard square footage unit (435.6 sq ft = 1 Dismil)",
        explanationHi = "मानक वर्ग फीट इकाई (435.6 वर्ग फीट = 1 डिसमिल)"
    )

    object SquareMeter : LandAreaUnit(
        id = "sq_m",
        nameEn = "Square Meter (वर्ग मीटर)",
        nameHi = "वर्ग मीटर (मी²)",
        symbolEn = "Sq M",
        symbolHi = "वर्ग मीटर",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Sq Meter = 10.7639 sq ft (40.47 Sq M = 1 Dismil)",
        explanationHi = "1 वर्ग मीटर = 10.7639 वर्ग फीट (40.47 वर्ग मीटर = 1 डिसमिल)"
    )

    object SquareInch : LandAreaUnit(
        id = "sq_in",
        nameEn = "Square Inch (वर्ग इंच)",
        nameHi = "वर्ग इंच (इंच²)",
        symbolEn = "Sq In",
        symbolHi = "वर्ग इंच",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Sq Ft = 144 Square Inches",
        explanationHi = "1 वर्ग फीट = 144 वर्ग इंच"
    )

    object SquareKilometer : LandAreaUnit(
        id = "sq_km",
        nameEn = "Square Kilometer (वर्ग किमी)",
        nameHi = "वर्ग किलोमीटर",
        symbolEn = "Sq Km",
        symbolHi = "वर्ग किमी",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Sq Km = 1,000,000 Sq Meters = 247.1 Acres",
        explanationHi = "1 वर्ग किमी = 10,00,000 वर्ग मीटर = 247.1 एकड़"
    )

    object SquareMile : LandAreaUnit(
        id = "sq_mi",
        nameEn = "Square Mile (वर्ग मील)",
        nameHi = "वर्ग मील",
        symbolEn = "Sq Mi",
        symbolHi = "वर्ग मील",
        category = UnitCategory.INTERNATIONAL,
        explanationEn = "1 Sq Mile = 640 Acres = 27,878,400 sq ft",
        explanationHi = "1 वर्ग मील = 640 एकड़ = 2,78,78,400 वर्ग फीट"
    )

    companion object {
        val allUnits: List<LandAreaUnit> by lazy {
            listOf(
                // Regional / Bihar
                Dismil,
                Bigha,
                Katha,
                Dhur,
                Dhurki,
                Furki,
                SquareLaggi,
                // Standard Indian
                SquareGaj,
                SquareKadi,
                Guntha,
                Ground,
                Kanal,
                Marla,
                // International
                Acre,
                Hectare,
                SquareFeet,
                SquareMeter,
                SquareInch,
                SquareKilometer,
                SquareMile
            )
        }

        fun fromId(id: String): LandAreaUnit {
            return allUnits.firstOrNull { it.id == id } ?: Dismil
        }
    }
}

/**
 * Core Converter Engine for Land Area
 */
object LandConverterEngine {
    // 1 Dismil = 435.6 sq ft
    const val SQ_FT_PER_DISMIL = 435.6
    // 1 Acre = 43,560 sq ft
    const val SQ_FT_PER_ACRE = 43560.0
    // 1 sq meter = 10.763910416709722 sq ft
    const val SQ_FT_PER_SQ_METER = 10.763910416709722
    // 1 Hectare = 10,000 sq m = 107639.10416709722 sq ft
    const val SQ_FT_PER_HECTARE = 107639.10416709722
    // 1 sq Gaj (sq yard) = 9 sq ft
    const val SQ_FT_PER_SQ_GAJ = 9.0
    // 1 sq Kadi (sq link) = (0.66 ft)^2 = 0.4356 sq ft
    const val SQ_FT_PER_SQ_KADI = 0.4356

    /**
     * Calculate single Dhur area in sq ft based on Laggi in Haath
     * 1 Haath = 1.5 ft (18 in)
     * 1 Laggi (ft) = haath * 1.5
     * 1 Dhur = (1 Laggi)^2 = (haath * 1.5)^2
     */
    fun getDhurSqFt(laggiHaath: Double): Double {
        val laggiFt = laggiHaath * 1.5
        return laggiFt * laggiFt
    }

    fun getKathaSqFt(laggiHaath: Double): Double = getDhurSqFt(laggiHaath) * 20.0
    fun getBighaSqFt(laggiHaath: Double): Double = getKathaSqFt(laggiHaath) * 20.0
    fun getDhurkiSqFt(laggiHaath: Double): Double = getDhurSqFt(laggiHaath) / 20.0
    fun getFurkiSqFt(laggiHaath: Double): Double = getDhurkiSqFt(laggiHaath) / 20.0

    /**
     * Converts any unit value into canonical Square Feet (sq ft)
     */
    fun toSquareFeet(value: Double, unit: LandAreaUnit, laggiHaath: Double): Double {
        if (value <= 0.0) return 0.0
        return when (unit) {
            LandAreaUnit.SquareFeet -> value
            LandAreaUnit.Dismil -> value * SQ_FT_PER_DISMIL
            LandAreaUnit.Acre -> value * SQ_FT_PER_ACRE
            LandAreaUnit.Hectare -> value * SQ_FT_PER_HECTARE
            LandAreaUnit.SquareMeter -> value * SQ_FT_PER_SQ_METER
            LandAreaUnit.SquareGaj -> value * SQ_FT_PER_SQ_GAJ
            LandAreaUnit.SquareKadi -> value * SQ_FT_PER_SQ_KADI
            LandAreaUnit.Bigha -> value * getBighaSqFt(laggiHaath)
            LandAreaUnit.Katha -> value * getKathaSqFt(laggiHaath)
            LandAreaUnit.Dhur -> value * getDhurSqFt(laggiHaath)
            LandAreaUnit.SquareLaggi -> value * getDhurSqFt(laggiHaath)
            LandAreaUnit.Dhurki -> value * getDhurkiSqFt(laggiHaath)
            LandAreaUnit.Furki -> value * getFurkiSqFt(laggiHaath)
            LandAreaUnit.Guntha -> value * 1089.0
            LandAreaUnit.Ground -> value * 2400.0
            LandAreaUnit.Kanal -> value * 5445.0
            LandAreaUnit.Marla -> value * 272.25
            LandAreaUnit.SquareInch -> value / 144.0
            LandAreaUnit.SquareKilometer -> value * (SQ_FT_PER_SQ_METER * 1_000_000.0)
            LandAreaUnit.SquareMile -> value * (SQ_FT_PER_ACRE * 640.0)
        }
    }

    /**
     * Converts canonical Square Feet (sq ft) into target unit
     */
    fun fromSquareFeet(sqFt: Double, targetUnit: LandAreaUnit, laggiHaath: Double): Double {
        if (sqFt <= 0.0) return 0.0
        return when (targetUnit) {
            LandAreaUnit.SquareFeet -> sqFt
            LandAreaUnit.Dismil -> sqFt / SQ_FT_PER_DISMIL
            LandAreaUnit.Acre -> sqFt / SQ_FT_PER_ACRE
            LandAreaUnit.Hectare -> sqFt / SQ_FT_PER_HECTARE
            LandAreaUnit.SquareMeter -> sqFt / SQ_FT_PER_SQ_METER
            LandAreaUnit.SquareGaj -> sqFt / SQ_FT_PER_SQ_GAJ
            LandAreaUnit.SquareKadi -> sqFt / SQ_FT_PER_SQ_KADI
            LandAreaUnit.Bigha -> sqFt / getBighaSqFt(laggiHaath)
            LandAreaUnit.Katha -> sqFt / getKathaSqFt(laggiHaath)
            LandAreaUnit.Dhur -> sqFt / getDhurSqFt(laggiHaath)
            LandAreaUnit.SquareLaggi -> sqFt / getDhurSqFt(laggiHaath)
            LandAreaUnit.Dhurki -> sqFt / getDhurkiSqFt(laggiHaath)
            LandAreaUnit.Furki -> sqFt / getFurkiSqFt(laggiHaath)
            LandAreaUnit.Guntha -> sqFt / 1089.0
            LandAreaUnit.Ground -> sqFt / 2400.0
            LandAreaUnit.Kanal -> sqFt / 5445.0
            LandAreaUnit.Marla -> sqFt / 272.25
            LandAreaUnit.SquareInch -> sqFt * 144.0
            LandAreaUnit.SquareKilometer -> sqFt / (SQ_FT_PER_SQ_METER * 1_000_000.0)
            LandAreaUnit.SquareMile -> sqFt / (SQ_FT_PER_ACRE * 640.0)
        }
    }

    /**
     * Compute full compound breakdown in Bigha - Katha - Dhur - Dhurki - Furki
     */
    fun getBighaCompoundBreakdown(sqFt: Double, laggiHaath: Double): BighaCompoundResult {
        if (sqFt <= 0.0) return BighaCompoundResult(0, 0, 0, 0, 0.0, 0.0)
        val furkiSqFt = getFurkiSqFt(laggiHaath)
        val totalFurki = sqFt / furkiSqFt

        val bighaVal = (totalFurki / 8000.0).toLong()
        var rem = totalFurki - (bighaVal * 8000.0)

        val kathaVal = (rem / 400.0).toLong()
        rem -= (kathaVal * 400.0)

        val dhurVal = (rem / 20.0).toLong()
        rem -= (dhurVal * 20.0)

        val dhurkiVal = rem.toLong()
        val furkiRemaining = rem - dhurkiVal

        val totalDhur = sqFt / getDhurSqFt(laggiHaath)
        return BighaCompoundResult(
            bigha = bighaVal,
            katha = kathaVal,
            dhur = dhurVal,
            dhurki = dhurkiVal,
            furki = furkiRemaining,
            totalDhur = totalDhur
        )
    }

    /**
     * Compute full compound breakdown in Acre - Dismil - Sq Ft
     */
    fun getAcreCompoundBreakdown(sqFt: Double): AcreCompoundResult {
        if (sqFt <= 0.0) return AcreCompoundResult(0, 0, 0.0, 0.0)
        val acreVal = (sqFt / SQ_FT_PER_ACRE).toLong()
        val remAcre = sqFt - (acreVal * SQ_FT_PER_ACRE)

        val dismilVal = (remAcre / SQ_FT_PER_DISMIL).toLong()
        val remSqFt = remAcre - (dismilVal * SQ_FT_PER_DISMIL)
        val remSqKadi = remSqFt / SQ_FT_PER_SQ_KADI

        return AcreCompoundResult(
            acre = acreVal,
            dismil = dismilVal,
            sqFtRemainder = remSqFt,
            sqKadiRemainder = remSqKadi
        )
    }

    fun formatNumber(value: Double, decimals: Int = 4): String {
        if (value == 0.0) return "0"
        if (value.isNaN() || value.isInfinite()) return "0"
        
        // If it's effectively a whole number
        if (Math.abs(value - Math.round(value)) < 1e-6) {
            return Math.round(value).toString()
        }
        
        return try {
            val bd = BigDecimal(value).setScale(decimals, RoundingMode.HALF_UP).stripTrailingZeros()
            bd.toPlainString()
        } catch (e: Exception) {
            String.format("%.4f", value).trimEnd('0').trimEnd('.')
        }
    }
}

data class BighaCompoundResult(
    val bigha: Long,
    val katha: Long,
    val dhur: Long,
    val dhurki: Long,
    val furki: Double,
    val totalDhur: Double
) {
    fun toFormattedEn(): String {
        val furkiStr = if (furki > 0.001) " ${LandConverterEngine.formatNumber(furki, 2)} Furki" else ""
        return "$bigha Bigha - $katha Katha - $dhur Dhur - $dhurki Dhurki$furkiStr"
    }

    fun toFormattedHi(): String {
        val furkiStr = if (furki > 0.001) " ${LandConverterEngine.formatNumber(furki, 2)} फुरकी" else ""
        return "$bigha बीघा - $katha कट्ठा - $dhur धुर - $dhurki धुरकी$furkiStr"
    }
}

data class AcreCompoundResult(
    val acre: Long,
    val dismil: Long,
    val sqFtRemainder: Double,
    val sqKadiRemainder: Double
) {
    fun toFormattedEn(): String {
        val remStr = if (sqFtRemainder > 0.01) " (${LandConverterEngine.formatNumber(sqFtRemainder, 2)} Sq Ft)" else ""
        return "$acre Acre - $dismil Dismil$remStr"
    }

    fun toFormattedHi(): String {
        val remStr = if (sqFtRemainder > 0.01) " (${LandConverterEngine.formatNumber(sqFtRemainder, 2)} वर्ग फीट)" else ""
        return "$acre एकड़ - $dismil डिसमिल$remStr"
    }
}
