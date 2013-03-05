package de.himberger.gradle.ocms

import org.gradle.api.Project

/**
 * Created with IntelliJ IDEA.
 * User: sebastian
 * Date: 3/4/13
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
class OpenCmsModel {

    def project = null
    def module = null

    OpenCmsModel(Project project) {
        this.project = project
        this.module = new OpenCmsModule(this,project)
    }

    def opencms(Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

    def module(Closure closure) {
        closure.delegate = module
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

}
