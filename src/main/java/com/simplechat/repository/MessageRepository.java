package com.simplechat.repository;

import java.util.List;
import java.util.UUID;

import com.simplechat.model.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {

    @Query(value="SELECT * FROM message WHERE user_id=?0 and second_user_id=?1 LIMIT 15")
    public List<Message> messagesGetHistory(UUID senderId, UUID toUserId);

    @Query(value="SELECT * FROM message WHERE user_id=?0 and second_user_id=?1 and id>?2 LIMIT 15")
    public List<Message> messagesGetHistoryWithOffset(UUID senderId, UUID toUserId, UUID offsetMessageId);

}
