package com.joeyvmason.articlemanager.web.domain.auth;

import com.joeyvmason.articlemanager.web.application.auth.Authorized;
import com.joeyvmason.articlemanager.web.application.auth.LoggedInAuthToken;
import com.joeyvmason.articlemanager.web.application.auth.LoggedInUser;
import com.joeyvmason.articlemanager.core.domain.BadRequestException;
import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.auth.AuthTokenRepository;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.joeyvmason.articlemanager.core.domain.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    @Autowired
    public AuthController(UserRepository userRepository, AuthTokenRepository authTokenRepository) {
        this.userRepository = userRepository;
        this.authTokenRepository = authTokenRepository;
    }

    @RequestMapping(value = "/signup")
    public AuthToken shouldSignUp(@RequestBody LoginForm loginForm) {
        try {
            User user = userRepository.save(new User(loginForm.getEmailAddress(), null, null, loginForm.getPassword()));
            return login(user);
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("Account already exists with this email address");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AuthToken login(@RequestBody LoginForm login) {
        User user = userRepository.findByEmailAddressIgnoreCaseAndShaPassword(login.getEmailAddress(), User.getSha512(login.getPassword()));
        if (user == null) {
            logger.debug("User provided invalid credentials");
            throw new BadRequestException("Invalid credentials");
        }

        return login(user);
    }

    @Authorized
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User findMyself(@LoggedInUser User user) {
        logger.debug("Retrieving LoggedInUser({})", user.getId());
        return user;
    }

    @Authorized
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(@LoggedInAuthToken AuthToken authToken) {
        logger.debug("Deleting AuthToken({})", authToken.getId());
        authTokenRepository.delete(authToken);
    }

    private AuthToken login(User user) {
        logger.debug("Logging in User({})", user.getId());
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken = authTokenRepository.save(authToken);

        userRepository.save(user);

        return authToken;
    }

    public static class LoginForm {
        private String emailAddress;
        private String password;

        public LoginForm() {}

        public LoginForm(String emailAddress, String password) {
            this.emailAddress = emailAddress;
            this.password = password;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
