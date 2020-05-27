package com.rock.kodebug

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation

class KOTransform(private var whiteList: Array<String>?) : Transform() {
    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isIncremental(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        println("=============transform==========")
    }

}