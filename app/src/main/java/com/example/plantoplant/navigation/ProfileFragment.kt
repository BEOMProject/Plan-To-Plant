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
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var logoutButton: Button
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("email") ?: ""
        loadUserName(userId)

        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun loadUserName(userId: String) {
        Thread {
            try {
                val response = profileUserName(userId)
                activity?.runOnUiThread {
                    updateUserName(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun updateUserName(userNickname: String) {
        binding.userNameText.text = userNickname
        userName = userNickname
        binding.userNameText.text = userName
    }

    private fun logout() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun profileUserName(userId: String): String {
        var response = ""

        try {
            val url = URL("http://125.142.56.47:8080/user/info?uid=$userId")
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}