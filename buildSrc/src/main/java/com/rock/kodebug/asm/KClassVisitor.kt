package com.rock.kodebug.asm

import org.objectweb.asm.ClassVisitor

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM5

class KClassVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
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