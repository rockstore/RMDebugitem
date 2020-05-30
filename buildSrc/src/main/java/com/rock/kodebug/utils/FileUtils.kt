package com.rock.kodebug.utils

import java.io.File

class FileUtils {
    companion object {
        /**
         * 向文件追加内容
         * @param file
         *      被追加的文件
         * @param content
         *      追加的内容
         * @param newLine
         *      是否新开一行
         * */
        fun append(file: File, content: String, newLine: Boolean) {

        }

        /**
         * 向文件追加内容
         * @param file
         *      被追加内容的文件
         * @param array
         *      追加的字符串数组
         * @param newLine
         *      是否新开一行
         * @param erase
         *      是否擦出原来的内容
         * */
        fun append(file: File, array: Array<String>, newLine: Boolean, erase: Boolean) {
            if(erase) {

            }
            file?.let {
                var sb = StringBuilder()
                array?.forEach {
                    sb.append(it + File.separator)
                }
                val tmp = sb.toString()
                val content = tmp.subSequence(0, tmp.length - File.separator.length)

            }
        }

        /**
         * 修改 class 文件，去掉 debug 信息
         * */
        fun transform(file: File, dstDir: File) {

        }

    }
}