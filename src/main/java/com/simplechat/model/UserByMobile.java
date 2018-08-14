package com.simplechat.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Table("user_by_mobile")
public class UserByMobile implements Serializable {

    @CassandraType(type = DataType.Name.UUID)
    private UUID id;

    @PrimaryKey
    @Column
    private String mobile;

    public UserByMobile() {
    }

    public UserByMobile(String mobile, UUID id) {
        this.mobile = mobile;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}