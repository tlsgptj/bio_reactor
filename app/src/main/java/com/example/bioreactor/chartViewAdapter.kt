package com.example.bioreactor
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.charts.LineChart

class chartViewAdapter(
    private val context: Context,
    private var dataSets: List<List<Entry>>
) : RecyclerView.Adapter<chartViewAdapter.ChartViewHolder>() {

    class ChartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chart: LineChart = itemView.findViewById(R.id.resultGraph)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_chart_view, parent, false)
        return ChartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val entries = dataSets[position]
        val dataSet = LineDataSet(entries, "Temp${position + 1}")
        val lineData = LineData(dataSet)

        holder.chart.data = lineData

        val description = Description()
        description.text = "Temp${position + 1} Chart"
        holder.chart.description = description

        holder.chart.invalidate() // refresh
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    fun updateData(newDataSets: List<List<Entry>>) {
        dataSets = newDataSets
        notifyDataSetChanged()
    }
}



