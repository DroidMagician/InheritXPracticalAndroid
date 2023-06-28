package com.example.inheritx.data.local

import androidx.room.TypeConverter
import com.example.inheritx.domain.homeData.model.Source
import com.google.gson.Gson

class SourceTypeConvertor{

    @TypeConverter
    fun ObjectToJsonString(value: Source?): String = Gson().toJson(value)

    @TypeConverter
    fun stringToSourceObject(value: String) =
        Gson().fromJson(value, Source::class.java)

}
