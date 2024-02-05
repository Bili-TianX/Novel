package top.bilitianx.utils

import org.apache.poi.xwpf.usermodel.XWPFDocument

private fun XWPFDocument.addStyledText(s: String, style: String? = null) {
    createParagraph().apply { this.style = style }.createRun().setText(s)
}

fun XWPFDocument.addHeading1(s: String) = addStyledText(s, "1")

fun XWPFDocument.addHeading2(s: String) = addStyledText(s, "2")

fun XWPFDocument.addText(s: String) = addStyledText(s)
