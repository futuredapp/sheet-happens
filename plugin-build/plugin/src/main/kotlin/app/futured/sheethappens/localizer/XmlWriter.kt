package app.futured.sheethappens.localizer

import java.io.OutputStream
import java.util.*

internal class XmlWriter(outputStream: OutputStream) {

    companion object {
        private const val INDENT = "    "
    }

    private val writer = outputStream.bufferedWriter(Charsets.UTF_8)
    private val tags: Stack<String> = Stack()

    fun writeStartDocument(version: String = "1.0") {
        writer.append("<?xml version=\"$version\" encoding=\"utf-8\"?>")
    }

    fun writeEndDocument() {
        writer.newLine()
    }

    fun writeComment(value: String) {
        writer.newLine()
        writer.newLine()
        writer.append(INDENT.repeat(tags.count()))
        writer.append("<!-- $value -->")
    }

    fun writeStartElement(name: String, attributes: Map<String, String> = emptyMap()) {
        writer.newLine()
        writer.write(INDENT.repeat(tags.count()))
        tags.push(name)
        writer.append("<$name")
        for (attribute in attributes) {
            writer.append(" ${attribute.key}=\"${attribute.value}\"")
        }
        writer.append(">")
    }

    fun writeEndElement(lineBreak: Boolean = false) {
        if (lineBreak) {
            writer.newLine()
            writer.append(INDENT.repeat(tags.count() - 1))
        }
        writer.append("</${tags.pop()}>")
    }

    fun writeCharacters(value: String) {
        writer.append(value)
    }

    fun flush() {
        writer.flush()
    }

    fun close() {
        writer.close()
    }
}