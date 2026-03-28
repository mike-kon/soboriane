package com.mikesoft.soboriane.repositories;

import com.mikesoft.soboriane.dto.db.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
