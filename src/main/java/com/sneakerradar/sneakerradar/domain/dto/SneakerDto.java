package com.sneakerradar.sneakerradar.domain.dto;

import com.sneakerradar.sneakerradar.domain.SneakerSizes;

import java.util.List;

public class SneakerDto {
    private String name;
    private int price;
    private String link;
    private String imgSrc;
    private String siteName;
    private String brand;
    private List<SneakerSizes> sizes;

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getBrand() {
        return brand;
    }

    public List<SneakerSizes> getSizes() {
        return sizes;
    }

    public void setSizes(List<SneakerSizes> sizes) {
        this.sizes = sizes;
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

    public void setLink(String link) {
        this.link = link;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
