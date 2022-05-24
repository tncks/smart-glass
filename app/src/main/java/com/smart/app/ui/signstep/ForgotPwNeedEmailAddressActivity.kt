package com.smart.app.ui.signstep

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R
import com.smart.app.network.TESTInterface
import com.smart.app.repository.credential.CredentialRemoteDataSource
import com.smart.app.repository.credential.CredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ForgotPwNeedEmailAddressActivity : AppCompatActivity() {

    @Suppress("JoinDeclarationAndAssignment")
    private val credentialRepository: CredentialRepository

    init {
        this.credentialRepository = CredentialRepository(CredentialRemoteDataSource(TESTInterface.create()))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pw_need_email_address)

        findViewById<Button>(R.id.btn_next_step).setOnClickListener {

            sendEmailForward(findViewById<EditText>(R.id.tv_user_email_address_to_resetpw).text.toString())

        }
    }

    @Suppress("LiftReturnOrAssignment", "DuplicatedCode")
    private fun sendEmailForward(targetAddress: String?) {


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

            val response = this@ForgotPwNeedEmailAddressActivity.credentialRepository.forwardEmail(requestBody)

            withContext(Dispatchers.Main) {
                try {
                    val resultIntent: Intent
                    if (response.isSuccessful) {
                        Log.d("TES", "TESSUCCESS")
                        resultIntent = Intent(applicationContext, ForgotPwConfirmEmailCheckValidActivity::class.java)
                    } else {
                        Log.d("TES", "TESFAILURE")
                        resultIntent = Intent(applicationContext, ForgotPwConfirmEmailCheckValidActivity::class.java)
                    }
                    resultIntent.putExtra("targetedAdd", targetAddress)
                    startActivity(resultIntent)
                } finally {
                    // finish()
                }
            }
        }
    }
}

