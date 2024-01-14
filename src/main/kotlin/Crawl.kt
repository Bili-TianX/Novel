package top.bilitianx

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val HOST = "https://www.linovelib.com/"

val logger: Logger = LogManager.getLogger("crawl")

fun crawlCatalog(id: Int): Novel {
    Browser.get("$HOST/novel/${id}/catalog")

    val novelName = Browser.getElementText("//div[@class='book-meta']/h1")
    logger.info("novelName: $novelName")

    val volumes = Browser.getElements("""//div[@id="volume-list"]/div[@class="volume clearfix"]""")
        .map { it ->
            val volumeName = it.getElementText("""div[@class="volume-info"]/h2""")
            logger.info("volumeName: $volumeName")

            val chapters = it.getElements("ul/li")
                .map {
                    val chapterName = it.getElementText("a")
                    logger.info("chapterName: $chapterName")

                    val chapterUrl = it.getElementAttribute("a", "href")
                    logger.info("chapterUrl: $chapterUrl")

                    if ("javascript" in chapterUrl) {
                        logger.warn("Invalid url: $volumeName, $chapterName")
                    }

                    Chapter(
                        chapterName,
                        chapterUrl
                    )
                }
            logger.info("chapters: $chapters")

            Volume(volumeName, chapters)
        }

    return Novel(
        id,
        novelName,
        volumes
    )
}

fun crawlChapter(chapter: Chapter): List<String> {
    logger.info("Crawl: ${chapter.name} (${chapter.urlTemplate})")

    with(mutableListOf<String>()) {
        var i = 1
        var hasNext = true

        while (hasNext) {
            Browser.get(chapter.urlTemplate.format(i++))

            with(Browser.paragraphs) {
                if (isEmpty()) {
                    logger.info("Finish: ${chapter.name}")
                    hasNext = false
                } else {
                    logger.info("Finish: Part ${i - 1}")
                    addAll(this)
                }
            }

            rest()
        }

        return this
    }
}