package com.example

import com.example.model.*
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun test_dismil_to_sq_ft_and_sq_kadi() {
        // 1 Dismil = 435.6 sq ft
        val sqFt1Dismil = LandConverterEngine.toSquareFeet(1.0, LandAreaUnit.Dismil, 6.0)
        assertEquals(435.6, sqFt1Dismil, 0.001)

        // 1 Dismil = 1000 sq kadi (वर्ग कड़ी)
        val sqKadi = LandConverterEngine.fromSquareFeet(sqFt1Dismil, LandAreaUnit.SquareKadi, 6.0)
        assertEquals(1000.0, sqKadi, 0.001)

        // 1 Dismil = 48.4 sq gaj (वर्ग गज)
        val sqGaj = LandConverterEngine.fromSquareFeet(sqFt1Dismil, LandAreaUnit.SquareGaj, 6.0)
        assertEquals(48.4, sqGaj, 0.001)

        // 100 Dismil = 1 Acre
        val sqFt100Dismil = LandConverterEngine.toSquareFeet(100.0, LandAreaUnit.Dismil, 6.0)
        val acre = LandConverterEngine.fromSquareFeet(sqFt100Dismil, LandAreaUnit.Acre, 6.0)
        assertEquals(1.0, acre, 0.001)
    }

    @Test
    fun test_dhur_chain_and_laggi_calculations() {
        // Laggi = 6 Haath = 9 Feet
        // 1 Dhur = 9 * 9 = 81 sq ft
        val dhurSqFt6 = LandConverterEngine.getDhurSqFt(6.0)
        assertEquals(81.0, dhurSqFt6, 0.001)

        // 1 Katha = 20 Dhur = 1620 sq ft
        val kathaSqFt6 = LandConverterEngine.getKathaSqFt(6.0)
        assertEquals(1620.0, kathaSqFt6, 0.001)

        // 1 Bigha = 20 Katha = 400 Dhur = 32400 sq ft
        val bighaSqFt6 = LandConverterEngine.getBighaSqFt(6.0)
        assertEquals(32400.0, bighaSqFt6, 0.001)

        // 20 Furki = 1 Dhurki, 20 Dhurki = 1 Dhur
        val dhurkiSqFt = LandConverterEngine.getDhurkiSqFt(6.0)
        assertEquals(81.0 / 20.0, dhurkiSqFt, 0.001)
        val furkiSqFt = LandConverterEngine.getFurkiSqFt(6.0)
        assertEquals(81.0 / 400.0, furkiSqFt, 0.001)
    }

    @Test
    fun test_length_constants() {
        // 1 Jarib = 100 Kadi = 66 Feet
        val feetFromJarib = LengthConverterEngine.toFeet(1.0, LengthUnit.Jarib)
        assertEquals(66.0, feetFromJarib, 0.001)

        val kadiFromJarib = LengthConverterEngine.fromFeet(feetFromJarib, LengthUnit.Kadi)
        assertEquals(100.0, kadiFromJarib, 0.001)

        // 1 Haath = 1.5 Feet = 18 Inches
        val feetFromHaath = LengthConverterEngine.toFeet(1.0, LengthUnit.Haath)
        assertEquals(1.5, feetFromHaath, 0.001)

        val inchFromHaath = LengthConverterEngine.fromFeet(feetFromHaath, LengthUnit.Inch)
        assertEquals(18.0, inchFromHaath, 0.001)

        // 1 Bitta = 9 Inches = 0.75 Feet
        val feetFromBitta = LengthConverterEngine.toFeet(1.0, LengthUnit.Bitta)
        assertEquals(0.75, feetFromBitta, 0.001)

        // 1 Mutthi = 6 Inches = 0.5 Feet
        val feetFromMutthi = LengthConverterEngine.toFeet(1.0, LengthUnit.Mutthi)
        assertEquals(0.5, feetFromMutthi, 0.001)
    }
}
