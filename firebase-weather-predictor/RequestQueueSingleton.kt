object RequestQueueSingleton {
    private var instance: RequestQueueSingleton? = null
    lateinit var requestQueue: RequestQueue
    @Synchronized
    fun getInstance(context: Context?): RequestQueueSingleton {
        if (instance == null) {
            instance = RequestQueueSingleton
            if (context != null) {
                requestQueue = Volley.newRequestQueue(context.applicationContext)
            }
        }
        return instance!!
    }
    fun initialize(context: Context) {
        requestQueue = Volley.newRequestQueue(context.applicationContext)
    }
}