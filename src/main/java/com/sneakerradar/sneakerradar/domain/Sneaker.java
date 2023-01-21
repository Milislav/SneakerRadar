package com.sneakerradar.sneakerradar.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SNEAKER")
public class Sneaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    private String name;
    private int price;
    @Column(unique=true)
    private String link;
    private String imgSrc;
    private String siteName;
    private String brand;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="SNEAKER_ID")
    private List<SneakerSizes> sizes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<SneakerSizes> getSizes() {
        return sizes;
    }

    public void setSizes(List<SneakerSizes> sizes) {
        this.sizes = sizes;
    }

    public Sneaker(String name, int price, String link, String imgSrc, String siteName, String brand, List<SneakerSizes> sizes) {
        this.name = name;
        this.price = price;
        this.link = link;
        this.imgSrc = imgSrc;
        this.siteName = siteName;
        this.brand = brand;
        this.sizes = sizes;
    }

    public Sneaker() {
    }
}
