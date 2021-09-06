package api.tests.hotel_booking.mapping

import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun <T> getJsonBody(json: String, typeOfT: Class<T>): T {
    return Gson().fromJson(json, typeOfT)
}

fun dateParser(date: String): LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
}