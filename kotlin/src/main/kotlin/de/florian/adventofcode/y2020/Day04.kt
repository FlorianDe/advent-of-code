package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() {
    Day04().exec()
}

class Day04 : AoCDay(Year.YEAR_2020, Day.DAY_04) {

    companion object {
        const val IN = "in";
        const val CM = "cm";
        val BLANK_LINE_REGEX = Regex("\\n\\n")
        val KEY_VALUE_REGEX = Regex("(.+):(.+)")
        val HEIGHT_REGEX = Regex("(\\d+)($CM|$IN)")
        val SIX_CHARACTERS_REGEX = Regex("#\\w{6}")
        val VALID_EYE_COLOR_REGEX = Regex("(amb|blu|brn|gry|grn|hzl|oth)")
        val NINE_DIGIT_NUMBER_REGEX = Regex("\\d{9}")
    }

    enum class PassportDataField(val value: String, val required: Boolean, val apply: (value: String) -> Boolean = {true}) {
        BYR("byr", true, {(1920..2002).contains(it.toIntOrNull())}), //(Birth Year)
        IYR("iyr", true, {(2010..2020).contains(it.toIntOrNull())}), // (Issue Year)
        EYR("eyr", true, {(2020..2030).contains(it.toIntOrNull())}), // (Expiration Year)
        HGT("hgt", true, {
            HEIGHT_REGEX.find(it)?.let { match ->
                val (size, unit) = match.destructured
                when(unit){
                    IN -> (59..76).contains(size.toIntOrNull())
                    CM -> (150..193).contains(size.toIntOrNull())
                    else -> false
                }
            } ?: false
        }), // (Height)
        HCL("hcl", true, {SIX_CHARACTERS_REGEX.matches(it)}), // (Hair Color)
        ECL("ecl", true, {VALID_EYE_COLOR_REGEX.matches(it)}), // (Eye Color)
        PID("pid", true, {NINE_DIGIT_NUMBER_REGEX.matches(it)}), // (Passport ID)
        CID("cid", false), // (Country ID)
    }

    private fun convertInput(input: String): List<Map<String, String>> = input.split(BLANK_LINE_REGEX).map { entries ->
            val keyValuesString = entries.trim().replace("\n", " ")
            keyValuesString.split(" ").map {
                val (key, value) = KEY_VALUE_REGEX.find(it)!!.destructured
                key to value
            }.toMap()
    }

    private val passports = convertInput(Inputs_2020.DAY_04)

    override fun part1(): String {
        return passports.count { fields ->
            PassportDataField.values()
                    .filter { it.required }
                    .all { req -> fields.containsKey(req.value) }
        }.toString()
    }

    override fun part2(): String {
        return passports.count { fields ->
            PassportDataField.values()
                    .filter { it.required }
                    .all { fieldRule -> fields[fieldRule.value]?.let(fieldRule.apply) ?: false }
        }.toString()
    }
}