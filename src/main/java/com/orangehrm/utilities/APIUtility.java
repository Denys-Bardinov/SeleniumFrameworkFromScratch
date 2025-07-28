package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {

    //Method to send GET request
    public static Response sendGetRequest(String endPoint) {
        return RestAssured.get(endPoint);
    }

    //Method to send Post request
    public static Response sendPostRequest(String endPoint, String payload) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post();
    }

    //Method to validate status code
    public static boolean validateStatusCode(Response response, int statusCode) {
       return response.getStatusCode() == statusCode;
    }

    //Method to extract value from JSON response
    public static String getJsonValue(Response response, String value) {
      return response.jsonPath().getString(value);
    }
}
