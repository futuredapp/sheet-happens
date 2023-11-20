package app.futured.sheethappens.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

private const val TaskName = "makeSheetHappen"

open class SheetHappensPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = SheetHappensExtension.createFor(project)

        project.tasks.register(TaskName, LocalizationUpdateTask::class.java) { task ->
            task.spreadsheetId.set(extension.spreadsheetId)
            task.sheetName.set(extension.sheetName)
            task.apiKey.set(extension.apiKey)
            task.sectionColumnName.set(extension.sheetLayout.sectionColumnName)
            task.keyColumnName.set(extension.sheetLayout.keyColumnName)
            task.languageMapping.set(extension.sheetLayout.languageMappings)
            task.resourcesDir.set(extension.resourcesLayout.resourcesDir)
            task.stringsFileName.set(extension.resourcesLayout.stringsFileName)
            task.pluralsFileName.set(extension.resourcesLayout.pluralsFileName)
            task.splitResources.set(extension.resourcesLayout.splitResources)
        }
    }
}