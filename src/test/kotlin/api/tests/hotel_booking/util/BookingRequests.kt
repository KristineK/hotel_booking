package api.tests.hotel_booking.util

import api.tests.hotel_booking.mapping.Booking
import api.tests.hotel_booking.mapping.BookingDates
import api.tests.hotel_booking.util.AuthRequests.getAuthToken
import api.tests.hotel_booking.util.AuthRequests.getCredentials
import com.google.gson.Gson
import io.restassured.http.Header
import java.io.FileInputStream
import java.math.BigDecimal
import java.util.*

object BookingRequests : Requests() {
    private var url: String
    var defaultBooking = Booking("Jim",
        "Brown",
        BigDecimal.valueOf(111),
        true,
        BookingDates("2018-01-01",
            "2019-01-01"),
        "Breakfast")
    var authToken: String? = null
    var authCredentials: String? = null

    init {
        val props = Properties()
        props.load(FileInputStream("src/test/resources/config.properties"))
        url = props.getProperty("baseUrl") + props.getProperty("bookingPath")
    }

    private fun authCookieHeader(): Header {
        if (authToken.isNullOrEmpty()) {
            authToken = getAuthToken()
        }
        return Header("Cookie", "token=$authToken")
    }

    private fun basicAuthHeader(): Header {
        // TODO change to valid code:
        // Note: on site it appears to be "base64(username:password), but when I tryed that API returned 403
        if (authCredentials.isNullOrEmpty()) {
            authCredentials = Base64.getEncoder().encodeToString(getCredentials().toByteArray())
        }

        return Header("Authorisation",
            "Basic $authCredentials")
    }

    fun createDefaultBooking(): String {
        return createBooking(Gson().toJson(defaultBooking).toString())
    }

    fun createBooking(body: String): String {
        return postRequest(body, url, 200)
    }

    fun deleteBookingWithCookieHeader(id: Int, statusCode: Int): String {
        return deleteRequest(authCookieHeader(), "$url/$id", statusCode)
    }

    fun deleteBookingWithCookieHeader(id: Int): String {
        return deleteRequest(authCookieHeader(), "$url/$id", 201)
    }

    fun deleteBookingWithAuthorisationHeader(id: Int): String {
        return deleteRequest(basicAuthHeader(), "$url/$id", 201)
    }

    fun deleteBookingWithoutAuth(id: Int): String {
        return deleteRequest("$url/$id", 403)
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

    fun partialUpdateBookingWithoutHeader(body: String, id: Int): String {
        return putRequest(body, "$url/$id", 403)
    }

    fun partialUpdateBookingWithBasicAuthHeader(body: String, id: Int): String {
        return putRequest(body, basicAuthHeader(), "$url/$id", 200)
    }

    fun partialUpdateBookingWithCookieHeader(body: String, id: Int): String {
        return putRequest(body, authCookieHeader(), "$url/$id", 200)
    }
}