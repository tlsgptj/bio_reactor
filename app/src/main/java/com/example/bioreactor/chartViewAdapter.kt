package com.example.bioreactor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.bioreactor.databinding.ActivityChartViewBinding
import com.github.mikephil.charting.data.Entry

class chartViewAdapter(private val context: Context, private var chartDataList: List<resultChartData>) : RecyclerView.Adapter<chartViewAdapter.ChartViewHolder>() {

    inner class ChartViewHolder(val binding: ActivityChartViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ActivityChartViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val chartData = chartDataList[position]
        val temp1 = createChart("temp1", chartData.temp1, chartData.time)
        val temp2 = createChart("temp2", chartData.temp2, chartData.time)
        val temp3 = createChart("temp3", chartData.temp3, chartData.time)
        val temp4 = createChart("temp4", chartData.temp4, chartData.time)
        val temp5 = createChart("temp5", chartData.temp5, chartData.time)
        val temp6 = createChart("temp6", chartData.temp6, chartData.time)
        val temp7 = createChart("temp7", chartData.temp7, chartData.time)
        val temp8 = createChart("temp8", chartData.temp8, chartData.time)
        val temp9 = createChart("temp9", chartData.temp9, chartData.time)
        val temp10 = createChart("temp10", chartData.temp10, chartData.time)
        val temp11 = createChart("temp11", chartData.temp11, chartData.time)
        val temp12 = createChart("temp12", chartData.temp12, chartData.time)
        val phChart = createChart("pH", chartData.ph, chartData.time)
        val motorChart1 = createChart("Motor1", chartData.motor, chartData.time)
        val motorChart2 = createChart("Motor2", chartData.motor1, chartData.time)


        holder.binding.resultGraph.addView(phChart)
        holder.binding.resultGraph.addView(motorChart1)
        holder.binding.resultGraph.addView(motorChart2)

       /* if (isGoalReached(chartData)) {
            val message = "Goal reached for chart $position"
            sendFCMMessage("goal_reached", message)
        }*/
    }

    override fun getItemCount() = chartDataList.size

    fun setData(data: List<resultChartData>) {
        chartDataList = data
        notifyDataSetChanged()
    }

    /*private fun isGoalReached(chartData: resultChartData): Boolean {
        for (i in 1..12) {
            val fieldName = "Temp$i"
            val data = chartData.getField(fieldName) ?: emptyList()
            val threshold = chartData.thresholds[fieldName] ?: 0f
            val minData = data.minOrNull() ?: Float.MAX_VALUE
            if (minData <= threshold) {
                return true
            }
        }
        return false
    }*/

    private fun createChart(label: String, data: List<Float>, timeData: List<Float>): LineChart {
        val lineChart = LineChart(context)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        lineChart.layoutParams = layoutParams

        val entries = mutableListOf<Entry>()
        data.forEachIndexed { index, value ->
            entries.add(Entry(timeData[index], value))
        }

        val dataSet = LineDataSet(entries, label)
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()

        return lineChart
    }

    fun sendFCMMessage(topic: String, message: String) {
        // FCM 메시지 전송 로직 추가
    }
}
