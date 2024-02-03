package top.bilitianx

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val HOST = "https://www.bilinovel.com/"

val logger: Logger = LogManager.getLogger("crawl")

fun crawlCatalog(id: Int): Novel {
    Browser.get("$HOST/novel/${id}/catalog")

    val novelName = Browser.getElementText("""//span[@class="header-back-title"]""")
    logger.info("novelName: $novelName")

    val volumes = mutableListOf<Volume>()

    for (element in Browser.getElements("""//ol[@id="volumes"]/li""")) {
        when (element.getAttribute("class")) {
            "chapter-bar chapter-li" -> {
                volumes.add(Volume(element.getElementText("h3"), mutableListOf()))
            }

            "chapter-li jsChapter" -> {
                volumes.last().chapters.add(
                    Chapter(
                        element.getElementText("a/span"),
                        element.getElementAttribute("a", "href")
                    )
                )
            }
        }
    }

//    val volumes = Browser.getElements("""//ol[@id="volumes"]/li""")
//        .map { it ->
//            val volumeName = it.getElementText("""div[@class="volume-info"]/h2""")
//            logger.info("volumeName: $volumeName")
//
//            val chapters = it.getElements("li")
//                .map {
//                    val chapterName = it.getElementText("a")
//                    logger.info("chapterName: $chapterName")
//
//                    val chapterUrl = it.getElementAttribute("a", "href")
//                    logger.info("chapterUrl: $chapterUrl")
//
//                    if ("javascript" in chapterUrl) {
//                        logger.warn("Invalid url: $volumeName, $chapterName")
//                    }
//
//                    Chapter(
//                        chapterName,
//                        chapterUrl
//                    )
//                }
//            logger.info("chapters: $chapters")
//
//            Volume(volumeName, chapters)
//        }

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
            Browser.get(chapter.urlTemplate.format(i++), local = true)
            Browser.scroll()
            rest()

            with(Browser.paragraphs) {
                if (isEmpty()) {
                    logger.info("Finish: ${chapter.name}")
                    hasNext = false
                } else {
                    logger.info("Finish: Part ${i - 1}")
                    addAll(this)
                }
            }
        }

        return this
    }
}