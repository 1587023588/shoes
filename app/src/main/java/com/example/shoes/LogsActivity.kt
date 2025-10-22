package com.example.shoes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class LogsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        val tvCrash = findViewById<TextView>(R.id.tvCrash)
        val tvBreadcrumb = findViewById<TextView>(R.id.tvBreadcrumb)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        val btnShare = findViewById<Button>(R.id.btnShare)

        fun readLogs(): Pair<String, String> {
            val dir = File(filesDir, "logs")
            val crash = File(dir, "last_crash.txt").takeIf { it.exists() }?.readText()
                ?: "(暂无崩溃日志)"
            val bread = File(dir, "scan_breadcrumbs.txt").takeIf { it.exists() }?.readText()
                ?: "(暂无面包屑日志)"
            return crash to bread
        }

        fun refresh() {
            val (crash, bread) = readLogs()
            tvCrash.text = crash
            tvBreadcrumb.text = bread
        }

        fun shareAll() {
            val (crash, bread) = readLogs()
            val content = StringBuilder().apply {
                appendLine("=== last_crash.txt ===")
                appendLine(crash)
                appendLine()
                appendLine("=== scan_breadcrumbs.txt ===")
                appendLine(bread)
            }.toString()
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Shoes 日志导出")
                putExtra(Intent.EXTRA_TEXT, content)
            }
            startActivity(Intent.createChooser(i, "分享日志"))
        }

        btnRefresh.setOnClickListener { refresh() }
        btnShare.setOnClickListener { shareAll() }

        refresh()
    }
}
