import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.BigPoint
import java.math.BigDecimal
import java.math.BigDecimal.*
import java.math.RoundingMode

fun main() {
    Day12().exec()
}

typealias Point = BigPoint

class Day12 : AoCDay() {
    companion object {
        val SAIL_ACTION_REGEX = Regex("([NSEWLRF])(\\d+)")
    }

    data class SailAction(val action: Action, val value: BigDecimal)

    enum class Action(val execute: (value: BigDecimal, dir: Point) -> Point) {
        N(execute = { scalar, _ -> Point(ZERO, ONE) * scalar }),
        S(execute = { scalar, _ -> Point(ZERO, -ONE) * scalar }),
        E(execute = { scalar, _ -> Point(ONE, ZERO) * scalar }),
        W(execute = { scalar, _ -> Point(-ONE, ZERO) * scalar }),
        L(execute = { value, dir -> dir.rotate(value.toDouble(), clockWise = false) }),
        R(execute = { value, dir -> dir.rotate(value.toDouble(), clockWise = true) }),
        F(execute = { value, dir -> dir * value }),
    }

    private val sailActions: List<SailAction> = Inputs_2020.DAY_12.split("\n").map {
        val (action, value) = SAIL_ACTION_REGEX.find(it)!!.destructured
        SailAction(Action.valueOf(action), value.toBigDecimal())
    }

    override fun part1(): String {
        var curPos = Point.zero()
        var curDir = Point(ONE, ZERO)
        sailActions.forEach {
            when (it.action) {
                Action.L, Action.R -> curDir = it.action.execute(it.value, curDir)
                else -> curPos += it.action.execute(it.value, curDir)
            }
        }
        return curPos.manhattanDistance().setScale(0, RoundingMode.HALF_EVEN).toString()
    }

    override fun part2(): String {
        var curPos = Point.zero()
        var waypoint = Point(valueOf(10L), ONE)
        sailActions.forEach {
            when (it.action) {
                Action.L, Action.R -> waypoint = it.action.execute(it.value, waypoint)
                Action.F -> curPos += it.action.execute(it.value, waypoint)
                else -> waypoint += it.action.execute(it.value, waypoint)
            }
        }
        return curPos.manhattanDistance().setScale(0, RoundingMode.HALF_EVEN).toString()
    }
}
