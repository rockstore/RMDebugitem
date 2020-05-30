package com.rock.kodebug

import com.android.utils.forEach
import javax.xml.parsers.DocumentBuilderFactory

data class Config(var enabled: Boolean = true,
                  var enabledWhenDebug: Boolean = false,
                  val packageList: MutableList<String> = mutableListOf<String>(),
                  val classesList: MutableList<String> = mutableListOf<String>()) {
    companion object {
        // 解析配置文件
        fun parse(configFilePath: String): Config {
            val builderFactory = DocumentBuilderFactory.newInstance()
            val documentBuilder = builderFactory.newDocumentBuilder()
            val parseResult = documentBuilder.parse(configFilePath)
            val enabledElement = parseResult.getElementsByTagName("enabled")?.item(0)
            val enableWhenDebugElement = parseResult.getElementsByTagName("enableWhenDebug")?.item(0)
            val packageElements = parseResult.getElementsByTagName("packages")?.item(0)?.childNodes
            val classElements = parseResult.getElementsByTagName("classes")?.item(0)?.childNodes
            val config = Config()
            config.enabled = "true" == enabledElement?.firstChild?.nodeValue
            config.enabledWhenDebug = "true" == enableWhenDebugElement?.firstChild?.nodeValue
            packageElements?.forEach {
                it?.firstChild?.let {
                    config.packageList.add(it.nodeValue)
                }
            }
            classElements?.forEach {
                it?.firstChild?.let {
                    config.classesList.add(it.nodeValue)
                }
            }
            return config
        }
    }
}