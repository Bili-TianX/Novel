package top.bilitianx.utils

import java.nio.file.*
import kotlin.io.path.createDirectory

fun String.toValidPath(): String {
    return replace("""[<>:"/\\|?*]""".toRegex(), "_")
}

fun Path.tryCreateDirectory(): Path {
    try {
        createDirectory()
    } catch (_: FileAlreadyExistsException) {
    }

    return this
}