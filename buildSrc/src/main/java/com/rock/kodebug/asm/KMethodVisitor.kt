package com.rock.kodebug.asm

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor

/**
 * class visitor，根据配置删除方法的调试信息，同时将调试信息保存的本地
 * */
class KMethodVisitor(api: Int, methodVisitor: MethodVisitor?) : MethodVisitor(api, methodVisitor) {

    override fun visitLocalVariable(
        name: String?,
        descriptor: String?,
        signature: String?,
        start: Label?,
        end: Label?,
        index: Int
    ) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index)
    }

    override fun visitLineNumber(line: Int, start: Label?) {
        super.visitLineNumber(line, start)
    }
}