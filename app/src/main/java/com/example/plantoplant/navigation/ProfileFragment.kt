package com.example.plantoplant.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.plantoplant.LoginActivity
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var logoutButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_profile, container, false)

        val userId = arguments?.getString("email") ?: ""
        CoroutineScope(Dispatchers.IO).launch {
            val response = profileUserName(userId)
            val jsons = JSONTokener(response).nextValue() as JSONArray
            withContext(Dispatchers.Main) {
                for (i in 0 until jsons.length()) {
                    val userId = jsons.getJSONObject(i).getString("id")
                }
            }
        }

        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // 로그아웃 처리
            logout()
        }
        return view
    }

    private fun logout() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun profileUserName(user_id: String): String {
        var response = ""

        try {
            val url = URL("http://192.168.163.1:8080/users/info?user_id=$user_id")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.requestMethod = "GET"
            conn.setRequestProperty("Accept", "application/json")
            conn.connect()

            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            response = stringBuilder.toString()
        }
        catch (e: MalformedURLException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        } catch(e: FileNotFoundException){
            e.printStackTrace()
        }
        return response
    }
}