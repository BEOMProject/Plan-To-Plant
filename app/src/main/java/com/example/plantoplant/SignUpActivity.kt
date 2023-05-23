package com.example.plantoplant

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class SignUpActivity : AppCompatActivity() {
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordCheck: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton: Button = findViewById(R.id.signupButton)
        val userName: EditText = findViewById(R.id.usernameEdit)
        val emailEdit: EditText = findViewById(R.id.emailEdit)
        val passwordEdit: EditText = findViewById(R.id.passwordEdit)
        val passwordReEdit: EditText = findViewById(R.id.repasswordEdit)
        val mismatchPassword: TextView = findViewById(R.id.mismatchPassword)
        val goToLogin: ImageView = findViewById(R.id.goToLoginButton)

        // 로그인 화면으로 이동
        goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 클릭 시
        signUpButton.setOnClickListener {
            username = userName.text.toString()
            email = emailEdit.text.toString()
            password = passwordEdit.text.toString()
            passwordCheck = passwordReEdit.text.toString()
            var registerResult = ""

            if (password == passwordCheck) {
                mismatchPassword.visibility = TextView.GONE

                val job = CoroutineScope(Dispatchers.IO).launch {
                    val response = async { registerRequest() }
                    registerResult = response.await()

                    when (registerResult) {
                        "1\n" -> {
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            intent.putExtra("registerResult", "true")
                            startActivity(intent)
                            finish()
                        }
                        "2\n" -> {
                            runOnUiThread {
                                Toast.makeText(this@SignUpActivity, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                runBlocking {
                    job.join()
                    job.cancel()
                }

            } else {
                mismatchPassword.visibility = TextView.VISIBLE
            }
        }
    }


    private fun registerRequest():String {
        var response = ""
        try {
            val url = URL("http://192.168.163.1:8080/user/register")
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
            jsonObject.put("nickname", username)

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
                stringBuilder.append(line).append("\n")
            }
            inputStream.close()

            response = stringBuilder.toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return response
    }
}