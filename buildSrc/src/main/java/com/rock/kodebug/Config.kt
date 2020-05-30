package com.rock.kodebug

data class Config(var enabled: Boolean = true,
                  var enabledWhenDebug: Boolean = false,
                  val packageList: MutableList<String> = mutableListOf<String>(),
                  val classesList: MutableList<String> = mutableListOf<String>()) {
    companion object {
        fun parse(configFilePath: String): Config {
            val config = Config()
            config.enabled = true
            config.enabledWhenDebug = true
            return config
        }
    }
}