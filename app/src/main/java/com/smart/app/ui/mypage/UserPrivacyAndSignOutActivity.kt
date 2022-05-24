package com.smart.app.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R
import com.smart.app.ui.signstep.SignBeforeStartActivity

class UserPrivacyAndSignOutActivity : AppCompatActivity() {
    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_privacy_and_sign_out)


        // 로그아웃 SP 처리 알고리즘이 잘못되서 수정 필요
        findViewById<TextView>(R.id.tv_logout).setOnClickListener {
            val prefs: SharedPreferences = getSharedPreferences("UID", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.remove("UID")
            editor.commit() // do not use apply extension function, must use commit

            val clearingIntent = Intent(this, SignBeforeStartActivity::class.java)
            finishAffinity()
            startActivity(clearingIntent)
        }
    }
}