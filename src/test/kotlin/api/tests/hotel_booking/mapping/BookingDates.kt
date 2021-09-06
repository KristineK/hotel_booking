package api.tests.hotel_booking.mapping

import com.google.gson.annotations.Expose

data class BookingDates(
    @Expose
    var checkin: String?,
    @Expose
    var checkout: String?)
