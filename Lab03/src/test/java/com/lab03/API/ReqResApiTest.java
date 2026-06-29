package com.lab03.API;

import com.lab03.utils.TestListener;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ReqResApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @DataProvider(name = "userCreationData")
    public Object[][] getUserCreationData() {
        return new Object[][]{
                {"Alice", "Manager"},
                {"Bob", "Developer"},
                {"Charlie", "QA Automation Engineer"}
        };
    }

    @Test(dataProvider = "userCreationData", priority = 1)
    public void testCreateUser_DataDriven(String name, String job) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("job", job);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        // Validate response body contents
        String responseBody = response.asString();
        Assert.assertEquals(response.jsonPath().getString("name"), name, "Name mismatch");
        Assert.assertEquals(response.jsonPath().getString("job"), job, "Job title mismatch");
        Assert.assertNotNull(response.jsonPath().getString("id"), "User ID should be generated");
        Assert.assertNotNull(response.jsonPath().getString("createdAt"), "Timestamp should be generated");

        // Log request and response to ExtentReport
        if (TestListener.getTest() != null) {
            TestListener.getTest().info("Request Body: " + requestBody);
            TestListener.getTest().info("Response Status: " + response.getStatusCode());
            TestListener.getTest().info("Response Body: " + responseBody);
        }
    }

    @Test(priority = 2)
    public void testGetUser_SchemaValidation() {
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/user-schema.json");
        Assert.assertNotNull(schemaStream, "user-schema.json could not be loaded from classpath");

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(schemaStream))
                .extract().response();

        if (TestListener.getTest() != null) {
            TestListener.getTest().info("GET User endpoint: /api/users/2");
            TestListener.getTest().info("Response Status: " + response.getStatusCode());
            TestListener.getTest().info("Schema validation check passed successfully");
            TestListener.getTest().info("Response: " + response.asString());
        }
    }

    @Test(priority = 3)
    public void testUserNotFound_ErrorValidation() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .extract().response();

        if (TestListener.getTest() != null) {
            TestListener.getTest().info("GET User endpoint: /api/users/23");
            TestListener.getTest().info("Response Status: " + response.getStatusCode() + " (Expected 404)");
            TestListener.getTest().info("Error response body: " + response.asString());
        }
    }
}
