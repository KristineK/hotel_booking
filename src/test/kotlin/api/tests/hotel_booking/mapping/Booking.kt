package api.tests.hotel_booking.mapping

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import org.assertj.core.api.SoftAssertions
import java.math.BigDecimal

data class Booking(
    @Expose
    var firstname: String?,
    @Expose
    var lastname: String?,
    @Expose
    var totalprice: BigDecimal?,
    @Expose
    var depositpaid: Boolean?,
    @Expose
    var bookingdates: BookingDates?,
    @Expose
    var additionalneeds: String?)

fun getBookingJson(json: String): Booking {
    return Gson().fromJson(json, Booking::class.java)
}

fun compareBookings(bookingOld: Booking, bookingNew: Booking) {
    val softly = SoftAssertions()
    softly.assertThat(bookingNew.firstname).isEqualTo(bookingOld.firstname)
    softly.assertThat(bookingNew.lastname).isEqualTo(bookingOld.lastname)
    softly.assertThat(bookingNew.totalprice).isEqualTo(bookingOld.totalprice)
    softly.assertThat(bookingNew.depositpaid).isEqualTo(bookingOld.depositpaid)
    softly.assertThat(bookingNew.bookingdates!!.checkin).isEqualTo(bookingOld.bookingdates!!.checkin)
    softly.assertThat(bookingNew.bookingdates!!.checkout).isEqualTo(bookingOld.bookingdates!!.checkout)
    softly.assertThat(bookingNew.additionalneeds).isEqualTo(bookingOld.additionalneeds)
    softly.assertAll()
}