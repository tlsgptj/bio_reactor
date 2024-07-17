package com.example.bioreactor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class ChartAdapter(private val context: Context, private val chartDataList: List<resultChartData>) : RecyclerView.Adapter<ChartAdapter.ChartViewHolder>() {

    class ChartViewHolder(val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        val binding = ItemChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val chartData = chartDataList[position]

        holder.binding.chartTitle.text = "Results"
        holder.binding.chartContainer.removeAllViews()

        // Create temperature charts
        val temperatureCharts = listOf(
            createChart("Temperature1", chartData.Temp1),
            createChart("Temperature2", chartData.Temp2),
            createChart("Temperature3", chartData.Temp3),
            createChart("Temperature4", chartData.Temp4),
            createChart("Temperature5", chartData.Temp5),
            createChart("Temperature6", chartData.Temp6),
            createChart("Temperature7", chartData.Temp7),
            createChart("Temperature8", chartData.Temp8),
            createChart("Temperature9", chartData.Temp9),
            createChart("Temperature10", chartData.Temp10),
            createChart("Temperature11", chartData.Temp11),
            createChart("Temperature12", chartData.Temp12)
        )

        // Add temperature charts to container
        temperatureCharts.forEach {
            holder.binding.chartContainer.addView(it)
        }

        // Create pH chart
        val phChart = createChart("pH", chartData.PH)
        holder.binding.chartContainer.addView(phChart)

        // Create motor charts
        val motorChart1 = createChart("Motor1", chartData.motor)
        holder.binding.chartContainer.addView(motorChart1)

        val motorChart2 = createChart("Motor2", chartData.motor1)
        holder.binding.chartContainer.addView(motorChart2)

        // Check if goal is reached and send FCM message
        if (isGoalReached(position)) {
            val message = "Goal reached for chart $position"
            sendFCMMessage("goal_reached", message)
        }
    }
    private fun isGoalReached(chartData: resultChartData): Boolean {

        for (i in 1..12) {
            val fieldName = "Temp$i"
            val data = chartData.getField(fieldName) ?: continue
            val threshold = if (i in 1..12) chartData.Temp1[i-1] else continue
            val minData = data.min() ?: continue
            if (minData <= threshold) {
                return true
            }
        }

        // 다른 데이터들도 확인할 수 있도록 chartData.otherData를 이용하여 처리합니다.
        for ((label, data) in chartData.otherData) {
            val threshold = chartData.thresholds[label] ?: continue
            val minData = data.min() ?: continue
            if (minData <= threshold) {
                return true
            }
        }

        return false
    }


    private fun createChart(label: String, data: List<Float>): LineChart {
        val lineChart = LineChart(context)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        lineChart.layoutParams = layoutParams

        val dataSet = LineDataSet(data, label)
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()

        return lineChart
    }

    override fun getItemCount() = chartDataList.size
}
