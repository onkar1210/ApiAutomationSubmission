package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CoindeskApiTest {
    @Test
    public void coindeskApiTest() throws IOException {
        RestAssured.baseURI = "https://api.coindesk.com";
        String response = given()
                .when().get("v1/bpi/currentprice.json")
                .then().log().all().assertThat().statusCode(200).body("bpi.size()", equalTo(3))
                .body("bpi.USD", notNullValue())
                .body("bpi.GBP", notNullValue())
                .body("bpi.EUR", notNullValue())
                .body("bpi.GBP.description", equalTo("British Pound Sterling")) // Check GBP description
                .extract().response().asString();

        System.out.println(response);
        JsonPath js = new JsonPath(response);

        Assert.assertEquals(3, js.getMap("bpi").size(), "BPI count does not match");
        Assert.assertTrue(js.getMap("bpi").containsKey("USD"), "USD key is missing");
        Assert.assertTrue(js.getMap("bpi").containsKey("GBP"), "GBP key is missing");
        Assert.assertTrue(js.getMap("bpi").containsKey("EUR"), "EUR key is missing");
        Assert.assertEquals("British Pound Sterling", js.getString("bpi.GBP.description"), "GBP description mismatch");

    }
}
