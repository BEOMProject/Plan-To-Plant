package com.example.plantoplant

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    val editEmail : EditText = findViewById(R.id.editTextTextEmailAddress)
    val editPassword : EditText = findViewById(R.id.editTextTextPassword)
    val loginButton : Button = findViewById(R.id.loginButton)
    var getemail:String = ""
    var getpassword:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        loginButton.setOnClickListener {
            getemail = editEmail.text.toString()
            getpassword = editPassword.text.toString()
            var url : String  = "http://localhost:8080/login"

            val networkTask = NetworkTask(url, null)
            networkTask.execute()
        }
         */
    }
}

/*
class NetworkTask(url : String, values : ContentValues) : AsyncTask<Void, Int, Boolean>() {
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }

    override fun onCancelled(result: Boolean?) {
        super.onCancelled(result)
    }

}
 */