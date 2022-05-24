package com.smart.app.ui.signstep

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R

class JoinCompleteSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_complete_success)


        findViewById<Button>(R.id.bt_finish_end).setOnClickListener {
            finish()
        }
    }
}