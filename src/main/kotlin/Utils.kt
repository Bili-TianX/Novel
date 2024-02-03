package top.bilitianx

import kotlin.random.Random
import kotlin.random.nextLong

val sleepRange = 3000L..4000L

fun rest() {
    System.gc()
    Thread.sleep(Random.nextLong(sleepRange))
}
