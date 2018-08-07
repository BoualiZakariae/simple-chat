package com.simplechat.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("auth_key")
public class AuthKey implements Serializable {

    @PrimaryKey
    private String authkey;

    private int user_id;

    private Date create_at;

    public AuthKey() {
    }

    // @todo add time to live
}