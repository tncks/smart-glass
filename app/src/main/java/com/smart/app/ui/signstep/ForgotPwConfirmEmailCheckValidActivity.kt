package com.smart.app.ui.signstep

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R
import com.smart.app.network.TESTInterface
import com.smart.app.repository.credential.CredentialRemoteDataSource
import com.smart.app.repository.credential.CredentialRepository
import com.smart.app.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ForgotPwConfirmEmailCheckValidActivity : AppCompatActivity() {

    @Suppress("JoinDeclarationAndAssignment")
    private val credentialRepository: CredentialRepository

    init {
        this.credentialRepository = CredentialRepository(CredentialRemoteDataSource(TESTInterface.create()))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pw_confirm_email_check_valid)


        val relayedDataOfUser: String? = intent.getStringExtra("targetedAdd")
        val relayedDisplayingEmailTv = findViewById<TextView>(R.id.tv_user_email_address_to_resetpw)
        val reSendAgainBtn = findViewById<Button>(R.id.btn_next_step_again)

        if (relayedDataOfUser.isNullOrBlank()) {
            val erMessageT = "이메일 정보가 존재하지 않습니다."
            relayedDisplayingEmailTv.text = erMessageT
            reSendAgainBtn.isEnabled = false
            reSendAgainBtn.alpha = 0.5f
            findViewById<TextView>(R.id.tv_notice_sent_message_code).visibility = View.INVISIBLE
        } else {
            relayedDisplayingEmailTv.text = relayedDataOfUser

            reSendAgainBtn.setOnClickListener {

                findViewById<TextView>(R.id.tv_notice_sent_message_code).text = "인증메일이 정상적으로 다시 발송되었습니다."
                sendEmailForwardAgain(relayedDataOfUser)
            }
        }


    }

    @Suppress("LiftReturnOrAssignment", "DuplicatedCode")
    private fun sendEmailForwardAgain(targetAddress: String?) {

        val jsonObject = JSONObject()


        if (!targetAddress.isNullOrBlank()) {
            val consKeyNameFix = "targetm"
            jsonObject.put(
                consKeyNameFix,
                targetAddress
            )
        } else {
            return
        }

        val jsonObjectString: String = jsonObject.toString()


        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        CoroutineScope(Dispatchers.IO).launch {

            val response = this@ForgotPwConfirmEmailCheckValidActivity.credentialRepository.forwardEmail(requestBody)

            // 급하진 않아서 나중에 시간 남으면 수정
            // 이부분 인텐트 넘겨주고 다른 액티비티 시작하는 코드 대충 짜놓음 -> 수정 해야 됨 일단은 아무 액티비티 실행되게 해놓음
            withContext(Dispatchers.Main) {
                try {
                    val resultIntent: Intent
                    if (response.isSuccessful) {
                        Log.d("TES", "TESSUCCESS")
                        resultIntent = Intent(applicationContext, MainActivity::class.java)
                    } else {
                        Log.d("TES", "TESFAILURE")
                        resultIntent = Intent(applicationContext, SignBeforeStartActivity::class.java)
                    }
                    // resultIntent.putExtra("targetedAdd", targetAddress)
                    startActivity(resultIntent)
                } catch (e: Exception) {
                    finish()
                } finally {
                    // finish()
                    finishAndRemoveTask()
                }
            }
        }
    }
}