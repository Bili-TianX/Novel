package top.bilitianx.utils

import org.apache.logging.log4j.LogManager
import kotlin.random.*

private val logger = LogManager.getLogger()

fun rest() {
    System.gc()

    Thread.sleep(
        Random.nextLong(2500L..4000L)
            .also { logger.info("休息${it}毫秒") }
    )
}