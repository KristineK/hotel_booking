package api.tests.hotel_booking.mapping

import com.google.gson.Gson

data class CreatedBooking(
    var bookingid: Int,
    var booking: Booking) {
}

fun getCreatedBookingJson(json: String): CreatedBooking {
    return Gson().fromJson(json, CreatedBooking::class.java)
}