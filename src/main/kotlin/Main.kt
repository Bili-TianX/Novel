package top.bilitianx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.apache.logging.log4j.LogManager
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Paths
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.outputStream

const val TEMPLATE_DOCUMENT_PATH = "TEMPLATE.docx"

@OptIn(ExperimentalSerializationApi::class)
fun getDocx(filename: String) {
    val logger = LogManager.getLogger("main")

    val novel = Json.decodeFromStream<Novel>(FileInputStream(filename))
    logger.info("Load: 《${novel.name}》")

    val novelPath = Paths.get("novels", novel.name.toValidPath())
        .also {
            try {
                it.createDirectory()
            } catch (_: FileAlreadyExistsException) {
            }
        }
    logger.info("novelPath: $novelPath")

    for (volume in novel.volumes) {
        val volumePath = novelPath.resolve(volume.name.toValidPath() + ".docx")
        if (volumePath.exists()) {
            logger.info("skip volume: $volumePath")
            continue
        }
        logger.info("volumePath: $volumePath")

        val document = XWPFDocument(ClassLoader.getSystemResourceAsStream(TEMPLATE_DOCUMENT_PATH))
        with(document) {
            addHeading1(volume.name)

            for (chapter in volume.chapters) {
                addHeading2(chapter.name)
                crawlChapter(chapter).map { s -> "    $s" }.forEach(::addText)
            }

            logger.info("write: $volumePath")
        }
        document.write(volumePath.outputStream())
        document.close()

        rest()
    }

    Browser.exit()
}

@OptIn(ExperimentalSerializationApi::class)
fun getJson(id: Int, filename: String) {
    Json.encodeToStream(crawlCatalog(8), FileOutputStream(filename))
    Browser.exit()
}

fun main() {
//    getJson(8, "novels/novel.json")
//    getDocx("novels/novel.json")
}