package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.Locale
import app.futured.sheethappens.localizer.model.Resource
import app.futured.sheethappens.localizer.model.SheetEntry
import java.io.OutputStream

internal object ResourcesSerializer {

    fun serialize(
        entries: List<SheetEntry>,
        locale: Locale,
        outputStream: OutputStream
    ) {
        XmlWriter(outputStream).document {
            entries.forEach { entry ->
                when (entry) {
                    is SheetEntry.Section -> writeComment(entry.comment)
                    is SheetEntry.Translation -> {
                        entry
                            .resources
                            .filter { it.locale == locale }
                            .forEach { resource ->
                                when (resource) {
                                    is Resource.Plain -> writeStringResource(resource.key, resource.value.sanitize())
                                    is Resource.Plural -> Unit // TODO plurals
                                }
                            }
                    }
                }
            }
        }.flushAndClose()
    }
}

private fun String.sanitize() =
    this
        .replace("'", "\\'")
        .apply {
            if (!contains("CDATA")) {
                replace("\"", "\\\"")
            }
        }
        .replace("&(?!.{2,4};)".toRegex(), "&amp;")
        .replace("\n", "\\n")

private fun XmlWriter.document(builder: XmlWriter.() -> Unit): XmlWriter = apply {
    writeStartDocument(version = "1.0")
    writeStartElement("resources")
    builder()
    writeEndElement(lineBreak = true)
    writeEndDocument()
}

private fun XmlWriter.writeStringResource(name: String, value: String) {
    writeStartElement(name = "string", attributes = mapOf("name" to name))
    writeCharacters(value)
    writeEndElement()
}

private fun XmlWriter.writePluralResource(name: String, pluralBuilder: XmlWriter.() -> Unit) {
    writeStartElement("plural", mapOf("name" to name))
    pluralBuilder()
    writeEndElement()
}

private fun XmlWriter.writePluralResourceItem(quantityModifier: String, value: String) {
    writeStartElement("item", mapOf("quantity" to quantityModifier))
    writeCharacters(value)
    writeEndElement()
}

private fun XmlWriter.flushAndClose() {
    flush()
    close()
}