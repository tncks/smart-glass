package com.smart.app.ui.signstep

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R

class JoinIncompleteInvalidorfailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_incomplete_invalidorfail)


        findViewById<Button>(R.id.bt_retry_back).setOnClickListener {
            finish()
            startActivity(Intent(this, SignBeforeStartActivity::class.java))
        }
    }
}