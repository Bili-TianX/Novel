package top.bilitianx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.apache.logging.log4j.LogManager
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileInputStream
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Paths
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.outputStream

const val TEMPLATE_DOCUMENT_PATH = """C:\Users\TianX\Documents\Novel\TEMPLATE.docx"""

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val logger = LogManager.getLogger("main")

    val novel = Json.decodeFromStream<Novel>(FileInputStream("novels/novel2.json"))
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

        val document = XWPFDocument(FileInputStream(TEMPLATE_DOCUMENT_PATH))
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