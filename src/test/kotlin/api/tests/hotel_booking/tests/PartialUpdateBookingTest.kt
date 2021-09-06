package api.tests.hotel_booking.tests

import api.tests.hotel_booking.mapping.*
import api.tests.hotel_booking.util.BookingRequests.createDefaultBooking
import api.tests.hotel_booking.util.BookingRequests.deleteBookingWithCookieHeader
import api.tests.hotel_booking.util.BookingRequests.getBookingId
import api.tests.hotel_booking.util.BookingRequests.partialUpdateBookingWithBasicAuthHeader
import api.tests.hotel_booking.util.BookingRequests.partialUpdateBookingWithCookieHeader
import api.tests.hotel_booking.util.BookingRequests.partialUpdateBookingWithoutHeader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.*

class PartialUpdateBookingTest {
    lateinit var testBooking: BookingWithId
    private val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    private var bookingJson = Booking(null, null, null, null, null, null)

    @BeforeEach
    fun dataPrep() {
        testBooking = getBookingWithIdJson(createDefaultBooking())
    }

    @AfterEach
    fun cleanUp() {
        deleteBookingWithCookieHeader(testBooking.bookingid)
    }

    @Test
    fun `partially update booking firstname test`() {
        bookingJson.firstname = randomAlphabetic(10)
        testBooking.booking.firstname = bookingJson.firstname
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking lastname test`() {
        bookingJson.lastname = randomAlphabetic(10)
        testBooking.booking.lastname = bookingJson.lastname
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking totalprice test`() {
        bookingJson.totalprice = Random().nextInt(1000).toBigDecimal()
        testBooking.booking.totalprice = bookingJson.totalprice
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking depositpaid test`() {
        bookingJson.depositpaid = !testBooking.booking.depositpaid!!
        testBooking.booking.depositpaid = bookingJson.depositpaid
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    // TODO something strange is happening it seems if checkout or checkin date is not set - it becomes today's date, unclear if expected or not
    @Test
    @Tag("fail")
    fun `partially update booking checkin test`() {
        bookingJson.bookingdates = BookingDates(randomDate(), null)
        testBooking.booking.bookingdates!!.checkin = bookingJson.bookingdates!!.checkin
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    // TODO something strange is happening it seems if checkout or checkin date is not set - it becomes today's date, unclear if expected or not
    @Test
    @Tag("fail")
    fun `partially update booking checkout test`() {
        bookingJson.bookingdates = BookingDates(null, randomDate())
        testBooking.booking.bookingdates!!.checkout = bookingJson.bookingdates!!.checkout
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking additionalneeds test`() {
        bookingJson.additionalneeds = randomAlphabetic(10)
        testBooking.booking.additionalneeds = bookingJson.additionalneeds
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking firstname and lastname (2 fields) test`() {
        bookingJson.firstname = randomAlphabetic(10)
        bookingJson.lastname = randomAlphabetic(10)
        testBooking.booking.firstname = bookingJson.firstname
        testBooking.booking.lastname = bookingJson.lastname
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking dates, additionalneeds and price (4 fields) test`() {
        bookingJson = Booking(null, null, Random().nextInt(1000).toBigDecimal(), null, BookingDates(randomDate(), randomDate()), randomAlphabetic(10))
        testBooking.booking.totalprice = bookingJson.totalprice
        testBooking.booking.bookingdates!!.checkin = bookingJson.bookingdates!!.checkin
        testBooking.booking.bookingdates!!.checkout = bookingJson.bookingdates!!.checkout
        testBooking.booking.additionalneeds = bookingJson.additionalneeds
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking for all fields test`() {
        bookingJson = Booking(randomAlphabetic(10), randomAlphabetic(10), Random().nextInt(1000).toBigDecimal(), !testBooking.booking.depositpaid!!, BookingDates(randomDate(), randomDate()), randomAlphabetic(10))
        testBooking.booking = bookingJson
        val updatedBooking = getBookingJson(partialUpdateBookingWithCookieHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }

    @Test
    fun `partially update booking firstname without auth header test`() {
        bookingJson.firstname = randomAlphabetic(10)
        assertThat(partialUpdateBookingWithoutHeader(gson.toJson(bookingJson), testBooking.bookingid)).isEqualTo("Forbidden")
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
    }


    // TODO auth header not working see inside for more details
    @Test
    @Tag("fail")
    fun `partially update booking firstname with basic auth header test`() {
        bookingJson.firstname = randomAlphabetic(10)
        testBooking.booking.firstname = bookingJson.firstname
        val updatedBooking = getBookingJson(partialUpdateBookingWithBasicAuthHeader(gson.toJson(bookingJson), testBooking.bookingid))
        compareBookings(testBooking.booking, getBookingJson(getBookingId(testBooking.bookingid)))
        compareBookings(updatedBooking, getBookingJson(getBookingId(testBooking.bookingid)))
    }
}
