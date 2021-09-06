package api.tests.hotel_booking.mapping

import com.google.gson.Gson

data class Bookings(var bookingid: Int)

fun getBookingJsonArray(json: String): Array<Bookings> {
    return Gson().fromJson(json, Array<Bookings>::class.java)
}
