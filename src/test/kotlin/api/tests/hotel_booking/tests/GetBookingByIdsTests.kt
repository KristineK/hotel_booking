package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.CreatedBooking
import api.tests.hotel_booking.util.BookingRequests
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test


class GetBookingByIdsTests {
    var softly = SoftAssertions()

    @Test
    fun `get all bookings by ids after one more is added test`() {
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        val allBookingsNew: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size + 1)
        assertThat((allBookingsNew.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNotNull
    }

    @Test
    fun `get all bookings by ids after a new one is removed test`() {
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        BookingRequests().deleteBookingWithCookieHeader(newBooking.bookingid)
        val allBookingsNew: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size)
        assertThat((allBookingsNew.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `get bookings by ids with firstname and lastname test`() {
        val defaultBooking = BookingRequests().defaultBooking
        val query = mapOf("firstname" to defaultBooking.firstname, "lastname" to defaultBooking.lastname)
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(query), Array<JsonObject>::class.java)
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        val allBookingsNew: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(query), Array<JsonObject>::class.java)
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size + 1)
        assertThat((allBookingsNew.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNotNull
        // depending on need - additional checks for the firstname and lastname within booking can be added
    }

    @Test
    fun `get bookings by ids with checkin and checkout test`() {
        val defaultBooking = BookingRequests().defaultBooking
        val query = mapOf("checkin" to defaultBooking.bookingdates.checkin, "checkout" to defaultBooking.bookingdates.checkout)
        print(query)
        print(BookingRequests().getBookingIds(query))
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(query), Array<JsonObject>::class.java)
//        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
//        val allBookingsNew : Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(query), Array<JsonObject>::class.java)
//        assertThat(allBookingsNew.size).isEqualTo(allBookings.size + 1)
//        assertThat((allBookingsNew.find { it.get("bookingid").asInt == newBooking.bookingid }))
//                .`as`("Check that new booking ID is added").isNotNull
        // fail?
    }

//    todo add more tests around dates
}