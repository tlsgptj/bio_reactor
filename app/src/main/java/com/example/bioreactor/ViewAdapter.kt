package com.example.bioreactor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var dataList: List<SensorData> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.Search_Value)
        private val thingTextView: TextView = itemView.findViewById(R.id.Search_thing)
        // 여기서 필요한 다른 뷰들도 마찬가지로 findViewById로 초기화합니다

        fun bind(sensorData: SensorData) {
            nameTextView.text = resultChartValue.COLUMN_NAME
            thingTextView.text = dataList.toString()
            // 여기서 필요한 다른 데이터들도 적절히 바인딩합니다
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
