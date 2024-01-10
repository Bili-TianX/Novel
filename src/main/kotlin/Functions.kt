package top.bilitianx

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.openqa.selenium.By
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.random.Random

object Browser {
    val driver: RemoteWebDriver = EdgeDriver()
}

fun crawlCatalog(id: Int): Novel {
    Browser.driver.get("$HOST/novel/${id}/catalog")

    val volumes = ArrayList<Volume>()
    for (element in Browser.driver.findElements(By.xpath("//ol[@id=\"volumes\"]/li")))
        when (element.getAttribute("class")) {
            "chapter-li jsChapter" -> {
                val chapters = volumes.last().chapters as ArrayList<Chapter>

                val name = element.findElement(By.xpath("a/span")).text
                val route = element.findElement(By.xpath("a")).getAttribute("href")
                chapters.add(Chapter(name, route))
            }

            "chapter-bar chapter-li" -> {
                val name = element.findElement(By.xpath("h3")).text
                volumes.add(Volume(name, ArrayList()))
            }
        }

    val title = Browser.driver.findElement(By.xpath("//h1")).text
    return Novel(id, title, volumes)
}

fun getTexts(): List<String> {
    with(ArrayList<String>()) {
        for (element in Browser.driver.findElements(By.xpath("//p"))) {
            val text = element.text
            if (!text.isNullOrBlank()) {
                add(text)
            }
        }
        return this
    }
}

fun crawlChapter(chapter: Chapter): List<String> {
    var i = 1
    val template = chapter.route.dropLast(5) + "_%d.html"

    with(ArrayList<String>()) {
        while (true) {
            Browser.driver.get(template.format(i++))
            val texts = getTexts()
            if (texts.isEmpty()) {
                break
            }
            addAll(texts)
            Thread.sleep(Random.nextLong(1000, 3000))
        }
        return this
    }
}

fun String.sanitize(): String {
    return replace("""[<>:"/\\|?*]""".toRegex(), "_")
}

fun Path.tryCreateDirectory() {
    try {
        createDirectory()
    } catch (_: FileAlreadyExistsException) {
    }
}

private fun XWPFDocument.addStyledText(s: String, style: String? = null) {
    createParagraph().apply { this.style = style }.createRun().setText(s)
}

fun XWPFDocument.addHeading1(s: String) = addStyledText(s, "1")

fun XWPFDocument.addHeading2(s: String) = addStyledText(s, "2")

fun XWPFDocument.addText(s: String) = addStyledText(s)
