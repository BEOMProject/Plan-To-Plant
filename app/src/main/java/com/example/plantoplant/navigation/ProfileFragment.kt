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
import kotlinx.coroutines.*
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("email") ?: ""
        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = async { profileUserName(userId) }
            val userNickname = response.await()
            print(userNickname)

            binding.userNameText.text = userNickname
        }

        runBlocking {
            job.join()
            job.cancel()
        }

        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // 로그아웃 처리
            logout()
        }
    }

    private fun logout() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun profileUserName(user_id: String): String {
        var response = ""

        try {
            val url = URL("http://223.194.130.163:8080/user/info?uid=$user_id")
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
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return response
    }
}