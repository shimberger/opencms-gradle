package de.himberger.gradle.ocms

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 3/4/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
class OpenCmsManifest extends OpenCmsTask {

    def suffixToResourceType = [
        'jsp': 'jsp',
        'txt': 'plain',
        'xsd': 'plain'
    ]

    def date = new Date()

    @TaskAction
    def createManifest() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.export() {
            info() {
                creator("Admin")
                opencms_version("8.0.2")
                infoproject("Offline")
                export_version("7")
            }
            module() {
                xml.name(project.name)
                xml.nicename(model.module.nicename)
                xml.group(model.module.group)
                // Class
                xml.description(model.module.description)
                xml.version(model.module.version)
                xml.authorname(model.module.authorname)
                xml.authoremail(model.module.authoremail)
                xml.datecreated(now())
                xml.userinstalled(null)
                xml.dateinstalled(null)
                xml.dependencies() {
                    // TODO find project dependencies
                }
                xml.exportpoints() {
                    exportpoint(uri: "/system/modules/$project.name/classes",destination: "WEB-INF/classes")
                    exportpoint(uri: "/system/modules/$project.name/lib",destination: "WEB-INF/lib")
                }
                xml.resources() {
                    resource(uri: "/system/modules/$project.name/")
                }
                xml.parameters() {

                }
                xml.resourcetypes() {

                }
            }
            files() {
                def vfsFiles = project.fileTree("src/main/opencms-vfs")
                vfsFiles.visit { vfsFile ->
                    def relativePath = "/$vfsFile.path"
                    def uuid = UUID.randomUUID()
                    def type = getOpenCmsResourceType(vfsFile.file)
                    file() {
                        if (!vfsFile.directory) { source(relativePath) }
                        destination(relativePath)
                        xml.type(type)
                        uuidstructure(uuid)
                        if (!vfsFile.directory) { uuidresource(uuid) }
                        datelastmodified(now())
                        userlastmodified("Admin")
                        datecreated(now())
                        usercreated("Admin")
                        flags("0")
                        xml.properties() {

                        }
                        relations() {

                        }
                        accesscontrol() {

                        }
                    }
                }
            }
        }
        print writer.toString()
    }

    def now() {
       return date.format("EEE, d MMM yyyy HH:mm:ss z")
    }


    def getOpenCmsResourceType(File file) {
        def metadata = getOpenCmsMetadata(file)
        if (metadata != null) {
            if (metadata.type != null) {
                return metadata.type
            }
        }
        if (file.isDirectory()) {
            return "folder"
        }
        return "binary"
    }

    def getOpenCmsMetadata(File file) {
        def metadataFile = new File(file.getAbsolutePath() + ".meta.json")
        if (metadataFile.exists()) {
            return new JsonSlurper().parseText(metadataFile.text)
        }
        return null;
    }

}
