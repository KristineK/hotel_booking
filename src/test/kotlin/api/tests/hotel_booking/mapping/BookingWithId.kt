package api.tests.hotel_booking.mapping

import com.google.gson.Gson

data class BookingWithId(
    var bookingid: Int,
    var booking: Booking)

fun getBookingWithIdJson(json: String): BookingWithId {
    return Gson().fromJson(json, BookingWithId::class.java)
}