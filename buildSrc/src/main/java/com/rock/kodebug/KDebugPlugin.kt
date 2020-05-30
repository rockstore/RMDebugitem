package com.rock.kodebug

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class KDebugPlugin:Plugin<Project> {


    override fun apply(project: Project) {

        println("=========================")
        println("========kodebug==========")
        println("=========================")
        val configFilePath = project.projectDir.absolutePath + "/kodebug.xml";
        val configFile = File(configFilePath)
        if (configFile == null || !configFile.exists()) {
            throw GradleException("can not locate kodebug.xml file at:${project.projectDir.absolutePath}")
        }
        Config.configFilePath = configFilePath
        if (!Config.getInstance().enabled) {
            println("not eable KODebug plugin, exit")
            return
        }
        // 只能是 android 或者 library 工程才可以应用该插件
        val hasAppPlugin = project.plugins.hasPlugin("com.android.application")
        val hasLibraryPlugin = project.plugins.hasPlugin("com.android.library")
        if (!hasAppPlugin && !hasLibraryPlugin) {
            println("not app or library, exit")
            return
        }

        val app = project.extensions.getByType(AppExtension::class.java)
        app.registerTransform(KOTransform())

    }
}
