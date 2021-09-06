package api.tests.hotel_booking.util

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.config.ObjectMapperConfig.objectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import io.restassured.http.Header
import io.restassured.http.Headers
import io.restassured.mapper.ObjectMapperType
import org.assertj.core.api.Assertions.assertThat

open class Requests {
    init {
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(objectMapperConfig().defaultObjectMapperType(ObjectMapperType.GSON))
    }

    fun deleteRequest(token: Header, url: String, statusCode: Int): String {
        val response = given()
            .header(token)
            .`when`()
            .delete(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for DELETE call to url $url with header '$token' " +
                "and response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }

    fun deleteRequest(url: String, statusCode: Int): String {
        val response = given()
            .`when`()
            .delete(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for DELETE call to url $url with response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }

    fun getRequest(url: String, statusCode: Int): String {
        val response = given()
            .header("Accept", "application/json")
            .`when`()
            .get(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for GET call to url $url with response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }

    fun getRequest(queryParams: Map<String, String>, url: String, statusCode: Int): String {
        val response = given()
            .header("Accept", "application/json")
            .queryParams(queryParams)
            .`when`()
            .get(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for GET call to url $url with query param ${queryParams.toString()}" +
                " and response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }

    fun postRequest(body: String, url: String, statusCode: Int): String {
        val response = given()
            .contentType(ContentType.JSON)
            .body(body)
            .`when`()
            .post(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for POST call to url $url with body $body" +
                " and response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }

    fun putRequest(body: String, headers: Headers, url: String, statusCode: Int): String {
        val response = given()
            .contentType(ContentType.JSON)
            .headers(headers)
            .body(body)
            .`when`()
            .put(url)
            .then()
            .extract()
        assertThat(response.statusCode())
            .`as`("Incorrect status code for PUT call to url $url with body $body" +
                ", headers $headers and response ${response.body().asString()}.")
            .isEqualTo(statusCode)
        return response.body().asString()
    }
}