package com.rock.kodebug

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.rock.kodebug.utils.FileUtils
import kotlin.collections.MutableSet

class KOTransform(val config: Config) : Transform() {

    override fun getName() = "KOTrandform"
    val lineNumMap = HashMap<String, String>()

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        val set = HashSet<QualifiedContent.ContentType>()
        set.add(QualifiedContent.DefaultContentType.CLASSES)
        return set
    }

    override fun isIncremental() = true

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        val set = HashSet<QualifiedContent.Scope>()
        set.add(QualifiedContent.Scope.PROJECT)
        set.add(QualifiedContent.Scope.SUB_PROJECTS)
        return set
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        println("=========transform==============")
        transformInvocation!!.inputs.forEach {transformInput ->
            transformInput.directoryInputs.forEach {directoryInput ->
                directoryInput.let {directoryInput ->
                    directoryInput.file.let { file ->
                        file.listFiles().let {fileArray ->
                            fileArray.forEach {
                                val dstDir = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                                println("dir_file:" + it.absolutePath + "-" + dstDir.absolutePath)

                            }
                        }
                    }
                }
            }
        }
    }

}