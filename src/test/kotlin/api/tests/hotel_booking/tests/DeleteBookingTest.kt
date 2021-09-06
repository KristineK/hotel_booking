package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.getBookingJsonArray
import api.tests.hotel_booking.mapping.getBookingWithIdJson
import api.tests.hotel_booking.util.BookingRequests.createDefaultBooking
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithAuthorisationHeader
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithCookieHeader
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithoutAuth
import api.tests.hotel_booking.util.BookingRequests.getBookingId
import api.tests.hotel_booking.util.BookingRequests.getBookingIdNotFound
import api.tests.hotel_booking.util.BookingRequests.getBookingIds
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class DeleteBookingTest {
    @Test
    fun `create and then delete booking with request which uses cookie for auth test`() {
        val newBooking = getBookingWithIdJson(createDefaultBooking())
        getBookingId(newBooking.bookingid)
        deleteBookingWithCookieHeader(newBooking.bookingid)
        getBookingIdNotFound(newBooking.bookingid)
        val allBookings = getBookingJsonArray(getBookingIds())
        assertThat((allBookings.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    // TODO correct this test fails, need to be fixed with auth
    @Test
    @Tag("fail")
    fun `create and then delete booking with request which uses basic auth for auth test`() {
        val newBooking = getBookingWithIdJson(createDefaultBooking())
        getBookingId(newBooking.bookingid)
        deleteBookingWithAuthorisationHeader(newBooking.bookingid)
        getBookingIdNotFound(newBooking.bookingid)
        val allBookings = getBookingJsonArray(getBookingIds())
        assertThat((allBookings.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `create and then delete booking without authorization token test`() {
        val newBooking = getBookingWithIdJson(createDefaultBooking())
        getBookingId(newBooking.bookingid)
        deleteBookingWithoutAuth(newBooking.bookingid)
        getBookingId(newBooking.bookingid)
        val allBookings = getBookingJsonArray(getBookingIds())
        assertThat((allBookings.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNotNull
    }

    // TODO change when specification is known
    @Test
    @Tag("fail")
    fun `delete none existing id test`() {
        val noneExistingBookingId = 987654321
        getBookingIdNotFound(noneExistingBookingId)
        // no specification on this error
        assertThat(deleteBookingWithCookieHeader(noneExistingBookingId, 405)).isEqualTo("Method Not Allowed")
    }
}