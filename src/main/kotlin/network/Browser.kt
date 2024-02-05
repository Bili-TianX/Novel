package top.bilitianx.network

import org.apache.logging.log4j.LogManager
import org.jsoup.Jsoup
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import top.bilitianx.utils.*
import java.nio.file.Files
import kotlin.io.path.*

private val localHTMLPath = Path("temp.html")

object Browser : AutoCloseable {
    private val logger = LogManager.getLogger()

    private val driver = ChromeDriver()

    fun getParagraphs(): List<String> = driver
        .findElements(By.xpath("//p"))
        .map(WebElement::getText)
        .filter(String::isNotBlank)
        .toList()

    fun get(url: String, local: Boolean = false) {
        logger.info("访问网址（local=$local)：$url")

        driver.get(
            when (local) {
                true -> {
                    val html = Jsoup
                        .connect(url)
                        .headers(HEADERS)
                        .get()
                        .injectScript()

                    Files.writeString(localHTMLPath, html.toString())

                    "file:///${localHTMLPath.absolute()}"
                }

                false -> url
            }
        )
    }

    fun getElement(xpath: String): WebElement =
        driver.findElement(By.xpath(xpath))

    fun getElements(xpath: String): List<WebElement> =
        driver.findElements(By.xpath(xpath))

    override fun close() =
        driver.quit()
}