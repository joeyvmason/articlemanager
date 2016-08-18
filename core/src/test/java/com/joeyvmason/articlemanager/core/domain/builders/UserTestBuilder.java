package com.joeyvmason.articlemanager.core.domain.builders;

import com.joeyvmason.articlemanager.core.domain.users.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserTestBuilder {

    private String emailAddress = String.format("foo%s@foobar.com", RandomStringUtils.randomAlphanumeric(10));
    private String firstName = RandomStringUtils.randomAlphanumeric(10);
    private String lastName = RandomStringUtils.randomAlphanumeric(10);
    private String password = RandomStringUtils.randomAlphanumeric(10);

    public static UserTestBuilder valid() {
        return new UserTestBuilder();
    }

    public User build() {
        return new User(emailAddress, firstName, lastName, password);
    }

    public UserTestBuilder withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserTestBuilder withPassword(String passowrd) {
        this.password = passowrd;
        return this;
    }
}
