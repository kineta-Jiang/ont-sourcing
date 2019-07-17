package com.github.ontio.sourcing.model.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_event")
public class Event {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String txhash;

    @Column
    private String event;

    @Column
    private int height;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
