package com.rock.kodebug

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.ide.common.internal.WaitableExecutor
import com.rock.kodebug.utils.FileUtils
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.collections.MutableSet

class KOTransform(val config: Config) : Transform() {

    init {
        println("start kotransform enabled:${config.enabled}, " +
                "enabledWhenDebug:${config.enabledWhenDebug}, " +
                "packageList:${config.packageList?.toString()}," +
                "classesList:${config.classesList}")
    }

    override fun getName() = "KOTrandform"


    val lineNumMap = HashMap<String, String>()

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
        println("=========transform==============")
        val waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()
        transformInvocation!!.inputs.forEach {transformInput ->
            transformInput.directoryInputs.forEach {directoryInput ->
                directoryInput.let {directoryInput ->
                    directoryInput.file.let { file ->
                        val dstPath = transformInvocation.outputProvider.getContentLocation(directoryInput.name
                        , directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                        FileUtils.transform(file.absolutePath, file, dstPath.absolutePath)
                    }
                }
            }

            transformInput.jarInputs.forEach { jarInput ->
                jarInput.let { jarInput ->

                }
            }
        }
    }

}