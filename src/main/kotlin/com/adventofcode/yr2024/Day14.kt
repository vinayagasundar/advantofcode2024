package com.adventofcode.yr2024

import com.adventofcode.*
import java.io.BufferedWriter
import java.io.FileWriter
import kotlin.math.abs


private const val MAX_ROW = 103
private const val MAX_COLUMN = 101

private fun displayMap(position: Array<Point>) {
    for (i in 0 until MAX_ROW) {
        for (j in 0 until MAX_COLUMN) {
            val value = if (position.contains(Point(i, j))) {
                "#"
            } else {
                "."
            }
            print(value)
        }
        println()
    }
}

fun main() {
    val regex = Regex("-*\\d+")

    val input = readFileFromResources("day14.txt").orEmpty()
        .lines()

    val p = Array(input.size) {
        Point(0, 0)
    }
    val v = Array(input.size) {
        Point(0, 0)
    }

    input.forEachIndexed { i, line ->
        val (px, py, vx, vy) = regex.findAll(line).map { it.value.toInt() }.toList()
        p[i] = Point(py, px)
        v[i] = Point(vy, vx)
    }

    println("Part 1: ${solvePartOne(p.clone(), v.clone())}")
    println("Part 2: ${solvePartTwo(p.clone(), v.clone())}")
}

private fun moveRobot(p: Array<Point>, v: Array<Point>) {
    for (i in p.indices) {
        val (px, py) = p[i]
        val (vx, vy) = v[i]
        val x = abs((px + vx + MAX_ROW) % MAX_ROW)
        val y = abs((py + vy + MAX_COLUMN) % MAX_COLUMN)
        p[i] = Point(x, y)
    }
}

private fun solvePartOne(position: Array<Point>, velocity: Array<Point>): Int {
    for (i in 1..100) {
        moveRobot(position, velocity)
    }

    val midRow = MAX_ROW / 2
    val midColumn = MAX_COLUMN / 2

    var topLeft = 0
    var topRight = 0
    var bottomLeft = 0
    var bottomRight = 0

    for ((r, c) in position) {
        when {
            r < midRow && c < midColumn -> topLeft += 1
            r < midRow && c > midColumn -> topRight += 1
            r > midRow && c < midColumn -> bottomLeft += 1
            r > midRow && c > midColumn -> bottomRight += 1
        }
    }

    displayMap(position)

    return topLeft * topRight * bottomLeft * bottomRight
}

private fun solvePartTwo(position: Array<Point>, velocity: Array<Point>) {
    val writer = BufferedWriter(FileWriter("output.txt"))

    writer.use {
        var c = 1
        while (true) {
            moveRobot(position, velocity)

            writer.newLine()
            writer.write("Counter : $c")
            writer.newLine()
            println("$c")
            for (i in 0 until MAX_ROW) {
                for (j in 0 until MAX_COLUMN) {
                    if (position.contains(Point(i, j))) {
                        writer.write("#")
                    } else {
                        writer.write(".")
                    }
                }
                writer.newLine()
            }
            c++
        }
    }
}
