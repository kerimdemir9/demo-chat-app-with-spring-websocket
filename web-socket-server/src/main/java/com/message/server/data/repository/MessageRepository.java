package com.message.server.data.repository;

import com.message.server.data.model.MessageModel;
import com.message.server.data.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
    List<MessageModel> findAllByFrom(UserModel from);
    List<MessageModel> findAllByTo(UserModel to);
}
