package top.bilitianx.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import org.apache.logging.log4j.LogManager
import org.apache.poi.xwpf.usermodel.XWPFDocument
import top.bilitianx.novel.*
import top.bilitianx.utils.*
import java.io.FileInputStream
import java.nio.file.Paths
import kotlin.io.path.*

private const val ROOT_NOVEL_FOLDER = "novels"
private const val TEMPLATE_MS_WORD_PATH = "TEMPLATE.docx"
private const val PARAGRAPH_PREFIX = "    "

@Suppress("JSON_FORMAT_REDUNDANT")
@OptIn(ExperimentalSerializationApi::class)
fun persistJSON(id: Int, filename: String? = null) = crawlCatalog(id).run {
    val path = if (filename == null) Path(ROOT_NOVEL_FOLDER, "$name.json") else Path(filename)
    Json { prettyPrint = true }.encodeToStream(
        this,
        (path).outputStream()
    )
    path
}

@OptIn(ExperimentalSerializationApi::class)
fun persistMSWord(filename: String) {
    val logger = LogManager.getLogger()

    logger.info("读取小说：$filename")

    val novel = Json.decodeFromStream<Novel>(FileInputStream(filename))
        .also { logger.info("小说名：${it.name}") }

    val invalidChapters = sequence {
        novel.volumes.forEach { volume ->
            volume.chapters.forEach { chapter ->
                if ("javascript" in chapter.url) {
                    yield(Pair(volume, chapter))
                }
            }
        }
    }.toList()

    if (invalidChapters.isNotEmpty()) {
        logger.error("小说的JSON中存在非法章节：")

        invalidChapters.forEach { (volume, chapter) ->
            logger.error("“${volume.name}” 的 “${chapter.name}”")
        }

        return
    }

    val novelFolder = Paths.get(ROOT_NOVEL_FOLDER, novel.name.toValidPath())
        .tryCreateDirectory()
        .also { logger.info("小说路径：$it") }

    for (volume in novel.volumes) {
        logger.info("尝试爬取卷：${volume.name}")

        val volumePath = novelFolder.resolve(volume.name.toValidPath() + ".docx")
            .also { logger.info("卷路径：${it}") }

        if (volumePath.exists()) {
            logger.info("${volumePath}已存在，跳过")
            continue
        }

        with(
            XWPFDocument(
                ClassLoader.getSystemResourceAsStream(TEMPLATE_MS_WORD_PATH)
            )
        ) {
            addHeading1(volume.name)

            for (chapter in volume.chapters) {
                addHeading2(chapter.name)

                crawlChapter(chapter).forEach { addText(PARAGRAPH_PREFIX + it) }
            }

            logger.info("尝试写入Word文档: $volumePath")
            write(volumePath.outputStream())
            close()
        }

        rest()
    }
}
