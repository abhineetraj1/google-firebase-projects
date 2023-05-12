import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.google.firebase.ml.custom.FirebaseModelDataType
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions
import com.google.firebase.ml.custom.FirebaseModelInterpreter
import com.google.firebase.ml.custom.FirebaseModelManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var weatherList: ArrayList<String>
    private lateinit var modelInterpreter: FirebaseModelInterpreter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherList = ArrayList()
        setupFirebaseML()
        predict_button.setOnClickListener {
            val weather = weather_edit_text.text.toString()
            val temperature = temperature_edit_text.text.toString().toDouble()
            val humidity = humidity_edit_text.text.toString().toDouble()

            getPredictedWeatherForecast(weather, temperature, humidity)
        }
    }
    private fun setupFirebaseML() {
        val remoteModel = FirebaseCustomRemoteModel.Builder(MODEL_NAME).build()
        FirebaseModelManager.getInstance().registerRemoteModel(remoteModel)
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 3))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 10))
            .build()
        FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val modelFile = task.result!!
                    modelInterpreter = FirebaseModelInterpreter.getInstance(
                        FirebaseModelInterpreterOptions.Builder(remoteModel).build())!!
                } else {
                    Toast.makeText(
                        this,
                        "Failed to download model: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
    private fun getPredictedWeatherForecast(weather: String, temperature: Double, humidity: Double) {
        val url = URL("$API_ENDPOINT/weather?weather=$weather&temperature=$temperature&humidity=$humidity")
        val conn = url.openConnection() as HttpsURLConnection
        conn.requestMethod = "GET"
        BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
            val response = StringBuffer()
            var line = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }
            val jsonResponse = JSONObject(response.toString())
            val forecastList = jsonResponse.getJSONArray("forecasts")
            for (i in 0 until forecastList.length()) {
                weatherList.add(forecastList.getString(i))
            }
            predictWeatherUsingFirebaseML(weatherList)
        }
    }
    private fun predictWeatherUsingFirebaseML(weatherList: ArrayList<String>) {
        val inputs = arrayOf(floatArrayOf(0f, 0f, 0f))
        inputs[0][0] = weatherList.indexOf("Sunny").toFloat()
        inputs[0][1] = weatherList.indexOf("Cloudy").toFloat()
        inputs[0][2] = weatherList.indexOf("Rainy").toFloat()
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 3))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 10))
    val inputsArray = arrayOf(inputs)
    try {
        modelInterpreter.run(inputsArray, inputOutputOptions)
            .addOnSuccessListener { output ->
                val predictions = output.getOutput<Array<FloatArray>>(0)[0]
                for (i in predictions.indices) {
                    val weather = weatherList[predictions[i].toInt()]
                    val temperature = (20..30).random()
                    val humidity = (40..80).random()
                    val forecast = "$weather, $temperature°C, $humidity%"
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherList)
                    forecast_list_view.adapter = adapter
                    weatherList.add(forecast)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to predict weather: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    } catch (e: FirebaseMLException) {
        Toast.makeText(
            this,
            "Failed to run model: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}
companion object {
    const val MODEL_NAME = "weather_forecast"
    const val API_ENDPOINT = "https://your_api_endpoint.com"
}
}