package top.bilitianx

import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.logging.log4j.LogManager
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.absolute

val temporaryHtmlPath = Path("temp.html")

object Browser {
    private val logger = LogManager.getLogger(Browser.javaClass)

    private val driver = ChromeDriver()
    private val httpClient = OkHttpClient()

    val paragraphs: List<String>
        get() = driver.findElements(By.xpath("//p"))
            .map { it.text }
            .filterNot(String::isNullOrBlank)
            .toList()

    fun get(url: String, local: Boolean = false) {
        logger.info("Get: $url")
        if (local) {
            val request = Request.Builder()
                .addHeader(
                    "accept-language",
                    "zh-CN"
                )
                .url(url)
                .build()

            httpClient.newCall(request).execute().use {
                Files.writeString(temporaryHtmlPath, it.body!!.string())

                driver.get("file:///${temporaryHtmlPath.absolute()}")

                Files.delete(temporaryHtmlPath)
            }
        } else {
            driver.get(url)
        }
    }

    private fun getElement(xpath: String): WebElement =
        driver.findElement(By.xpath(xpath))

    fun getElementText(xpath: String): String =
        getElement(xpath).text

    fun getElements(xpath: String): List<WebElement> =
        driver.findElements(By.xpath(xpath))

    fun exit() =
        driver.quit()
}

fun WebElement.getElementText(xpath: String): String =
    findElement(By.xpath(xpath)).text

fun WebElement.getElements(xpath: String): List<WebElement> =
    findElements(By.xpath(xpath))

fun WebElement.getElementAttribute(xpath: String, attr: String): String =
    findElement(By.xpath(xpath)).getAttribute(attr)