package com.rock.kodebug.utils

import org.gradle.api.GradleException
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter


class FileAppendUtils(val appendFilePath: String) {

    private var fileWriter: FileWriter? = null
    private var bufferedWriter: BufferedWriter? = null
    private var printWriter: PrintWriter? = null

    private var inited = false
    private var opened = false

    fun openStream() {
        inited = true
        fileWriter = FileWriter(appendFilePath, true)
        bufferedWriter = BufferedWriter(fileWriter)
        printWriter = PrintWriter(bufferedWriter)
        opened = true
    }

    fun append(content: String, newLine: Boolean) {
        if (!inited) {
            openStream()
        }
        if (!opened) {
            throw GradleException("can not open ${appendFilePath}")
        }
        if (newLine) {
            printWriter?.println(content)
        } else {
            printWriter?.print(content)
        }
        printWriter?.flush()
    }

    fun close() {
        inited = false
        printWriter?.close()
        bufferedWriter?.close()
        fileWriter?.close()
        opened = false
    }

    companion object {
        var recordFilePath: String? = null
        var fileAppendUtils: FileAppendUtils? = null
        fun getInstance(): FileAppendUtils {
            if (fileAppendUtils == null) {
                fileAppendUtils = FileAppendUtils(recordFilePath!!)
            }
            return fileAppendUtils!!
        }
    }


}