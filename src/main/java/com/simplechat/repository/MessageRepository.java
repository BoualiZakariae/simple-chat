package com.simplechat.repository;

import java.util.List;

import com.simplechat.model.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface MessageRepository extends CrudRepository<Message, String> {

    @Query(value="SELECT * FROM message WHERE id=1 and user_id=?0 and second_user_id=?1")
    public List<Message> findBySenderAndReciever(String sender, String receiver);

    @Query(value="SELECT * FROM message WHERE id=1 and user_id=?0 and second_user_id=?1 LIMIT 20;")
    public List<Message> messagesGetHistory(String senderId, String peer, int offset);

}
