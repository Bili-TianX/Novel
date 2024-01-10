package top.bilitianx

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileInputStream
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.outputStream

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    try {
//        val novel = crawlCatalog(8)

//        Json.encodeToStream(novel,  FileOutputStream("novel.json"))
//        return

        val novel = Json.decodeFromStream<Novel>(FileInputStream("novel2.json"))

        val novelPath = Paths.get("novels", novel.name.sanitize())
            .also { it.tryCreateDirectory() }

        for (volume in novel.volumes) {
            val volumePath = novelPath.resolve(volume.name.sanitize() + ".docx")
            if (volumePath.exists()) {
                continue
            }

            val document = XWPFDocument(FileInputStream("""C:\Users\TianX\Documents\Novel\TEMPLATE.docx"""))
                .apply { addHeading1(volume.name) }

            for (chapter in volume.chapters) {
                document.addHeading2(chapter.name)
                crawlChapter(chapter).map { s -> "    $s" }.forEach(document::addText)
            }

            document.write(volumePath.outputStream())
            document.close()
            System.gc()
        }
    } finally {
        Browser.driver.quit()
    }
}