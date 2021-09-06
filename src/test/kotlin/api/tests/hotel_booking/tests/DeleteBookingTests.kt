package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.CreatedBooking
import api.tests.hotel_booking.util.BookingRequests
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DeleteBookingTests {
    @Test
    fun `create and then delete booking with request which uses cookie for auth test`() {
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        BookingRequests().getBookingId(newBooking.bookingid)
        BookingRequests().deleteBookingWithCookieHeader(newBooking.bookingid)
        BookingRequests().getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNull()
    }

    // TODO correct this test fails, need to be fixed with auth
    @Test
    fun `create and then delete booking with request which uses basic auth for auth test`() {
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        BookingRequests().getBookingId(newBooking.bookingid)
        BookingRequests().deleteBookingWithAuthorisationHeader(newBooking.bookingid)
        BookingRequests().getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `create and then delete booking without authorization token test`() {
        val newBooking = Gson().fromJson(BookingRequests().createDefaultBooking(), CreatedBooking::class.java)
        BookingRequests().getBookingId(newBooking.bookingid)
        BookingRequests().deleteBookingWithoutAuth(newBooking.bookingid)
        BookingRequests().getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = Gson().fromJson(BookingRequests().getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
                .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `delete none existing id test`() {
        val noneExistingBookingId = 123456789
        BookingRequests().getBookingIdNotFound(noneExistingBookingId)
        // not sure if this request should or should not though an error:
        BookingRequests().deleteBookingWithCookieHeader(noneExistingBookingId)
    }
}