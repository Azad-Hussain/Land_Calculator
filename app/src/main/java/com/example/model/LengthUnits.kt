package com.example.model

import java.math.BigDecimal
import java.math.RoundingMode

sealed class LengthUnit(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val symbolEn: String,
    val symbolHi: String,
    val descriptionEn: String,
    val descriptionHi: String
) {
    object Jarib : LengthUnit(
        id = "jarib",
        nameEn = "Jarib (Chain)",
        nameHi = "जरीब (Gunter Chain)",
        symbolEn = "Jarib",
        symbolHi = "जरीब",
        descriptionEn = "1 Jarib = 100 Kadi = 66 Feet = 22 Yards = 20.1168 m",
        descriptionHi = "1 जरीब = 100 कड़ी = 66 फीट = 22 गज = 20.1168 मीटर"
    )

    object Kadi : LengthUnit(
        id = "kadi",
        nameEn = "Kadi (Link)",
        nameHi = "कड़ी (लिंक)",
        symbolEn = "Kadi",
        symbolHi = "कड़ी",
        descriptionEn = "1 Kadi = 0.66 Feet = 7.92 Inches = 0.201168 m",
        descriptionHi = "1 कड़ी = 0.66 फीट = 7.92 इंच = 0.201168 मीटर"
    )

    object Haath : LengthUnit(
        id = "haath",
        nameEn = "Haath (Cubit)",
        nameHi = "हाथ",
        symbolEn = "Haath",
        symbolHi = "हाथ",
        descriptionEn = "1 Haath = 1.5 Feet = 18 Inches = 2.2727 Kadi",
        descriptionHi = "1 हाथ = 1.5 फीट = 18 इंच = 2.2727 कड़ी"
    )

    object Bitta : LengthUnit(
        id = "bitta",
        nameEn = "Bitta (Span)",
        nameHi = "बित्ता (बालिश्त)",
        symbolEn = "Bitta",
        symbolHi = "बित्ता",
        descriptionEn = "1 Bitta = 0.75 Feet = 9 Inches = 1.1363 Kadi",
        descriptionHi = "1 बित्ता = 0.75 फीट = 9 इंच = 1.1363 कड़ी"
    )

    object Mutthi : LengthUnit(
        id = "mutthi",
        nameEn = "Mutthi (Fist)",
        nameHi = "मुट्ठी",
        symbolEn = "Mutthi",
        symbolHi = "मुट्ठी",
        descriptionEn = "1 Mutthi = 0.5 Feet = 6 Inches = 0.7575 Kadi",
        descriptionHi = "1 मुट्ठी = 0.5 फीट = 6 इंच = 0.7575 कड़ी"
    )

    object Feet : LengthUnit(
        id = "feet",
        nameEn = "Feet (Foot)",
        nameHi = "फीट (फुट)",
        symbolEn = "Ft",
        symbolHi = "फीट",
        descriptionEn = "1 Foot = 12 Inches = 0.3048 Meters",
        descriptionHi = "1 फुट = 12 इंच = 0.3048 मीटर"
    )

    object Inch : LengthUnit(
        id = "inch",
        nameEn = "Inches",
        nameHi = "इंच",
        symbolEn = "In",
        symbolHi = "इंच",
        descriptionEn = "1 Inch = 2.54 cm",
        descriptionHi = "1 इंच = 2.54 सेमी"
    )

    object Gaj : LengthUnit(
        id = "gaj",
        nameEn = "Gaj (Yard)",
        nameHi = "गज (यार्ड)",
        symbolEn = "Gaj / Yd",
        symbolHi = "गज",
        descriptionEn = "1 Gaj = 3 Feet = 36 Inches = 0.9144 m",
        descriptionHi = "1 गज = 3 फीट = 36 इंच = 0.9144 मीटर"
    )

    object Meter : LengthUnit(
        id = "meter",
        nameEn = "Meter",
        nameHi = "मीटर",
        symbolEn = "m",
        symbolHi = "मी",
        descriptionEn = "1 Meter = 3.28084 Feet = 39.3701 Inches",
        descriptionHi = "1 मीटर = 3.28084 फीट = 39.3701 इंच"
    )

    object Centimeter : LengthUnit(
        id = "cm",
        nameEn = "Centimeter",
        nameHi = "सेंटीमीटर (सेमी)",
        symbolEn = "cm",
        symbolHi = "सेमी",
        descriptionEn = "1 cm = 0.3937 Inches",
        descriptionHi = "1 सेमी = 0.3937 इंच"
    )

    object Kilometer : LengthUnit(
        id = "km",
        nameEn = "Kilometer",
        nameHi = "किलोमीटर (किमी)",
        symbolEn = "km",
        symbolHi = "किमी",
        descriptionEn = "1 km = 1000 Meters = 3280.84 Feet",
        descriptionHi = "1 किमी = 1000 मीटर = 3280.84 फीट"
    )

    companion object {
        val allUnits: List<LengthUnit> by lazy {
            listOf(
                Jarib,
                Kadi,
                Haath,
                Bitta,
                Mutthi,
                Feet,
                Inch,
                Gaj,
                Meter,
                Centimeter,
                Kilometer
            )
        }

        fun fromId(id: String): LengthUnit = allUnits.firstOrNull { it.id == id } ?: Feet
    }
}

object LengthConverterEngine {
    // Canonical unit is Feet
    const val FT_PER_JARIB = 66.0
    const val FT_PER_KADI = 0.66
    const val FT_PER_HAATH = 1.5
    const val FT_PER_BITTA = 0.75
    const val FT_PER_MUTTHI = 0.5
    const val FT_PER_INCH = 1.0 / 12.0
    const val FT_PER_GAJ = 3.0
    const val FT_PER_METER = 3.280839895013123
    const val FT_PER_CM = 0.03280839895013123
    const val FT_PER_KM = 3280.839895013123

    fun toFeet(value: Double, unit: LengthUnit): Double {
        if (value <= 0.0) return 0.0
        return when (unit) {
            LengthUnit.Jarib -> value * FT_PER_JARIB
            LengthUnit.Kadi -> value * FT_PER_KADI
            LengthUnit.Haath -> value * FT_PER_HAATH
            LengthUnit.Bitta -> value * FT_PER_BITTA
            LengthUnit.Mutthi -> value * FT_PER_MUTTHI
            LengthUnit.Feet -> value
            LengthUnit.Inch -> value * FT_PER_INCH
            LengthUnit.Gaj -> value * FT_PER_GAJ
            LengthUnit.Meter -> value * FT_PER_METER
            LengthUnit.Centimeter -> value * FT_PER_CM
            LengthUnit.Kilometer -> value * FT_PER_KM
        }
    }

    fun fromFeet(feet: Double, targetUnit: LengthUnit): Double {
        if (feet <= 0.0) return 0.0
        return when (targetUnit) {
            LengthUnit.Jarib -> feet / FT_PER_JARIB
            LengthUnit.Kadi -> feet / FT_PER_KADI
            LengthUnit.Haath -> feet / FT_PER_HAATH
            LengthUnit.Bitta -> feet / FT_PER_BITTA
            LengthUnit.Mutthi -> feet / FT_PER_MUTTHI
            LengthUnit.Feet -> feet
            LengthUnit.Inch -> feet / FT_PER_INCH
            LengthUnit.Gaj -> feet / FT_PER_GAJ
            LengthUnit.Meter -> feet / FT_PER_METER
            LengthUnit.Centimeter -> feet / FT_PER_CM
            LengthUnit.Kilometer -> feet / FT_PER_KM
        }
    }

    fun formatNumber(value: Double, decimals: Int = 4): String {
        if (value == 0.0) return "0"
        if (value.isNaN() || value.isInfinite()) return "0"
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
