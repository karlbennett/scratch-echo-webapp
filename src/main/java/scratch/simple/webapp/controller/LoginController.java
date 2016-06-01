package scratch.simple.webapp.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Karl Bennett
 */
@Controller
@RequestMapping("/rest")
public class LoginController {

    @RequestMapping(method = POST, path = "/login", consumes = "application/json")
    public String login(@RequestBody Login login) {
        return format("forward:/login?username=%s&password=%s", login.getUsername(), login.getPassword());
    }

    public static class Login {

        private final String username;
        private final String password;

        public Login(@JsonProperty("username") String username, @JsonProperty("password") String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
