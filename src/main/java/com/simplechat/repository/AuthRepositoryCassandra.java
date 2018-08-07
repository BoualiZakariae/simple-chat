package com.simplechat.repository;

import java.util.List;

import com.simplechat.model.User;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface AuthRepositoryCassandra extends CrudRepository<User, String> {




}
