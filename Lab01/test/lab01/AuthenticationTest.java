package lab01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthenticationTest {

    @Test
    public void testLoginEmptyFields() throws IOException, InterruptedException {
        String url = "http://localhost:3000/auth/login";
        String email = "john@mail.com";
        String pwd = "changeme";

        // Building the JSON payload string
        String jsonPayload = "{\"email\":\"" + email + "\",\"password\":\"" + pwd + "\"}";

        HttpClient http = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        // Sending the request and receiving the response
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        // JUnit 5 syntax: assertEquals(expected, actual)
        assertEquals(200, response.statusCode());
    }
}
