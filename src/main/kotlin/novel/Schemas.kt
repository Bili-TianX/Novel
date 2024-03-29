package top.bilitianx.novel

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(val name: String, val url: String) {
    val urlTemplate by lazy { url.dropLast(5) + "_%d.html" }
}

@Serializable
data class Volume(val name: String, val chapters: MutableList<Chapter> = mutableListOf())

@Serializable
data class Novel(val id: Int, val name: String, val volumes: List<Volume>)
