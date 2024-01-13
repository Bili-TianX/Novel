package top.bilitianx

import org.apache.logging.log4j.LogManager
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.edge.EdgeDriver

object Browser {
    private val logger = LogManager.getLogger(Browser.javaClass)

    private var driver = EdgeDriver()

    val paragraphs: List<String>
        get() = driver.findElements(By.xpath("//p"))
            .map { it.text }
            .filterNot(String::isNullOrBlank)
            .toList()

    fun get(url: String) {
        logger.info("Get: $url")
        driver.get(url)
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

fun WebElement.getElementAttribute(xpath: String, attr: String): String =
    findElement(By.xpath(xpath)).getAttribute(attr)