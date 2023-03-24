package com.shareitserver.user;

import com.shareitserver.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryServer extends JpaRepository<User, Long> {
}