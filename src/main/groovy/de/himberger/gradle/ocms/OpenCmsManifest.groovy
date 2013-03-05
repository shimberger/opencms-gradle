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
        'jsp':  'jsp',
        'tag':  'jsp',
        'json': 'plain',
        'png':  'binary',
        'gif':  'binary',
        'jpg':  'binary',
        'jpeg': 'binary',
        'txt':  'plain',
        'xml':  'plain',
        'xml':  'plain',
        'json': 'plain',
        'css':  'plain',
        'js':   'plain',
        'xsd':  'plain'
    ]

    def date = new Date()

    @TaskAction
    def createManifest() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        def manifestFile = project.file("build/opencms-module/manifest.xml")
        outputs.file(manifestFile)
        inputs.files("src/main/opemcms-vfs/")
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
                xml.class(model.module.moduleClass)
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
                    model.module.exportpoints.each {
                        exportpoint(it)
                    }
                }
                xml.resources() {
                    model.module.resources.each {
                        resource(uri: it)
                    }
                }
                xml.parameters() {
                    // TODO Parameters
                }
                xml.resourcetypes() {
                    model.module.resourcetypesClosure.call(xml)
                }
                xml.explorertypes {
                    model.module.explorertypesClosure.call(xml)
                }
            }
            files() {
                def vfsFiles = project.fileTree(dir: "build/opencms-module", excludes: ["**/*.meta.json","manifest.xml"])
                vfsFiles.visit { vfsFile ->
                    def relativePath = "$vfsFile.path"
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
                            getProperties(vfsFile.file).each { property ->
                                if (property.type != null) {
                                    xml.property(type: property.type) {
                                        xml.name(property.name)
                                        xml.value(property.value)
                                    }
                                } else {
                                    xml.property() {
                                        xml.name(property.name)
                                        xml.value(property.value)
                                    }
                                }
                            }
                        }
                        relations() {

                        }
                        accesscontrol() {

                        }
                    }
                }
            }
        }
        manifestFile.parentFile.mkdirs()
        manifestFile.createNewFile()
        manifestFile.text = writer.toString()
        //print writer.toString()
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
        def suffix = file.name.substring(file.name.lastIndexOf('.')+1)
        if (suffixToResourceType.containsKey(suffix)) {
            return suffixToResourceType[suffix]
        }
        return "binary"
    }

    public getProperties(File file) {
        def properties = []
        withMetadata(file) {
            println "properties: ${it.properties[0].name}"
            it.properties.each {
              properties.add(new Expando(name: it.name, value: it.value, type: it.type))
            }
        }
        return properties
    }

    def withMetadata(File file, Closure closure) {
        def metadataFile = new File(file.getAbsolutePath() + ".meta.json")
        if (metadataFile.exists()) {
            def metadata = new JsonSlurper().parseText(metadataFile.text)
            closure.call(metadata)
        }
    }


    def getOpenCmsMetadata(File file) {
        def metadataFile = new File(file.getAbsolutePath() + ".meta.json")
        if (metadataFile.exists()) {
            return new JsonSlurper().parseText(metadataFile.text)
        }
        return null;
    }

}
