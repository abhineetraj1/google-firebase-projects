object ServerAPI {
    private const val PREDICT_API_ENDPOINT = "https://example.com/predict"
    fun getPredictedWeatherForecast(
        weather: String,
        temperature: Double,
        humidity: Double,
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestQueue = RequestQueueSingleton.getInstance(null).requestQueue
        val requestBody = JSONObject()
        requestBody.put("weather", weather)
        requestBody.put("temperature", temperature)
        requestBody.put("humidity", humidity)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, PREDICT_API_ENDPOINT, requestBody,
            { response ->
                val forecasts = mutableListOf<String>()
                val predictions = response.getJSONArray("predictions")
                for (i in 0 until predictions.length()) {
                    forecasts.add(predictions.getString(i))
                }
                onSuccess(forecasts)
            },
            { error ->
                onError(error.message ?: "An unknown error occurred")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
}
