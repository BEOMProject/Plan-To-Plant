package com.example.plantoplant

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.plantoplant.databinding.ActivitySignupBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity(){
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        val signupOkButton: Button = findViewById(R.id.button2)
        val userName: EditText = findViewById(R.id.editTextTextPersonName2)
        val emailEdit: EditText = findViewById(R.id.editTextTextEmailAddress2)
        val passwordEdit: EditText = findViewById(R.id.editTextTextPassword2)
        val passwordReEdit: EditText = findViewById(R.id.editTextTextPassword3)
         */


        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        signupOkButton.setOnClickListener {
            val username = userName.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            val passwordCheck = passwordReEdit.text.toString()

            val signupTask = SignUpTask(username, email, password)
            signupTask.execute()
        }
 */
    }
}
/*
class SignUpTask(private val username:String, private val email: String, private val password: String) : AsyncTask<Void, Void, String>() {
    /*
    override fun onPreExecute() {
        super.onPreExecute()
    }
     */

    override fun doInBackground(vararg p0: Void?): String {
        val url = URL("http://localhost:8080/signup")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true

        val postData = "username=$username + email=$email + password=$password"

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
 */