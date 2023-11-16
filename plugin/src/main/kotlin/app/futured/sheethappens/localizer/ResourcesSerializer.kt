package app.futured.sheethappens.localizer

import app.futured.sheethappens.localizer.model.XmlElement
import java.io.OutputStream

/**
 * Object responsible for writing XML element models into XML file.
 */
internal object ResourcesSerializer {

    /**
     * Serializes [xmlElements] into provided [outputStream][OutputStream] in XML format.
     */
    fun serialize(
        xmlElements: List<XmlElement>,
        outputStream: OutputStream
    ) {
        XmlWriter(outputStream).document {
            xmlElements.forEach { xmlElement ->
                when (xmlElement) {
                    is XmlElement.PlainResource -> {
                        xmlElement.comments.forEach { comment ->
                            writeComment(comment)
                        }
                        writeStringResource(xmlElement.key, xmlElement.value.sanitize())
                    }

                    is XmlElement.PluralResource -> {
                        xmlElement.comments.forEach { comment ->
                            writeComment(comment)
                        }
                        writePluralResource(xmlElement.key) {
                            xmlElement.items.forEach { pluralItem ->
                                writePluralResourceItem(pluralItem.quantityModifier, pluralItem.value.sanitize())
                            }
                        }
                    }
                }
            }
        }.flushAndClose()
    }
}

/**
 * Sanitizes [String] into XML-friendly format.
 */
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
    writeStartElement("plurals", mapOf("name" to name))
    pluralBuilder()
    writeEndElement(lineBreak = true)
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