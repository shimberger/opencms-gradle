package de.himberger.gradle.ocms

import org.gradle.api.Project

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 3/5/13
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
class OpenCmsModule {

    def project = null
    def model = null

    def group = ""
    def nicename = ""
    def version = "0.0.1    "
    def authorname = null
    def authoremail = null
    def description = null
    def explorertypesClosure = {}
    def resourcetypesClosure = {}
    def resources = []
    def exportpoints = []
    def moduleClass = null

    OpenCmsModule(OpenCmsModel model,Project project) {
        this.project = project
        this.model = model
        this.resources += "/system/modules/$project.name/"
        this.exportpoints += [uri: "/system/modules/$project.name/classes",destination: "WEB-INF/classes"]
        this.exportpoints += [uri: "/system/modules/$project.name/lib", destination: "WEB-INF/lib"]
    }

    def explorertypes(Closure closure) {
        explorertypesClosure = closure
    }

    def resourcetypes(Closure closure) {
        resourcetypesClosure = closure
    }

}
