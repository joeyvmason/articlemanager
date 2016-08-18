package com.joeyvmason.articlemanager.core.domain.users;

import com.joeyvmason.articlemanager.core.domain.BaseRepository;

public interface UserRepository extends BaseRepository<User> {

    User findByEmailAddressIgnoreCase(String emailAddress);

    User findByEmailAddressIgnoreCaseAndShaPassword(String emailAddress, byte[] shaPassword);

}
