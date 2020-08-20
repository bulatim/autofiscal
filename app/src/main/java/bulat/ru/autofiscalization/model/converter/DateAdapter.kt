package bulat.ru.autofiscalization.model.converter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {
    @FromJson
    fun fromJson(value: String?): Date? {
        if (value == null) return null
        val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss aaa", Locale.US)//Aug 14, 2019 12:06:47 PM
        try {
            return simpleDateFormat.parse(value)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    @ToJson
    fun toJson(date: Date?): Long? {
        return date?.time
    }
}