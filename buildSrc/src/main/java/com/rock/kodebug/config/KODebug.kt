package com.rock.kodebug.config

open class KODebug(// 是否启用
              var enable: Boolean = true,
              // debug 包是否需要进行优化
              var koWhenDebug: Boolean = false,
              // 白名单
              var whiteList: Array<String> = emptyArray()
              ) {
}