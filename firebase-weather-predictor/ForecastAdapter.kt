class ForecastAdapter(private val context: Context, private val forecastList: List<String>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.forecastText.text = forecastList[position]
    }
    override fun getItemCount(): Int {
        return forecastList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val forecastText: TextView = itemView.findViewById(R.id.forecast_text)
    }
}