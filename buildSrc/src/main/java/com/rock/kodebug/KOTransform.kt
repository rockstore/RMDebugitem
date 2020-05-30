package com.rock.kodebug

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.rock.kodebug.utils.FileAppendUtils
import com.rock.kodebug.utils.FileUtils
import java.util.jar.JarFile
import kotlin.collections.MutableSet

class KOTransform() : Transform() {
    init {
        println("start kotransform enabled:${Config.getInstance().enabled}, " +
                "enabledWhenDebug:${Config.getInstance().enabledWhenDebug}, " +
                "packageList:${Config.getInstance().packageList?.toString()}," +
                "classesList:${Config.getInstance().classesList}")
        FileAppendUtils.recordFilePath = Config.getInstance().recordFilePath
    }

    override fun getName() = "KOTrandform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        val set = HashSet<QualifiedContent.ContentType>()
        set.add(QualifiedContent.DefaultContentType.CLASSES)
        return set
    }

    override fun isIncremental() = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        val set = HashSet<QualifiedContent.Scope>()
        set.add(QualifiedContent.Scope.PROJECT)
        set.add(QualifiedContent.Scope.SUB_PROJECTS)
        return set
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        println("=========start transform==============")
        FileAppendUtils.getInstance().openStream()
        // TODO 计划使用多线程降低 transform 的时间
        transformInvocation!!.inputs.forEach {transformInput ->

            transformInput.directoryInputs.forEach {directoryInput ->
                directoryInput.let {directoryInput ->
                    directoryInput.file.let { file ->
                        val dstFile = transformInvocation.outputProvider.getContentLocation(directoryInput.name
                        , directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                        FileUtils.transformClz(file.absolutePath, file, dstFile.absolutePath)
                    }
                }
            }

            transformInput.jarInputs.forEach { jarInput ->
                jarInput.let { jarInput ->
                    jarInput.file.let {file ->
                        val dstFile = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR)
                        FileUtils.transformJar(JarFile(file), dstFile.absolutePath)
                    }
                }
            }
        }
        // transform 结束后 close
        FileAppendUtils.getInstance().close()
    }

}