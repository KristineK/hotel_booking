package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.CreatedBooking
import api.tests.hotel_booking.mapping.getJsonBody
import api.tests.hotel_booking.util.BookingRequests.createDefaultBooking
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithAuthorisationHeader
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithCookieHeader
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithoutAuth
import api.tests.hotel_booking.util.BookingRequests.getBookingId
import api.tests.hotel_booking.util.BookingRequests.getBookingIdNotFound
import api.tests.hotel_booking.util.BookingRequests.getBookingIds
import com.google.gson.JsonObject
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DeleteBookingTests {
    @Test
    fun `create and then delete booking with request which uses cookie for auth test`() {
        val newBooking = getJsonBody(createDefaultBooking(), CreatedBooking::class.java)
        getBookingId(newBooking.bookingid)
        deleteBookingWithCookieHeader(newBooking.bookingid)
        getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = getJsonBody(getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    // TODO correct this test fails, need to be fixed with auth
    @Test
    fun `create and then delete booking with request which uses basic auth for auth test`() {
        val newBooking = getJsonBody(createDefaultBooking(), CreatedBooking::class.java)
        getBookingId(newBooking.bookingid)
        deleteBookingWithAuthorisationHeader(newBooking.bookingid)
        getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = getJsonBody(getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `create and then delete booking without authorization token test`() {
        val newBooking = getJsonBody(createDefaultBooking(), CreatedBooking::class.java)
        getBookingId(newBooking.bookingid)
        deleteBookingWithoutAuth(newBooking.bookingid)
        getBookingIdNotFound(newBooking.bookingid)
        val allBookings: Array<JsonObject> = getJsonBody(getBookingIds(), Array<JsonObject>::class.java)
        Assertions.assertThat((allBookings.find { it.get("bookingid").asInt == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `delete none existing id test`() {
        val noneExistingBookingId = 123456789
        getBookingIdNotFound(noneExistingBookingId)
        // not sure if this request should or should not though an error:
        deleteBookingWithCookieHeader(noneExistingBookingId)
    }
}