package com.rock.kodebug

import com.android.utils.forEach
import javax.xml.parsers.DocumentBuilderFactory

data class Config(var enabled: Boolean = true,
                  var enabledWhenDebug: Boolean = false,
                  var recordFilePath: String? = "",
                  val packageList: MutableList<String> = mutableListOf<String>(),
                  val classesList: MutableList<String> = mutableListOf<String>()
                  ) {
    companion object {

        var configFilePath: String? = null
        private var config: Config? = null

        fun getInstance(): Config {
            if (config == null) {
                config = parse(configFilePath!!)
            }
            return config!!
        }

        // 解析配置文件
        private fun parse(configFilePath: String): Config {
            val builderFactory = DocumentBuilderFactory.newInstance()
            val documentBuilder = builderFactory.newDocumentBuilder()
            val parseResult = documentBuilder.parse(configFilePath)
            val enabledNode = parseResult.getElementsByTagName("enabled")?.item(0)
            val enableWhenDebugNode = parseResult.getElementsByTagName("enable-when-debug")?.item(0)
            val recordFilePathNode = parseResult.getElementsByTagName("record-file-path")?.item(0)
            val packageNodeList = parseResult.getElementsByTagName("white-list-packages")?.item(0)?.childNodes
            val classNodeList = parseResult.getElementsByTagName("white-list-classes")?.item(0)?.childNodes
            val config = Config()
            config.enabled = "true" == enabledNode?.firstChild?.nodeValue
            config.enabledWhenDebug = "true" == enableWhenDebugNode?.firstChild?.nodeValue
            config.recordFilePath = recordFilePathNode?.firstChild?.nodeValue
            packageNodeList?.forEach {
                it?.firstChild?.let {
                    config.packageList.add(it.nodeValue)
                }
            }
            classNodeList?.forEach {
                it?.firstChild?.let {
                    config.classesList.add(it.nodeValue)
                }
            }
            return config
        }
    }
}