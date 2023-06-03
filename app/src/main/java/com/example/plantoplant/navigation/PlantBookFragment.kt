package com.example.plantoplant.navigation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plantoplant.R
import com.example.plantoplant.databinding.FragmentPlantbookBinding
import com.example.plantoplant.util.ServerCon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates


class PlantBookFragment : Fragment() {

    private var _binding: FragmentPlantbookBinding? = null
    private val binding get() = _binding!!

    private lateinit var test: ImageButton
    private lateinit var test1: ImageButton
    private lateinit var test2: ImageButton
    private lateinit var test3: ImageButton
    private lateinit var test4: ImageButton
    private lateinit var test5: ImageButton
    private lateinit var testShadow: ImageView
    private lateinit var test1Shadow: ImageView
    private lateinit var test2Shadow: ImageView
    private lateinit var test3Shadow: ImageView
    private lateinit var test4Shadow: ImageView
    private lateinit var test5shadow: ImageView

    private lateinit var flowerName: String
    private lateinit var userId: String
    private var plantId by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantbookBinding.inflate(inflater, container, false)
        userId = arguments?.getString("email") ?: ""
        val view = inflater.inflate(R.layout.fragment_plantbook, container, false)

        test = view.findViewById(R.id.cherryblossom)
        test1 = view.findViewById(R.id.forsythia)
        test2 = view.findViewById(R.id.fishbread)
        test3 = view.findViewById(R.id.tulip)
        //test4 = view.findViewById(R.id.tulip)
        testShadow = view.findViewById(R.id.cherryblossomShadow)
        test1Shadow = view.findViewById(R.id.forsythiaShadow)
        test2Shadow = view.findViewById(R.id.fishbreadShadow)
        test3Shadow = view.findViewById(R.id.tulipShadow)
        //test4Shadow = view.findViewById(R.id.tulipShadow)


        val job = CoroutineScope(Dispatchers.IO).launch{
            val response = getObtainedPlants(userId)
            parseJson(response)
        }
        runBlocking {
            job.join()
            job.cancel()
        }


        test.setOnClickListener {
            println("벚꽃 클릭됨")
            showPlantInfo("Cherry Blossom")
        }
        test1.setOnClickListener {
            println("개나리 클릭됨")
            showPlantInfo("Forsythia")
        }
        test2.setOnClickListener {
            println("붕어빵 클릭됨")
            showPlantInfo("Fishbread")
        }
        test3.setOnClickListener {
            println("튤립 클릭됨")
            showPlantInfo("Tulip")
        }


        return view
    }

    /*private fun getPlantsData(plantId: Int): PlantInfo? {
        var plantInfo: PlantInfo? = null

        try {
            val con = ServerCon()
            val url = URL(con.url + "plant/information?name=$flowerName")
            val conn = url.openConnection() as HttpURLConnection
            conn.defaultUseCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val jsonObject = JSONObject()
            jsonObject.put("name", flowerName)

            val outputStream = OutputStreamWriter(conn.outputStream, "UTF-8")
            outputStream.write(jsonObject.toString())
            outputStream.flush()

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = conn.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                inputStream.close()

                val response = stringBuilder.toString()

                val plantJsonArray = JSONArray(response)
                if (plantJsonArray.length() > 0) {
                    val plantJsonObject = plantJsonArray.getJSONObject(0)
                    val plantName = plantJsonObject.getString("name")
                    val plantDescription = plantJsonObject.getString("description")

                    // 가져온 정보를 PlantInfo 객체로 변환
                    plantInfo = PlantInfo(plantId, plantName, plantDescription)
                    println("서버 응답: $plantInfo")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return plantInfo
    }

     */

    private fun getObtainedPlants(userId: String): String {
        var response = ""

        try {
            val con = ServerCon()
            val url = URL(con.url + "garden/user?user_id=$userId")
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
            println("서버 응답: $response")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    private fun showPlantInfo(plantName: String) {
        flowerName = plantName
        val dialogView = layoutInflater.inflate(R.layout.dialog_plant_info, null)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        val titleTextView = dialogView.findViewById<TextView>(R.id.plantNameTextView)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.plantDescriptionTextView)

        val titleResId = when (plantName) {
            "Cherry Blossom" -> R.string.cherryBlossom
            "Forsythia" -> R.string.forsythia
            "Fishbread" -> R.string.fishbread
            "Tulip" -> R.string.tulip
            else -> 0
        }

        val descriptionResId = when (plantName) {
            "Cherry Blossom" -> R.string.cherryBlossomInfo
            "Forsythia" -> R.string.forsythiaInfo
            "Fishbread" -> R.string.fishbreadInfo
            "Tulip" -> R.string.tulipInfo
            else -> 0
        }

        if (titleResId != 0) {
            titleTextView.text = getString(titleResId)
        } else {
            titleTextView.text = ""
        }

        if (descriptionResId != 0) {
            descriptionTextView.text = getString(descriptionResId)
        } else {
            descriptionTextView.text = ""
        }
    }

    private fun parseJson(json: String): ArrayList<PlantBooks> {
        val result = ArrayList<PlantBooks>()

        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.getInt("id")
                val plantName = jsonObject.getString("plantName")
                val count = jsonObject.getInt("count")
                val isFound = jsonObject.getBoolean("found")

                when (plantName) {
                    "test" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            test.visibility = View.VISIBLE
                            testShadow.visibility = View.INVISIBLE
                        }
                    }
                    "test1" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            test1.visibility = View.VISIBLE
                            test1Shadow.visibility = View.INVISIBLE
                        }
                    }
                    "test2" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            test2.visibility = View.VISIBLE
                            test2Shadow.visibility = View.INVISIBLE
                        }
                    }
                    "test3" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            test3.visibility = View.VISIBLE
                            test3Shadow.visibility = View.INVISIBLE
                        }
                    }
                    "test4" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            //test4.visibility = View.VISIBLE
                            //test4Shadow.visibility = View.INVISIBLE
                        }
                    }
                    "test5" -> {
                        if (isFound) {
                            println("$id 값 가져옴")
                            //test5.visibility = View.VISIBLE
                            //test5Shadow.visibility = View.INVISIBLE
                        }
                    }
                }
                val plantBooks = PlantBooks(id, plantName, count, isFound)
                result.add(plantBooks)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return result
    }

    data class PlantBooks(var id: Int, var plantName: String, var count: Int, var isFound: Boolean)

}