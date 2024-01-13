package top.bilitianx

import kotlin.random.Random
import kotlin.random.nextLong

val sleepRange = 1000L..2000L

fun rest() {
    System.gc()
    Thread.sleep(Random.nextLong(sleepRange))
}
