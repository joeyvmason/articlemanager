package com.joeyvmason.articlemanager.core.domain.users;

import com.joeyvmason.articlemanager.core.BaseIntegrationTest;
import com.joeyvmason.articlemanager.core.domain.builders.UserTestBuilder;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.joeyvmason.articlemanager.core.domain.users.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test(expectedExceptions = DuplicateKeyException.class)
    public void shouldNotSaveDuplicateEmailAddress() {
        //given
        String emailAddress = RandomStringUtils.randomAlphanumeric(10);
        userRepository.save(UserTestBuilder.valid().withEmailAddress(emailAddress).build());
        userRepository.save(UserTestBuilder.valid().withEmailAddress(emailAddress).build());
    }

    @Test
    public void shouldSaveAndRetrieveUser() {
        //given
        User user = UserTestBuilder.valid().build();

        //when
        user = userRepository.save(user);

        //then
        User userFromDB = userRepository.findOne(user.getId());
        assertThat(userFromDB).isEqualTo(user);
    }

    @Test
    public void shouldUpdateUser() {
        //given
        User user = UserTestBuilder.valid().build();

        //when
        user.setFirstName("Brian");
        user = userRepository.save(user);

        User userFromDB = userRepository.save(user);
        assertThat(userFromDB.getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    public void shouldRetrieveByEmailAddressCaseInsensitively() {
        String emailAddress = RandomStringUtils.randomAlphanumeric(20);
        User user = userRepository.save(UserTestBuilder.valid().withEmailAddress(emailAddress.toLowerCase()).build());

        User userFromDB = userRepository.findByEmailAddressIgnoreCase(emailAddress.toUpperCase());
        assertThat(userFromDB).isEqualTo(user);
    }

    @Test
    public void shouldFindByUserAndShaPasswordCaseInsensitively() {
        String emailAddress = RandomStringUtils.randomAlphanumeric(20);
        User user = userRepository.save(UserTestBuilder.valid().withEmailAddress(emailAddress.toLowerCase()).build());

        User userFromDB = userRepository.findByEmailAddressIgnoreCaseAndShaPassword(emailAddress.toUpperCase(), user.getShaPassword());
        assertThat(userFromDB).isEqualTo(user);
    }

//    @Test
//    public void shouldFindByEmailAddressOrFullName() {
//        userRepository.save(UserTestBuilder.valid().build());
//        User user = userRepository.save(UserTestBuilder.valid().withFirstName("joseph").withLastName("mason").withEmailAddress("joeyvmason@gmail.com").build());
//
//        List<User> users = userRepository.findByEmailAddressLikeIgnoreCaseOrFullNameLikeIgnoreCase("OSEPH MAS", "OSEPH MAS");
//        assertThat(users).containsOnly(user);
//
//        users = userRepository.findByEmailAddressLikeIgnoreCaseOrFullNameLikeIgnoreCase("joeyvmason", "joeyvmason");
//        assertThat(users).containsOnly(user);
//    }

}