package api.tests.hotel_booking.util

import api.tests.hotel_booking.mapping.getJsonBody
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.junit.jupiter.api.fail
import java.io.FileInputStream
import java.util.*


object AuthRequests : Requests() {
    private val url: String
    private val loginDetails: JsonObject

    init {
        val props = Properties()
        props.load(FileInputStream("src/test/resources/config.properties"))
        url = props.getProperty("baseUrl") + props.getProperty("authPath")
        loginDetails = JsonObject()
        // username and password is being added via command line (e.g. -Dusername=admin option) for security reasons
        // if the username/password are just for test users they can be moved to config.properties
        if (System.getProperty(Vocabulary.username.toString()).isNullOrEmpty() ||
            System.getProperty(Vocabulary.password.toString()).isNullOrEmpty())
            fail("Username and password properties need to be added.")
        loginDetails.addProperty(Vocabulary.username.toString(), System.getProperty(Vocabulary.username.toString()))
        loginDetails.addProperty(Vocabulary.password.toString(), System.getProperty(Vocabulary.password.toString()))
    }

    fun getCredentials(): String {
        return "${loginDetails.get(Vocabulary.username.toString()).asString}:${loginDetails.get(Vocabulary.password.toString()).asString}"
    }

    fun getAuthToken(): String {
        return getJsonBody(postRequest(loginDetails.toString(), url, 200), JsonObject::class.java).get("token").toString()
    }

}