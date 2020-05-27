package com.rock.kodebug

import com.rock.kodebug.config.KODebug
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension


const val CONFIG_NAME = "KODebug"

class KDebugPlugin:Plugin<Project> {


    override fun apply(project: Project) {
        println("=========================")
        println("========kodebug==========")
        println("=========================")
        // 创建扩展
        project.extensions.create(CONFIG_NAME, KODebug::class.java)
        project.afterEvaluate {
            val config = project.extensions.findByName(CONFIG_NAME) as KODebug
            println("koconfig:" + config.enable + "-" + config.koWhenDebug + "-" + config.whiteList)
            if (config == null || !config.enable) {
                return@afterEvaluate
            }
            val hasPlugin = project.plugins.hasPlugin("com.android.application")
            // 必须是 application 工程才能进行优化，否则没有 dex 产生
            if (!hasPlugin) {
                return@afterEvaluate
            }
            val appExtension: AppExtension = project.extensions.getByType(AppExtension::class.java)
            appExtension.registerTransform(KOTransform(config.whiteList))
        }
    }
}
