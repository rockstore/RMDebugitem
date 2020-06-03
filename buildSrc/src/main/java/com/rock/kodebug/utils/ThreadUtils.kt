package com.rock.kodebug.utils

import java.util.concurrent.Executors
import java.util.concurrent.Future


class ThreadUtils {
    companion object {
        private val threadPool = Executors.newCachedThreadPool()

        fun submit(task: Runnable): Future<*> {
            return threadPool.submit(task)
        }
    }
}