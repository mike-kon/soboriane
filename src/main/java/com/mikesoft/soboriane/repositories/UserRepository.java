package com.mikesoft.soboriane.repositories;

import com.mikesoft.soboriane.dto.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
