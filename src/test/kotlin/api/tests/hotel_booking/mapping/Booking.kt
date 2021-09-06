package api.tests.hotel_booking.mapping

import java.math.BigDecimal

data class Booking(
    var firstname: String,
    var lastname: String,
    var totalprice: BigDecimal,
    var depositpaid: Boolean,
    var bookingdates: BookingDates,
    var additionalneeds: String) {
}