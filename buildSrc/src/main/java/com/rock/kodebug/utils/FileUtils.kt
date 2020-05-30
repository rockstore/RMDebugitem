package com.rock.kodebug.utils

import com.rock.kodebug.Config
import com.rock.kodebug.asm.KClassVisitor
import org.gradle.api.GradleException
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class FileUtils {
    companion object {

        fun isDir(file: File) = file!= null && file.isDirectory

        fun isClassFile(file: File) = file.absolutePath.endsWith(".class")

        fun isClassFile(filePath: String) = filePath?.endsWith(".class")

        fun acceptFile(filePath: String): Boolean {
            // 去除 R 文件
            if (filePath.contains("R$")
                || filePath.endsWith("R.class")) {
                return false
            }
            // 在待排除的包中
            val lastPreIndex = filePath.lastIndexOf(File.separator)
            val prePath = filePath.subSequence(0, lastPreIndex)
            Config.getInstance().packageList.forEach {
                if (prePath.lastIndexOf(it) != -1) {
                    return false
                }
            }
            // 在待排除的类中
            val lastDotIndex = filePath.lastIndexOf(".")
            val preFilePath = filePath.subSequence(0, lastDotIndex)
            Config.getInstance().classesList.forEach {
                if (preFilePath.lastIndexOf(it) != -1) {
                    return false
                }
            }
            return true
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
            } else {
                val bytes = IoUtils.slurpBytes(file)
                var retBytes = bytes
                val fileName = file.name
                val srcPackage = file.absolutePath.substring(originDirPath.length, file.absolutePath.length - fileName.length - 1)
                val dstFileDir = File(dstDir + File.separator +  srcPackage)
                if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
                    throw GradleException("can not create dst dir")
                }
                // 如果是 class 文件才进行转换
                if (isClassFile(file) && acceptFile(file.absolutePath)) {
                    val cr = ClassReader(bytes)
                    val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val cv = KClassVisitor(Opcodes.ASM5, cw)
                    cr.accept(cv, ClassReader.EXPAND_FRAMES)
                    retBytes = cw.toByteArray()
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
                if (!jarEntry.isDirectory) {
                    if (isClassFile(jarEntry.name) && acceptFile(jarEntry.name)) {
                        val cr = ClassReader(bytes)
                        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
                        val cv = KClassVisitor(Opcodes.ASM5, cw)
                        cr.accept(cv, ClassReader.EXPAND_FRAMES)
                        retBytes = cw.toByteArray()
                    }
                    val retByteArrayInputStream = ByteArrayInputStream(retBytes)
                    IoUtils.copy(retByteArrayInputStream, jarOutputStream, false)

                }
            }
            jarOutputStream.close()
        }

    }
}