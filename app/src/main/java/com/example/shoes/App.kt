package com.example.shoes

import android.app.Application
import android.util.Log
import java.io.File

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            try {
                val dir = File(filesDir, "logs"); if (!dir.exists()) dir.mkdirs()
                val f = File(dir, "last_crash.txt")
                f.writeText("Thread: ${t.name}\n${Log.getStackTraceString(e)}")
            } catch (_: Throwable) {}
        }
    }
}
