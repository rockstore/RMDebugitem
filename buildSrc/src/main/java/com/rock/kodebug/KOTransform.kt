package com.rock.kodebug

import com.android.build.api.transform.*
import com.rock.kodebug.utils.FileAppendUtils
import com.rock.kodebug.utils.FileUtils
import com.rock.kodebug.utils.ThreadUtils
import java.util.concurrent.Future
import java.util.jar.JarFile
import kotlin.collections.MutableSet

class KOTransform(private val isFirstBuild: Boolean) : Transform() {
    init {
        println("start kotransform enabled:${Config.getInstance().enabled}, " +
                "enabledWhenDebug:${Config.getInstance().enabledWhenDebug}, " +
                "packageList:${Config.getInstance().packageList?.toString()}," +
                "classesList:${Config.getInstance().classesList}")
        FileAppendUtils.recordFilePath = Config.getInstance().recordFilePath
        FileAppendUtils.getInstance()
    }

    override fun getName() = "KOTrandform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        val set = HashSet<QualifiedContent.ContentType>()
        set.add(QualifiedContent.DefaultContentType.CLASSES)
        return set
    }

    /**
     * 支持增量编译，支持增量编译的前提是已经编译过（本地存在被编译过的文件）且此函数返回 true
     * */
    override fun isIncremental() = true

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        val set = HashSet<QualifiedContent.Scope>()
        set.add(QualifiedContent.Scope.PROJECT)
        set.add(QualifiedContent.Scope.SUB_PROJECTS)
        return set
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        println("=========start transform==============")
        FileAppendUtils.getInstance().openStream()
        val startTime = System.currentTimeMillis()
        transformInvocation!!.inputs.forEach {transformInput ->

            val directoryTransformFuture = ThreadUtils.submit(Runnable {
                transformInput.directoryInputs.forEach {directoryInput ->
                    directoryInput.let {directoryInput ->
                        // 输出目录
                        val dstFile = transformInvocation.outputProvider.getContentLocation(directoryInput.name
                            , directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                        /**
                         * 如果是第一次编译（包括第一次编译以及 clean 后执行第一次编译），
                         * directoryInput.changedFiles 为空，这种场景需要进行区分
                         */
                        if (!isFirstBuild && isIncremental) {
                            directoryInput.changedFiles.entries.forEach {
                                val file = it.key
                                val status = it.value
                                /**
                                 * 经过测试，在文件被删除后，文件的状态会出现 Status.REMOVED, 故只处理 Status.ADDED
                                 * 和 Status.CHANGED 两种状态的文件
                                 */
                                if (status == Status.ADDED || status == Status.CHANGED) {
                                    FileUtils.transformClz(directoryInput.file.absolutePath, file, dstFile.absolutePath)
                                }
                            }
                        } else {
                            // 不支持增量编译，所有文件都需要被转换
                            directoryInput.file.let { file ->
                                FileUtils.transformClz(file.absolutePath, file, dstFile.absolutePath)
                            }
                        }
                    }
                }
            })

            val jarTransformFuture = ThreadUtils.submit(Runnable {
                transformInput.jarInputs.forEach { jarInput ->
                    jarInput.let { jarInput ->
                        jarInput.file.let {file ->
                            val dstFile = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR)
                            FileUtils.transformJar(JarFile(file), dstFile.absolutePath)
                        }
                    }
                }
            })
            // 等待转换完成
            directoryTransformFuture.get()
            jarTransformFuture.get()
        }
        val endTime = System.currentTimeMillis();
        println("transfor-duration:${endTime - startTime}")
        // transform 结束后 close
        FileAppendUtils.getInstance().close()
    }

}