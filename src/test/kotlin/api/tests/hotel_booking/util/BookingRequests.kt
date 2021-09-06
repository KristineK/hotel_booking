package api.tests.hotel_booking.util

import api.tests.hotel_booking.mapping.Booking
import api.tests.hotel_booking.mapping.BookingDates
import com.google.gson.Gson
import io.restassured.http.Header
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.*

class BookingRequests : Requests() {
    private var url: String

    init {
        val props = Properties()
        props.load(FileInputStream("src/test/resources/config.properties"))
        url = props.getProperty("baseUrl") + props.getProperty("bookingPath")
    }

    var defaultBooking = Booking("Jim",
            "Brown",
            BigDecimal.valueOf(111),
            true,
            BookingDates("2018-01-01",
                    "2019-01-01"),
            "Breakfast")

    fun createDefaultBooking(): String {
        return createBooking(Gson().toJson(defaultBooking).toString())
    }

    fun createBooking(body: String): String {
        return postRequest(body, url, 200)
    }

    fun getBookingId(id: Int): String {
        return getRequest("$url/$id", 200)
    }

    fun getBookingIdNotFound(id: Int): String {
        return getRequest("$url/$id", 404)
    }

    fun getBookingIds(): String {
        return getRequest(url, 200)
    }

    fun getBookingIds(query: Map<String, String>): String {
        return getRequest(query, url, 200)
    }

    fun deleteBookingWithCookieHeader(id: Int) {
        val header = Header("Cookie", "token=${AuthRequests().getAuthToken()}")
        deleteRequest(header, "$url/$id", 201)
    }

    fun deleteBookingWithAuthorisationHeader(id: Int) {
        // todo change to valid code:
        // Note: on site it appears to be "base64(username:password), but when I tryed that API returned 403
        val header = Header("Authorisation",
                "Basic ${Base64.getEncoder().encodeToString(AuthRequests().getCredentials().toByteArray())}")
        deleteRequest(header, "$url/$id", 201)
    }

    fun deleteBookingWithoutAuth(id: Int) {
        deleteRequest("$url/$id", 403)
    }
}