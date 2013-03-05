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

    OpenCmsModule(OpenCmsModel model,Project project) {
        this.project = project
        this.model = model
    }

}
