package com.simplechat.repository;

import com.simplechat.model.UserByMobile;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public interface UserByMobileRepository extends CassandraRepository<UserByMobile, String> {


    @Query(value="select * from user_by_mobile where mobile in ?0")
    Set<UserByMobile> getUsersIdByMobile(Set<String> mobiles);
}
