package com.example.plantoplant

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.plantoplant.databinding.ActivityLoginBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
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
/*
        // 로그인 버튼
        loginButton.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            val loginTask = LoginTask(email, password)
            loginTask.execute()

            // Main으로 이동 : 1일 때 성공, 2, 3일 때 실패
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
 */
}
/*
class LoginTask(private val email: String, private val password: String) : AsyncTask<Void, Void, String>() {
    /*
    override fun onPreExecute() {
        super.onPreExecute()
    }
     */

    override fun doInBackground(vararg p0: Void?): String {
        val url = URL("http://localhost:8080/user/login")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true

        val postData = "email=$email + password=$password"

        val writer = OutputStreamWriter(conn.outputStream)
        writer.write(postData)
        writer.flush()

        val reader = BufferedReader(InputStreamReader(conn.inputStream))
        val response = StringBuffer()
        var line: String?
        while (reader.readLine().also {line = it} != null) {
            response.append(line)
        }
        reader.close()

        return response.toString()
    }
    /*
    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
    }
     */

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
    /*
    override fun onCancelled(result: Boolean?) {
        super.onCancelled(result)
    }
     */
*/
}