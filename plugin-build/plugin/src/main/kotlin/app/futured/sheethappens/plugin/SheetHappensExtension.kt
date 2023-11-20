package app.futured.sheethappens.plugin

import app.futured.sheethappens.plugin.configuration.LanguageMapping
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * This [SheetHappensExtension] is used for configuring localization plugin in a Gradle project.
 */
open class SheetHappensExtension @Inject constructor(objects: ObjectFactory) {

    companion object {
        internal fun createFor(project: Project): SheetHappensExtension =
            project.extensions.create(/* name = */
                "sheetHappens", /* type = */
                SheetHappensExtension::class.java, /* ...constructionArguments = */
                project.objects,
            )
    }

    /**
     * A Google spreadsheet ID.
     */
    val spreadsheetId: Property<String> = objects.property(String::class.java)

    /**
     * The name of sheet that contains localizations.
     */
    val sheetName: Property<String> = objects.property(String::class.java)

    /**
     * API key used for access to Google spreadsheet API.
     */
    val apiKey: Property<String> = objects.property(String::class.java)

    /**
     * Used to configure layout of the Sheet which contains translations.
     */
    internal val sheetLayout: SheetLayoutHandler = objects.newInstance(SheetLayoutHandler::class.java)

    /**
     * Used to configure a layout of generated resource files.
     */
    internal val resourcesLayout: ResourcesLayoutHandler = objects.newInstance(ResourcesLayoutHandler::class.java)

    /**
     * Used to configure layout of the Sheet which contains translations.
     */
    fun sheetLayout(action: Action<SheetLayoutHandler>) = action.execute(sheetLayout)

    /**
     * Used to configure a layout of generated resource files.
     */
    fun resourcesLayout(action: Action<ResourcesLayoutHandler>) = action.execute(resourcesLayout)
}

/**
 * An object container which defines the layout of provided Google Sheet, such as where to look for string keys, etc.
 */
open class SheetLayoutHandler @Inject constructor(objects: ObjectFactory) {

    /**
     * Column name which contains section headers.
     *
     * Optional, defaults to "section".
     */
    val sectionColumnName: Property<String> = objects.property(String::class.java)

    /**
     * Column name which contains resource keys.
     */
    val keyColumnName: Property<String> = objects.property(String::class.java)

    /**
     * List of language mappings where column name to resource qualifier mapping can be specified.
     */
    internal val languageMappings: ListProperty<LanguageMapping> = objects.listProperty(LanguageMapping::class.java)

    /**
     * Configures Google Sheet column name and resources subdirectory in which resources for this language will be generated.
     *
     * @param mapping Pair of column name and directory name, such as `"EN" to "values"`, or `"CZ" to "values-cs"`
     */
    fun languageColumn(mapping: Pair<String, String>) {
        languageMappings.set(languageMappings.get() + LanguageMapping(mapping.first, mapping.second))
    }
}

/**
 * An object container which defines a layout of generated string resources, such as resource folder path, etc.
 */
open class ResourcesLayoutHandler @Inject constructor(objects: ObjectFactory) {

    /**
     * Folder where to put generated resources relative to [Project], such as "src/main/res"
     */
    val resourcesDir: DirectoryProperty = objects.directoryProperty()

    /**
     * Name of file where string resources will be generated.
     *
     * Optional, defaults to "strings.xml"
     */
    val stringsFileName: Property<String> = objects.property(String::class.java)

    /**
     * Name of file where plural resources will be generated if [splitResources] is `true`.
     *
     * Optional, defaults to "plurals.xml"
     */
    val pluralsFileName: Property<String> = objects.property(String::class.java)

    /**
     * If `true`, all strings and plurals will be generated into separate xml files
     * instead of a single one.
     *
     * Optional, defaults to `false`.
     */
    val splitResources: Property<Boolean> = objects.property(Boolean::class.java)
}
