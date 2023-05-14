package com.example.plantoplant

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityLoginBinding
import com.example.plantoplant.navigation.CalenderFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editEmail: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editPassword: EditText = findViewById(R.id.editTextTextPassword)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signupButton: Button = findViewById(R.id.gotoSignupButton)

        // 회원가입 버튼
        signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        loginButton.setOnClickListener {
            email = editEmail.text.toString()
            password = editPassword.text.toString()
            LoginTask()
        }
    }

    private fun LoginTask() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val url = URL("http://59.7.106.96:8080/user/login")
                val conn = url.openConnection() as HttpURLConnection
                conn.defaultUseCaches = false
                conn.doInput = true
                conn.doOutput = true
                conn.setRequestMethod("POST")
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
                var line: String? = null
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append('\n')
                }

                val response = stringBuilder.toString()
                Log.d(TAG, "서버에서 받은 결과: $response")

                if ("$response" == "1") {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                } else if ("$response" == "2" || "$response" == "3") {
            Toast.makeText(applicationContext, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
        }

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
}