package com.sneakerradar.sneakerradar.service;

import com.sneakerradar.sneakerradar.domain.Sneaker;
import com.sneakerradar.sneakerradar.domain.SneakerSizes;
import com.sneakerradar.sneakerradar.domain.dto.SneakerDto;
import com.sneakerradar.sneakerradar.repository.SneakerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class SneakerService {

    private final SneakerRepository sneakerRepository;

    public SneakerService(SneakerRepository sneakerRepository) {
        this.sneakerRepository = sneakerRepository;
    }

    public void insert(String name, int price, String link, String imgSrc, String siteName, String brand, List<SneakerSizes> sizes) {
        Sneaker sneaker = new Sneaker(name, price, link, imgSrc, siteName, brand, sizes);
        sneakerRepository.save(sneaker);
    }

    public Page<SneakerDto> searchByNamePageable(String term, Pageable pageable) {
        Page<Sneaker> posts = sneakerRepository.searchSneakerNamesPage(term.toLowerCase(Locale.ROOT), pageable);
        Page<SneakerDto> dtoPage = posts.map(this::mapFromSneakerToDto);
        return dtoPage;
    }


    public Page<SneakerDto> sneakersPageable(Pageable pageable) {
        Page<Sneaker> posts = sneakerRepository.findAll(pageable);
        Page<SneakerDto> dtoPage = posts.map(this::mapFromSneakerToDto);
        return dtoPage;
    }

    public Page<SneakerDto> sneakersPageableBySize(String shoeSize, Pageable pageable) {
        Page<Sneaker> posts = sneakerRepository.FilterByShoeSize(shoeSize, pageable);
        Page<SneakerDto> dtoPage = posts.map(this::mapFromSneakerToDto);
        return dtoPage;
    }

    public Page<SneakerDto> sneakersPageableByNameAndSize(String term, String shoeSize, Pageable pageable) {
        Page<Sneaker> posts = sneakerRepository.searchSneakerNamesAndSizePage(term, shoeSize, pageable);
        Page<SneakerDto> dtoPage = posts.map(this::mapFromSneakerToDto);
        return dtoPage;
    }

    public boolean checkIfLinkExists(String link) {
        return sneakerRepository.existsByLink(link);
    }

    private SneakerDto mapFromSneakerToDto(Sneaker sneaker) {
        SneakerDto sneakerDto = new SneakerDto();
        sneakerDto.setName(sneaker.getName());
        sneakerDto.setPrice(sneaker.getPrice());
        sneakerDto.setLink(sneaker.getLink());
        sneakerDto.setImgSrc(sneaker.getImgSrc());
        sneakerDto.setSiteName(sneaker.getSiteName());
        sneakerDto.setBrand(sneaker.getBrand());
        sneakerDto.setSizes(sneaker.getSizes());
        return sneakerDto;
    }

}
