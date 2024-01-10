package top.bilitianx

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(val name: String, val route: String)

@Serializable
data class Volume(val name: String, val chapters: List<Chapter>)

@Serializable
data class Novel(val id: Int, val name: String, val volumes: List<Volume>)