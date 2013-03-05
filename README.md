Gradle OpenCms Build Script
===========================

This project provides an OpenCms friendly Gradle Plugin.
The plugin is still in development!

Usage
-----

For now this project should be used by adding it as the buildSrc using a git submodule.
To do this execute the following commmand in your project root:

    git submodule add git@bitbucket.org:shimberger/gradle-opencms.git buildSrc

Then enter the following line in your build.gradle

    apply plugin: de.himberger.gradle.ocms.OpenCmsPlugin

The project provides an ocmsModule task which creates a manifest.xml and module zip based on the files in
'src/main/opencms-vfs'. Example config:

    opencms {
        module {
            group = "com.example.mymodule"
            version = "0.6"
            moduleClass = "..." // class is a keyword so moduleClass is used
        }
    }

For questions please contact me directly!