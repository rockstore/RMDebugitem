package com.rock.kodebug.asm

import com.rock.kodebug.utils.FileAppendUtils
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
//         super.visitLocalVariable(name, descriptor, signature, start, end, index)
        // 记录 class 文件内部 方法局部变量和源文件之间的对应信息
    }

    override fun visitLineNumber(line: Int, start: Label?) {
//         super.visitLineNumber(line, start)
        // 记录指令行和源代码之间的对应关系
        // 写入行号及偏移信息
        println("-------method line number:${line}--${start!!.offset}")
        FileAppendUtils.getInstance().append("${line}--${start!!.offset}", true)
    }
}