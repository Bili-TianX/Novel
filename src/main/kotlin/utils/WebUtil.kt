package top.bilitianx.utils

import org.jsoup.nodes.*
import org.openqa.selenium.*


val HEADERS = mapOf(
    "accept-language" to "zh-CN",
    "User-Agent" to """Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36 Edg/121.0.0.0"""
)

private val SCRIPT =
    Element("script")
        .append("""document.addEventListener = function (type, listener, options) { } """)
        .outerHtml()

fun WebElement.getElementText(xpath: String): String =
    findElement(By.xpath(xpath)).text

fun WebElement.getElementAttribute(xpath: String, attr: String): String =
    findElement(By.xpath(xpath)).getAttribute(attr)

fun Document.injectScript() =
    head().prepend(SCRIPT).let { this }