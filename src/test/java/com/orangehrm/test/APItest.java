package com.orangehrm.test;

import com.orangehrm.utilities.APIUtility;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APItest {

    @Test
    public void verifyGetUserAPI() {

        //Step 1: Define API endpoint
        String endpoint = "https://jsonplaceholder.typicode.com/users/1";

        //Step 2: Send GET request
        Response response = APIUtility.sendGetRequest(endpoint);

        //Step 3: validate status code
        boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);

        Assert.assertTrue(isStatusCodeValid, "Status code is not as Expected");

        //Step 4: validate username
        String username = APIUtility.getJsonValue(response, "username");
        boolean isUserNameValid = "Bret".equals(username);

        Assert.assertTrue(isUserNameValid, "Username is not Valid");



    }
}
