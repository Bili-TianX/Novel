package top.bilitianx

import kotlin.random.Random
import kotlin.random.nextLong

val sleepRange = 2000L..3000L

fun rest() {
    System.gc()
    Thread.sleep(Random.nextLong(sleepRange))
}
