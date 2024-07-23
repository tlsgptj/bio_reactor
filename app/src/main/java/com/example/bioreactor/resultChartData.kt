package com.example.bioreactor

import com.google.firebase.database.PropertyName

data class resultChartData(
    @get:PropertyName("temp/temp1") @set:PropertyName("temp/temp1") var temp1: List<Float> = emptyList(),
    @get:PropertyName("temp/temp2") @set:PropertyName("temp/temp2") var temp2: List<Float> = emptyList(),
    @get:PropertyName("temp/temp3") @set:PropertyName("temp/temp3") var temp3: List<Float> = emptyList(),
    @get:PropertyName("temp/temp4") @set:PropertyName("temp/temp4") var temp4: List<Float> = emptyList(),
    @get:PropertyName("temp/temp5") @set:PropertyName("temp/temp5") var temp5: List<Float> = emptyList(),
    @get:PropertyName("temp/temp6") @set:PropertyName("temp/temp6") var temp6: List<Float> = emptyList(),
    @get:PropertyName("temp/temp7") @set:PropertyName("temp/temp7") var temp7: List<Float> = emptyList(),
    @get:PropertyName("temp/temp8") @set:PropertyName("temp/temp8") var temp8: List<Float> = emptyList(),
    @get:PropertyName("temp/temp9") @set:PropertyName("temp/temp9") var temp9: List<Float> = emptyList(),
    @get:PropertyName("temp/temp10") @set:PropertyName("temp/temp10") var temp10: List<Float> = emptyList(),
    @get:PropertyName("temp/temp11") @set:PropertyName("temp/temp11") var temp11: List<Float> = emptyList(),
    @get:PropertyName("temp/temp12") @set:PropertyName("temp/temp12") var temp12: List<Float> = emptyList(),
    @get:PropertyName("ph") @set:PropertyName("ph") var ph: List<Float> = emptyList(),
    @get:PropertyName("motor") @set:PropertyName("motor") var motor: List<Float> = emptyList(),
    @get:PropertyName("motor1") @set:PropertyName("motor1") var motor1: List<Float> = emptyList(),
    @get:PropertyName("time") @set:PropertyName("time") var time: List<Float> = emptyList()
) {
    // 추가: 필드명을 바탕으로 데이터를 반환하는 헬퍼 함수
    fun getField(fieldName: String): List<Float>? {
        return when (fieldName) {
            "Temp1" -> temp1
            "Temp2" -> temp2
            "Temp3" -> temp3
            "Temp4" -> temp4
            "Temp5" -> temp5
            "Temp6" -> temp6
            "Temp7" -> temp7
            "Temp8" -> temp8
            "Temp9" -> temp9
            "Temp10" -> temp10
            "Temp11" -> temp11
            "Temp12" -> temp12
            "PH" -> ph
            "motor" -> motor
            "motor1" -> motor1
            "time" -> time
            else -> null
        }
    }


}
