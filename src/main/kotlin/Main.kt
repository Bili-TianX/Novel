package top.bilitianx

import top.bilitianx.network.*

fun main() = Browser.use {
    val filename = "novels/后藤.json"

//    persistJSON(3642, filename)
    persistMSWord(filename)
}