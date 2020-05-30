package com.rock.kodebug.asm

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
        println("visit class:${name}")
    }
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("-------visit_method:${name}-${descriptor}")
        val superVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        return KMethodVisitor(ASM5, superVisitor)
    }
}