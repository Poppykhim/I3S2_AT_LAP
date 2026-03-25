package lab01;

import javax.naming.AuthenticationException;

public class Authentication {
    private User user;
    private String apiUrl = "https://dogapi.dog/api/v2/breeds";
    public User login(String email, String password) throws AuthenticationException {
        throw new AuthenticationException("Invalid email or password!");
    }
}
