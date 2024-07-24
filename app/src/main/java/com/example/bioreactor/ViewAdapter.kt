package com.example.bioreactor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAdapter(emptyList: List<Any>) : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {

    private var dataList: List<SensorData> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val temp1TextView: TextView = itemView.findViewById(R.id.Search_temp1)
        private val temp2TextView: TextView = itemView.findViewById(R.id.Search_temp2)
        private val temp3TextView: TextView = itemView.findViewById(R.id.Search_temp3)
        private val temp4TextView: TextView = itemView.findViewById(R.id.Search_temp4)
        private val temp5TextView: TextView = itemView.findViewById(R.id.Search_temp5)
        private val temp6TextView: TextView = itemView.findViewById(R.id.Search_temp6)
        private val temp7TextView: TextView = itemView.findViewById(R.id.Search_temp7)
        private val temp8TextView: TextView = itemView.findViewById(R.id.Search_temp8)
        private val temp9TextView: TextView = itemView.findViewById(R.id.Search_temp9)
        private val temp10TextView: TextView = itemView.findViewById(R.id.Search_temp10)
        private val temp11TextView: TextView = itemView.findViewById(R.id.Search_temp11)
        private val temp12TextView: TextView = itemView.findViewById(R.id.Search_temp12)
        private val heatTempTextView: TextView = itemView.findViewById(R.id.Search_heatTemp)
        private val heatPow : TextView = itemView.findViewById(R.id.Search_heatPow)
        private val PHTextView: TextView = itemView.findViewById(R.id.Search_PH)

        // 여기서 필요한 다른 뷰들도 마찬가지로 findViewById로 초기화합니다

        fun bind(sensorData: SensorData) {
            temp1TextView.text = sensorData.temp1?.toString() ?: "N/A"
            temp2TextView.text = sensorData.temp2?.toString() ?: "N/A"
            temp3TextView.text = sensorData.temp3?.toString() ?: "N/A"
            temp4TextView.text = sensorData.temp4?.toString() ?: "N/A"
            temp5TextView.text = sensorData.temp5?.toString() ?: "N/A"
            temp6TextView.text = sensorData.temp6?.toString() ?: "N/A"
            temp7TextView.text = sensorData.temp7?.toString() ?: "N/A"
            temp8TextView.text = sensorData.temp8?.toString() ?: "N/A"
            temp9TextView.text = sensorData.temp9?.toString() ?: "N/A"
            temp10TextView.text = sensorData.temp10?.toString() ?: "N/A"
            temp11TextView.text = sensorData.temp11?.toString() ?: "N/A"
            temp12TextView.text = sensorData.temp12?.toString() ?: "N/A"
            heatTempTextView.text =sensorData.heattemp.toString() ?: "N/A"
            heatPow.text = sensorData.heatPow.toString() ?: "N/A"
            PHTextView.text = sensorData.Ph?.toString() ?: "N/A"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: List<SensorData>) {
        dataList = data
        notifyDataSetChanged()
    }
}
