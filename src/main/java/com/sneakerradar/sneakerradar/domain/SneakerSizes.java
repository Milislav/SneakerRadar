package com.sneakerradar.sneakerradar.domain;

import javax.persistence.*;

@Entity
public class SneakerSizes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    private String size;

    public SneakerSizes(String size) {
        this.size = size;
    }

    public SneakerSizes() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
