package com.example.plantoplant

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.plantoplant.databinding.ActivitySignupBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signupOkButton: Button = findViewById(R.id.signupButton)
        val userName: EditText = findViewById(R.id.usernameEdit)
        val emailEdit: EditText = findViewById(R.id.emailEdit)
        val passwordEdit: EditText = findViewById(R.id.passwordEdit)
        val passwordReEdit: EditText = findViewById(R.id.repasswordEdit)
        val gotoLogin: ImageView = findViewById(R.id.goToLoginButton)
        val mismatchPassword: TextView = findViewById(R.id.mismatchPassword)

        // 회원가입 버튼
        gotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupOkButton.setOnClickListener {
            val username = userName.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            val passwordCheck = passwordReEdit.text.toString()

            if (password == passwordCheck) {
                binding.mismatchPassword.visibility = View.GONE
                val signupTask = SignUpTask(username, email, password)
                signupTask.execute()
            }
            else {
                binding.mismatchPassword.visibility = View.VISIBLE
            }
        }
    }
}

class SignUpTask(private val username:String, private val email: String, private val password: String) : AsyncTask<Void, Void, String>() {
    /*
    override fun onPreExecute() {
        super.onPreExecute()
    }
     */

    override fun doInBackground(vararg p0: Void?): String {
        val url = URL("http://localhost:8080/user/register")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true

        val postData = "username=$username&email=$email&password=$password"

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
}