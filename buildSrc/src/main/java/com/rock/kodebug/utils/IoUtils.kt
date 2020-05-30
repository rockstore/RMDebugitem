package com.rock.kodebug.utils

import java.io.*


object IoUtils {
    const val DEFAULT_BUFFER_SIZE = 1024
    @Throws(IOException::class)
    fun copy(
        input: InputStream,
        output: OutputStream,
        closeStreams: Boolean
    ): Int {
        return copy(input, output, DEFAULT_BUFFER_SIZE, closeStreams)
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun copy(
        input: InputStream,
        output: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        closeStreams: Boolean = false
    ): Int {
        return try {
            val buffer = ByteArray(bufferSize)
            var count = 0
            var n = 0
            while (-1 != input.read(buffer).also { n = it }) {
                output.write(buffer, 0, n)
                count += n
            }
            count
        } finally {
            if (closeStreams) {
                input.close()
                output.close()
            }
        }
    }

    @Throws(IOException::class)
    fun slurpBytes(`in`: InputStream): ByteArray {
        val out = ByteArrayOutputStream()
        return try {
            copy(`in`, out)
            out.flush()
            out.toByteArray()
        } finally {
            out.close()
        }
    }

    @Throws(IOException::class)
    fun slurpBytes(file: File?): ByteArray? {
        return if (!FileUtils.isDir(file!!)) {
            slurpBytes(FileInputStream(file))
        } else {
            null
        }
    }

    @Throws(IOException::class)
    fun slurpBytes(path: String?): ByteArray? {
        return slurpBytes(File(path))
    }

    @Throws(IOException::class)
    fun copyBytesToFile(file: File?, newBytes: ByteArray?) {
        val oStream: OutputStream = FileOutputStream(file)
        try {
            copy(ByteArrayInputStream(newBytes), oStream, true)
        } finally {
            oStream.close()
        }
    }

    fun closeQuietly(vararg inputStreams: InputStream) {
        try {
            if (inputStreams != null && inputStreams.size > 0) {
                for (`is` in inputStreams) {
                    `is`.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeQuietly(vararg outputStreams: OutputStream) {
        try {
            if (outputStreams != null && outputStreams.size > 0) {
                for (os in outputStreams) {
                    os.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
