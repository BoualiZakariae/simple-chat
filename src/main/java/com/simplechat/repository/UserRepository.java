package com.simplechat.repository;

import com.simplechat.model.AuthKey;
import com.simplechat.model.User;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public interface UserRepository extends CrudRepository<User, String> {

    @Query(value="SELECT * FROM user WHERE mobile=?0")
    public User getIdByMobile(String mobile);

    @Query(value="INSERT INTO auth_key (user_id, authKey, create_at) values(?0, ?1, ?2)")
    public void addAuthKeyForUser(String userId, String authKey, Date date);

    @Query(value="select user_id from auth_key where authkey=?0")
    public String getUserByAuthKey(String authKey);

    @Query(value="select * from user where id in ?0")
    public Set<User> getUsersByIds(String userIds);

}
