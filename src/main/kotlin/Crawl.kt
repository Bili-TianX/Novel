package top.bilitianx

import org.apache.logging.log4j.LogManager
import org.openqa.selenium.By

const val HOST = "https://www.linovelib.com/"

val logger = LogManager.getLogger("crawl")

fun crawlCatalog(id: Int): Novel {
    Browser.get("$HOST/novel/${id}/catalog")

    val novelName = Browser.getElementText("//div[@class='book-meta']/h1")
    val volumes = Browser.getElements("""//div[@id="volume-list"]/div[@class="volume clearfix"]""")
        .map { it ->
            val volumeName = it.getElementText("""div[@class="volume-info"]/h2""")
            val chapters = it.findElements(By.xpath("ul/li"))
                .map {
                    val chapterName = it.getElementText("a")
                    val chapterUrl = it.getElementAttribute("a", "href")
                    Chapter(
                        chapterName,
                        chapterUrl
                    )
                }
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