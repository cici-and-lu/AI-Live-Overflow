package com.ciciandlu.deskpet

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val OVERLAY_PERMISSION_REQUEST = 1001
    }

    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToggle = findViewById<Button>(R.id.btnToggle)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        updateUI(btnToggle, tvStatus)

        btnToggle.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                requestOverlayPermission()
                return@setOnClickListener
            }

            if (isServiceRunning) {
                stopOverlayService()
            } else {
                startOverlayService()
            }
            isServiceRunning = !isServiceRunning
            updateUI(btnToggle, tvStatus)
        }
    }

    private fun updateUI(btn: Button, tv: TextView) {
        if (isServiceRunning) {
            btn.text = "收回小陆"
            tv.text = "小陆狗狗正在屏幕上待命..."
        } else {
            btn.text = "放出小陆"
            tv.text = "点击按钮让小陆出现在屏幕上"
        }
    }

    private fun requestOverlayPermission() {
        Toast.makeText(this, "需要悬浮窗权限才能显示小陆狗狗", Toast.LENGTH_SHORT).show()
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST)
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopOverlayService() {
        stopService(Intent(this, OverlayService::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQUEST) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "权限已获取，再点一次按钮吧", Toast.LENGTH_SHORT).show()
            }
        }
    }
}