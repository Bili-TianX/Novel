package top.bilitianx.novel

import org.apache.logging.log4j.LogManager
import top.bilitianx.network.Browser
import top.bilitianx.utils.*

const val HOST = "https://www.bilinovel.com"

private val logger = LogManager.getLogger()

fun crawlCatalog(id: Int): Novel {
    logger.info("尝试爬取小说目录（id = $id)")
    Browser.get("$HOST/novel/${id}/catalog")

    val novelName = Browser.getElement("""//span[@class="header-back-title"]""").text
        .also { logger.info("小说名：$it") }

    val volumes = mutableListOf<Volume>()

    Browser.getElements("""//ul[@class="volume-chapters"]/li""").forEach {
        when (it.getAttribute("class")) {
            "chapter-bar chapter-li" -> {
                volumes.add(
                    Volume(it.getElementText("h3")).also { volume -> logger.info("添加卷：${volume.name}") }
                )
            }

            "chapter-li jsChapter" -> {
                val volume = volumes.last()

                volume.chapters.add(
                    Chapter(
                        it.getElementText("a/span"),
                        it.getElementAttribute("a", "href")
                    ).also { chapter ->
                        logger.info("添加章节：${chapter}")

                        if ("javascript" in chapter.urlTemplate) {
                            logger.warn("请手动修正 “${volume.name}” 的 “${chapter.name}” 的 url")
                        }
                    }
                )
            }
        }
    }

    return Novel(
        id,
        novelName,
        volumes
    )
}

fun crawlChapter(chapter: Chapter) = sequence {
    var i = 1
    var hasNext = true

    logger.info("爬取章节: $chapter")
    while (hasNext) {
        logger.info("尝试爬取第 $i 部分")
        Browser.get(chapter.urlTemplate.format(i++), local = true)

        with(Browser.getParagraphs()) {
            if (isNotEmpty()) {
                logger.info("完成第 $i 部分")
                yieldAll(this)
            } else {
                logger.info("${chapter.name} 爬取完成")
                hasNext = false
            }
        }

        rest()
    }
}