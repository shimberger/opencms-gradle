package de.himberger.gradle.ocms

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
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
        manifestTask.dependsOn "ocmsPrepareModuleDirectory"
        manifestTask.conventionMapping.model = {
            project.convention.plugins.opencms
        }

        def copyTask = project.tasks.add("ocmsModuleFiles", Copy)
        copyTask.into project.file("build/opencms-module")
        copyTask.from project.fileTree("src/main/opencms-vfs") {

        }

        def libsTask = project.tasks.add("ocmsModuleLibs", Copy)
        libsTask.dependsOn "jar"
        libsTask.into project.file("build/opencms-module/system/modules/$project.name/lib")
        libsTask.from project.configurations.runtime
        libsTask.from { project.tasks.jar.archivePath }

        def ocmsPrepareModule = project.tasks.add("ocmsPrepareModuleDirectory", DefaultTask)
        ocmsPrepareModule.dependsOn "ocmsModuleFiles", "ocmsModuleLibs"


        def moduleTask = project.tasks.add("ocmsModule", Zip)
        //moduleTask.
        moduleTask.baseName = project.name
        moduleTask.destinationDir = project.file("build/libs")
        moduleTask.from project.fileTree('build/opencms-module')
        moduleTask.dependsOn "ocmsManifest"
        moduleTask.outputs.upToDateWhen { return false }



    }

}
