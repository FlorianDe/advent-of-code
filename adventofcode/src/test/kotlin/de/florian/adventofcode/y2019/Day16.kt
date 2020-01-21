package de.florian.adventofcode.y2019

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day16Test {
    @Test
    fun test_fft_samples(){
        assertEquals("01029498", Day16("12345678").fft(times = 4))

        assertEquals("24176176", Day16("80871224585914546619083218645595").fft())
        assertEquals("73745418", Day16("19617804207202209144916044189917").fft())
        assertEquals("52432133", Day16("69317163492948606335995924319873").fft())

        assertEquals("84462026", Day16("03036732577212944063491565474664").fft2())
        assertEquals("78725270", Day16("02935109699940807407585447034323").fft2())
        assertEquals("53553731", Day16("03081770884921959731165446850517").fft2())
    }
}