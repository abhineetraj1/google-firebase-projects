class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase and Volley request queue
        FirebaseApp.initializeApp(this)
        RequestQueueSingleton.initialize(this)
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val WEATHER_API_KEY = "YOUR_API_KEY"
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = RequestQueueSingleton.getInstance(this).requestQueue
        val location = "India"
        getWeatherData(location)
    }
    private fun getWeatherData(location: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$location&appid=$WEATHER_API_KEY"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val weather = response.getJSONObject("weather").getString("main")
                val temperature = response.getJSONObject("main").getDouble("temp")
                val humidity = response.getJSONObject("main").getDouble("humidity")
                ServerAPI.getPredictedWeatherForecast(weather, temperature, humidity,
                    { predictedForecast ->
                        displayPredictedWeatherForecast(predictedForecast)
                    }
                )
            }
        )
        requestQueue.add(request)
    }

    private fun displayPredictedWeatherForecast(predictions: Array<FloatArray>) {
        val forecastList = mutableListOf<String>()
        for (i in 0 until predictions.size) {
            forecastList.add("Forecast " + (i+1) + ": " + predictions[i][0])
        }
        val forecastAdapter = ForecastAdapter(this, forecastList)
        forecast_recyclerview.adapter = forecastAdapter
    }
}