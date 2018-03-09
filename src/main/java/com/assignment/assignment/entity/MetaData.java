package com.assignment.assignment.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "META_DATA")

@NamedNativeQuery(
        name    =   "MetaData.getrecent",
        query   =   "SELECT * FROM META_DATA WHERE DATEDIFF(MINUTE,META_DATA.TIME ,SYSDATE)<60 ",
        resultClass=MetaData.class
)
public class MetaData {
    private Integer id;
    private String name;
    private String path;
    private Date time;



    private long size;

    public MetaData(Integer id, String name, Date time, int size) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.size = size;
    }


    @Id
    @GeneratedValue

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "NAME",length=100, nullable=false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "TIME")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    @Column(name = "SIZE")
    public long getSize() {
        return size;
    }

    public MetaData(String name, String path, Date time, long size) {
        this.name = name;
        this.path = path;
        this.time = time;
        this.size = size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    @Column(name = "PATH")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MetaData(Integer id, String name, String path, Date time, long size) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.time = time;
        this.size = size;
    }

    public MetaData() {

    }


}
