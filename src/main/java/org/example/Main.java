package org.example;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class Main {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String token = given()
                .contentType("application/json")
                .body("{ \"username\" : \"admin\", \"password\" : \"password123\" }")
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        String bookingPayload = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2023-01-01\",\n" +
                "        \"checkout\" : \"2023-01-02\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        int bookingId = given()
                .contentType("application/json")
                .body(bookingPayload)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        given()
                .cookie("token", token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .statusCode(201);

        System.out.println("Booking with ID " + bookingId + " has been successfully deleted.");
    }
}
