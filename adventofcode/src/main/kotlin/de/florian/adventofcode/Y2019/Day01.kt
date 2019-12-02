package de.florian.adventofcode.Y2019

import kotlin.math.floor
import kotlin.streams.asSequence

val input = """109067
75007
66030
93682
83818
108891
139958
129246
80272
119897
112804
69495
95884
85402
148361
75986
120063
127683
146962
76907
61414
98452
134330
53858
82662
143258
82801
60279
131782
105989
102464
96563
71172
113731
90645
94830
133247
110149
54792
134863
125919
145490
69836
108808
87954
148957
110182
126668
148024
96915
117727
147378
75967
91915
60130
85331
66800
103419
72627
72687
61606
113160
107082
110793
61589
105005
73952
65705
117243
140944
117091
113482
91379
148185
113853
119822
78179
85407
119886
109230
68783
63914
51101
93549
53361
127984
106315
54997
138941
81075
120272
120307
98414
115245
105649
89793
88421
121104
97084
56928"""

fun main() {
    fun String.splittedDoubleSequence() =  this.split("\n").stream().asSequence().map{it.toDouble()}
    val formula: (Double) -> Double = { floor(it / 3.0) - 2 }
    fun recFormula(it: Double) : (Double) = if (formula(it) < 0) 0.0 else formula(it) + recFormula(formula(it))

    val part1 = input.splittedDoubleSequence()
        .map(formula)
        .sum().toInt()
    println("Part 1: $part1")

    val part2 = input.splittedDoubleSequence()
        .map{recFormula(it)}
        .sum().toInt()
    println("Part 2: $part2")

    //Part 1: 3330521
    //Part 2: 4992931
}