package de.himberger.gradle.ocms

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 3/4/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
class OpenCmsPlugin implements Plugin<Project> {

    def model = new OpenCmsModel()

    def void apply(Project project) {
        project.convention.plugins.opencms = model

        def manifestTask = project.tasks.add("ocmsManifest",OpenCmsManifest)
        manifestTask.conventionMapping.model = {
            project.convention.plugins.opencms
        }

        def moduleTask = project.tasks.add("ocmsModule", Zip)
        //moduleTask.
        moduleTask.baseName = project.name
        moduleTask.destinationDir = project.file("build/libs")
        moduleTask.from project.fileTree('src/main/opencms-vfs')
        moduleTask.from project.tasks.jar.archiveName
        moduleTask.dependsOn "jar", "ocmsManifest"
        moduleTask.outputs.upToDateWhen { return false }



    }

}
