package com.example.plantoplant

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityLoginBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var registerResult = intent.getStringExtra("registerResult")
        if (registerResult == "true") Toast.makeText(this@LoginActivity, "회원 가입 성공" +
                "", Toast.LENGTH_SHORT).show()

        val editEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editPassword: EditText = findViewById(R.id.editTextTextPassword)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signupButton: Button = findViewById(R.id.gotoSignupButton)

        email = editEmail.text.toString()
        password = editPassword.text.toString()

        // 회원 가입 버튼
        signupButton.setOnClickListener {
            val intentSignUp = Intent(this, SignUpActivity::class.java)
            startActivity(intentSignUp)
        }

        // 로그인 버튼
        loginButton.setOnClickListener {
            var loginResult = ""

            email = editEmail.text.toString()
            password = editPassword.text.toString()

            val job = CoroutineScope(Dispatchers.IO).launch {
                val response = async { makeLoginRequest(email, password) }
                loginResult = response.await()
                Log.d(TAG, "함수에서 받은 결과: $loginResult")

                when (loginResult) {
                    "1\n" -> {
                        val inputTextEmail = editEmail.text.toString()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("email", inputTextEmail)
                        startActivity(intent)
                        finish()
                    }
                    "2\n", "3\n" -> {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        Log.d(TAG, "결과 오류")
                    }
                }
            }

            runBlocking {
                job.join()
                job.cancel()
            }
        }
    }

    private fun makeLoginRequest(email: String, password:String): String {
        var response = ""

        try {
            val url = URL("http://223.194.134.71:8080/user/login")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
            jsonObject.put("id", email)
            jsonObject.put("password", password)

            // 서버로 값 전송
            val outStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outStream.write(jsonObject.toString())
            outStream.flush()

            // 서버에서 결과 받기
            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }

            response = stringBuilder.toString()
            Log.d(TAG, "서버에서 받은 결과: $response")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 2000) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
        }
        println("뒤로가기 버튼 클릭")
        super.onBackPressed()
    }
}