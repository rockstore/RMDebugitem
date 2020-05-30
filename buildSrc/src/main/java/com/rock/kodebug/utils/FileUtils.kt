package com.rock.kodebug.utils

import com.rock.kodebug.asm.KClassVisitor
import org.gradle.api.GradleException
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class FileUtils {
    companion object {

        fun isDir(file: File) = file!= null && file.isDirectory

        fun isClassFile(file: File) = file.absolutePath.endsWith(".class")

        fun isClassFile(filePath: String) = filePath?.endsWith(".class")
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
         * @param file      被 transform 的文件
         * @param dstDir    目的地址
         * */
        fun transformClz(originDirPath: String, file: File, dstDir: String) {
            if (file == null || !file.exists()) {
                return
            }
            if (file.isDirectory) {
                // 如果传递的是 directory 则递归转换
                file.listFiles().forEach {
                    transformClz(originDirPath, it, dstDir)
                }
            } else if (FileUtils.isClassFile(file)) {
                val bytes = IoUtils.slurpBytes(file)
                val cr = ClassReader(bytes)
                val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
                val cv = KClassVisitor(Opcodes.ASM5, cw)
                cr.accept(cv, ClassReader.EXPAND_FRAMES)
                val retBytes = cw.toByteArray()
                val fileName = file.name
                val srcPackage = file.absolutePath.substring(originDirPath.length, file.absolutePath.length - fileName.length - 1)
                val dstFileDir = File(dstDir + File.separator +  srcPackage)
                if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
                    throw GradleException("can not create dst dir")
                }
                val dstFile = File(dstFileDir, fileName)
                IoUtils.copy(ByteArrayInputStream(retBytes), FileOutputStream(dstFile), true)
            }
        }

        fun transformJar(inputJar: JarFile, outputJarPath: String) {
            val jarOutputStream = JarOutputStream(BufferedOutputStream(FileOutputStream(File(outputJarPath))))
            inputJar.entries().toList().forEach {jarEntry ->
                val newJarEntry = JarEntry(jarEntry.name)
                newJarEntry.setTime(jarEntry.time);
                jarOutputStream.putNextEntry(newJarEntry);
                val inputStream = inputJar.getInputStream(jarEntry)
                val bytes = IoUtils.slurpBytes(inputStream)
                var retBytes = bytes
                if (!jarEntry.isDirectory && isClassFile(jarEntry.name)) {
                    val cr = ClassReader(bytes)
                    val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val cv = KClassVisitor(Opcodes.ASM5, cw)
                    cr.accept(cv, ClassReader.EXPAND_FRAMES)
                    retBytes = cw.toByteArray()
                    val retByteArrayInputStream = ByteArrayInputStream(retBytes)
                    IoUtils.copy(retByteArrayInputStream, jarOutputStream, false)

                }
            }
            jarOutputStream.close()
        }

    }
}