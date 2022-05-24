package com.smart.app.ui.signstep

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smart.app.R
import com.smart.app.common.GLOBALUID
import com.smart.app.common.PrepareJsonHelper
import com.smart.app.common.SAFEUID
import com.smart.app.common.SP_NOT_FOUND_USER_STRING_CODE
import com.smart.app.myjetcp.MainComposeActivity
import com.smart.app.network.RESTInterface
import com.smart.app.repository.auth.AuthRemoteDataSource
import com.smart.app.repository.auth.AuthRepository
import com.smart.app.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class SignBeforeStartActivity : AppCompatActivity() {
    private lateinit var ediEmails: EditText
    private lateinit var ediPasswords: EditText
    private lateinit var btnForwardLogin: Button
    private lateinit var btnForwardJoin: Button
    private lateinit var tvExploreBegin: TextView
    private lateinit var messageStates: TextView

    @Suppress("JoinDeclarationAndAssignment")
    private val authRepository: AuthRepository

    init {
        this.authRepository = AuthRepository(AuthRemoteDataSource(RESTInterface.create()))
    }

    @Suppress("DuplicatedCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_before_start)

        ediEmails = findViewById(R.id.et_u_email)
        ediPasswords = findViewById(R.id.et_u_pw)
        btnForwardLogin = findViewById(R.id.bt_forward_login)
        btnForwardJoin = findViewById(R.id.bt_forward_join)
        tvExploreBegin = findViewById(R.id.tv_explore_without_sign)
        messageStates = findViewById(R.id.tv_values_do_not_match)

        btnForwardLogin.setOnClickListener {
            btnForwardLogin.isEnabled = false
            var checkCode = 0
            if (TextUtils.isEmpty(getEmailsText()) || TextUtils.isEmpty(getPwsText())) {
                checkCode = -1
            }

            if (checkCode != 0) {
                messageStates.visibility = View.VISIBLE
            } else {
                messageStates.visibility = View.GONE
            }
            when (checkCode) {
                -1 -> messageStates.text = "빈 칸을 모두 채워주세요."
                else -> {
                    doRequestAndGetResult(ediEmails.text.toString(), ediPasswords.text.toString())

                }
            }

            btnForwardLogin.isEnabled = true
        }


        btnForwardJoin.setOnClickListener {
            val uIntent = Intent(this, JoinNormalNewActivity::class.java)
            startActivity(uIntent)
        }

        tvExploreBegin.setOnClickListener {
            val eIntent = Intent(this, MainActivity::class.java)
            startActivity(eIntent)
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun saveUidInfoAtSPSpace(parsedUid: String?) {
        try {
            val prefs: SharedPreferences = getSharedPreferences("UID", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putString("UID", parsedUid)
            editor.commit()
            GLOBALUID = prefs.getString("UID", SP_NOT_FOUND_USER_STRING_CODE)
            if (GLOBALUID == SP_NOT_FOUND_USER_STRING_CODE) {
                GLOBALUID = null
            } else {
                SAFEUID = GLOBALUID!!
            }
            GLOBALUID = parsedUid!!
            SAFEUID = GLOBALUID!!
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun goForwardMainPage() {
        startActivity(Intent(this@SignBeforeStartActivity, MainComposeActivity::class.java))
        finish()
    }

    @Suppress("DuplicatedCode")
    override fun onResume() {
        super.onResume()


        val prefs: SharedPreferences = getSharedPreferences("UID", Context.MODE_PRIVATE)
        GLOBALUID = prefs.getString("UID", SP_NOT_FOUND_USER_STRING_CODE)
        if (GLOBALUID == SP_NOT_FOUND_USER_STRING_CODE) {
            GLOBALUID = null
        } else {
            SAFEUID = GLOBALUID!!
        }

        if (GLOBALUID != null) {
            val intent = Intent(this, MainComposeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getEmailsText(): String {
        return if (this@SignBeforeStartActivity::ediEmails.isInitialized) {
            this@SignBeforeStartActivity.ediEmails.text.toString()
        } else {
            ""
        }
    }

    private fun getPwsText(): String {
        return if (this@SignBeforeStartActivity::ediPasswords.isInitialized) {
            this@SignBeforeStartActivity.ediPasswords.text.toString()
        } else {
            ""
        }
    }

    @Suppress("LiftReturnOrAssignment")
    private fun doRequestAndGetResult(uEmail: String?, uPassword: String?) {


        val jsonObjectString: String = PrepareJsonHelper().prepareLoginUserDataJson(uEmail, uPassword)


        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {

            val response = this@SignBeforeStartActivity.authRepository.checkUserLogin(requestBody)


            withContext(Dispatchers.Main) {
                try {
                    var isLoginResultSuccessful = false


                    if (response.code() == 200) {
                        isLoginResultSuccessful = true
                    } else {
                        // Log.d("FAILNONEXIST", "FAIL417")
                    }
                    if (isLoginResultSuccessful) {
                        messageStates.visibility = View.GONE
                        saveUidInfoAtSPSpace((JSONObject(response.body()!!.string())).getString("targetUid"))
                        goForwardMainPage()
                    } else {
                        messageStates.text = "이메일 또는 비밀번호가 일치하지 않습니다."
                        messageStates.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {

                }
            }
        }
    }


}

// Reference
/*
    @Suppress("LiftReturnOrAssignment")
    private fun sendEmailForward(targetAddress: String?) {


        val retrofit = Retrofit.Builder()
            .baseUrl(MAIL_FORWARDING_SERVER_BASE_URL)
            .build()


        val service = retrofit.create(TESTInterface::class.java)


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

            val response = service.forwardEmail(requestBody)

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
     */