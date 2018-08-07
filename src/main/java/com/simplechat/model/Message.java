package com.simplechat.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
//import org.springframework.data.cassandra.mapping.PrimaryKey;
//import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Table("message")
public class Message implements Serializable {

//    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @CassandraType(type = DataType.Name.TIMEUUID)
    private String id;

    @Column("user_id")
    @CassandraType(type = DataType.Name.UUID)
    private String userId;

    @Column("second_user_id")
    @CassandraType(type = DataType.Name.UUID)
    private String secondUserId;

    private String msg;

    public Message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return userId;
    }

    public void setSender_id(String sender_id) {
        this.userId = sender_id;
    }

    public String getTo_id() {
        return secondUserId;
    }

    public void setTo_id(String to_id) {
        this.secondUserId = to_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}