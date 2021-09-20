package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.dateParser
import api.tests.hotel_booking.mapping.getBookingJsonArray
import api.tests.hotel_booking.mapping.getBookingWithIdJson
import api.tests.hotel_booking.util.BookingRequests.createDefaultBooking
import api.tests.hotel_booking.util.BookingRequests.defaultBooking
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithCookieHeader
import api.tests.hotel_booking.util.BookingRequests.getBookingIds
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class GetBookingByIdsTest {
    @Test
    fun `get all bookings by ids after one more is added test`() {
        val allBookings = getBookingIds()
        val newBooking = createDefaultBooking()
        val allBookingsNew = getBookingIds()
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size + 1)
        assertThat((allBookingsNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNotNull
    }

    @Test
    fun `get all bookings by ids after a new one is removed test`() {
        val allBookings = getBookingIds()
        val newBooking = createDefaultBooking()
        deleteBookingWithCookieHeader(newBooking.bookingid)
        val allBookingsNew = getBookingIds()
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size)
        assertThat((allBookingsNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNull()
    }

    @Test
    fun `get bookings by ids with firstname and lastname test`() {
        val defaultBooking = defaultBooking
        val query = mapOf("firstname" to defaultBooking.firstname.toString(), "lastname" to defaultBooking.lastname.toString())
        val allBookings = getBookingIds(query)
        val newBooking = createDefaultBooking()
        val allBookingsNew = getBookingIds(query)
        assertThat(allBookingsNew.size).isEqualTo(allBookings.size + 1)
        assertThat((allBookingsNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("Check that new booking ID is added").isNotNull
        // depending on need - additional checks for the firstname and lastname within booking can be added
    }

    // TODO errors with:
    //    * with exact check-in/check-out date query
    //    * with plus 1 day to check-out date query
    //  Works as expected with:
    //    * minus 1 day to check-in date query
    //    * with extra day for check-in/check-out date query
    //    * minus 1 day to check-out date query
    //    * with plus 1 day to check-in date query
    @Test
    @Tag("fail")
    fun `get bookings by ids with checkin and checkout booking date test`() {
        val defaultBookingDates = defaultBooking.bookingdates

        val queryExactDate = mapOf("checkin" to defaultBookingDates!!.checkin!!, "checkout" to defaultBookingDates.checkout!!)
        val queryCheckInMinusOneDay = mapOf("checkin" to dateParser(defaultBookingDates.checkin!!).minusDays(1).toString(),
            "checkout" to defaultBookingDates.checkout!!)
        val queryCheckInPlusOneDay = mapOf("checkin" to dateParser(defaultBookingDates.checkin!!).plusDays(1).toString(),
            "checkout" to defaultBookingDates.checkout!!)
        val queryCheckOutMinusOneDay = mapOf("checkin" to defaultBookingDates.checkin!!,
            "checkout" to dateParser(defaultBookingDates.checkout!!).minusDays(1).toString())
        val queryCheckOutPlusOneDay = mapOf("checkin" to defaultBookingDates.checkin!!,
            "checkout" to dateParser(defaultBookingDates.checkout!!).plusDays(1).toString())
        val queryExtraDay = mapOf("checkin" to dateParser(defaultBookingDates.checkin!!).minusDays(1).toString(),
            "checkout" to dateParser(defaultBookingDates.checkout!!).plusDays(1).toString())

        val bookingsExactDate = getBookingIds(queryExactDate)
        val bookingsCheckInMinusOneDay = getBookingIds(queryCheckInMinusOneDay)
        val bookingsCheckInPlusOneDay = getBookingIds(queryCheckInPlusOneDay)
        val bookingsCheckOutMinusOneDay = getBookingIds(queryCheckOutMinusOneDay)
        val bookingsCheckOutPlusOneDay = getBookingIds(queryCheckOutPlusOneDay)
        val bookingsExtraDay = getBookingIds(queryExtraDay)

        val newBooking = createDefaultBooking()

        val bookingsExactNew = getBookingIds(queryExactDate)
        val bookingsCheckInMinusOneDayNew = getBookingIds(queryCheckInMinusOneDay)
        val bookingsCheckInPlusOneDayNew = getBookingIds(queryCheckInPlusOneDay)
        val bookingsCheckOutMinusOneDayNew = getBookingIds(queryCheckOutMinusOneDay)
        val bookingsCheckOutPlusOneDayNew = getBookingIds(queryCheckOutPlusOneDay)
        val bookingsExtraDayNew = getBookingIds(queryExtraDay)

        val softly = SoftAssertions()
        softly.assertThat(bookingsExactNew.size)
            .`as`("Booking query returns one more booking with exact check-in/check-out date query.")
            .isEqualTo(bookingsExactDate.size + 1)
        softly.assertThat((bookingsExactNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with exact check-in/check-out date query.").isNotNull

        softly.assertThat(bookingsCheckInMinusOneDayNew.size)
            .`as`("Booking query returns one more booking with minus 1 day to check-in date query.")
            .isEqualTo(bookingsCheckInMinusOneDay.size + 1)
        softly.assertThat((bookingsCheckInMinusOneDayNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with minus 1 day to check-in date query.").isNotNull

        softly.assertThat(bookingsCheckInPlusOneDayNew.size)
            .`as`("Booking query returns one more booking with plus 1 day to check-in date query.")
            .isEqualTo(bookingsCheckInPlusOneDay.size)
        softly.assertThat((bookingsCheckInPlusOneDayNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with plus 1 day to check-in date query.").isNull()

        softly.assertThat(bookingsCheckOutMinusOneDayNew.size)
            .`as`("Booking query returns one more booking with minus 1 day to check-out date query.")
            .isEqualTo(bookingsCheckOutMinusOneDay.size)
        softly.assertThat((bookingsCheckOutMinusOneDayNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with minus 1 day to check-out date query.").isNull()

        softly.assertThat(bookingsCheckOutPlusOneDayNew.size)
            .`as`("Booking query returns one more booking with plus 1 day to check-out date query.")
            .isEqualTo(bookingsCheckOutPlusOneDay.size + 1)
        softly.assertThat((bookingsCheckOutPlusOneDayNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with plus 1 day to check-out date query.").isNotNull

        softly.assertThat(bookingsExtraDayNew.size)
            .`as`("Booking query returns one more booking with extra day for check-in/check-out date query.")
            .isEqualTo(bookingsExtraDay.size + 1)
        softly.assertThat((bookingsExtraDayNew.find { it.bookingid == newBooking.bookingid }))
            .`as`("New id can be found in the all booking with extra day for check-in/check-out date query.").isNotNull

        softly.assertAll()
    }
}