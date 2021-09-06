package api.tests.hotel_booking.mapping

import com.google.gson.Gson

fun <T> getJsonBody(json: String, typeOfT: Class<T>): T {
    return Gson().fromJson(json, typeOfT)
}