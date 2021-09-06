package api.tests.hotel_booking.mapping

import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun <T> getJsonBody(json: String, typeOfT: Class<T>): T {
    return Gson().fromJson(json, typeOfT)
}

fun dateParser(date: String): LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
}

fun randomDate(): String {
    val random = Random()
    val minDay = LocalDate.of(2010, 1, 1).toEpochDay().toInt()
    val maxDay = LocalDate.of(2030, 1, 1).toEpochDay().toInt()
    val randomDay = (minDay + random.nextInt(maxDay - minDay)).toLong()
    return LocalDate.ofEpochDay(randomDay).toString()
}