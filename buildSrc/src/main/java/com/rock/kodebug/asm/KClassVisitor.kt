package com.rock.kodebug.asm

import com.rock.kodebug.utils.FileAppendUtils
import org.objectweb.asm.ClassVisitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM5

class KClassVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        // 写入类信息
        FileAppendUtils.getInstance().append(name!!, true)
        println("visit class:${name}")
    }
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val superVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return KMethodVisitor(ASM5, superVisitor)
    }
}