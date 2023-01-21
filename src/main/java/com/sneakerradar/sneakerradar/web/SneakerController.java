package com.sneakerradar.sneakerradar.web;

import com.sneakerradar.sneakerradar.domain.dto.SneakerDto;
import com.sneakerradar.sneakerradar.service.SneakerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sneakers")
@CrossOrigin(origins = "http://localhost:4200")
public class SneakerController {

    private final SneakerService sneakerService;

    public SneakerController(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @GetMapping("/searchPageable/{name}")
    public ResponseEntity<Page<SneakerDto>> showAllSneakersByNamePage(@PathVariable String name,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "24") int size,
                                                                      @RequestParam(required = false) String sort,
                                                                      @RequestParam(required = false) String shoesSize) {
        Pageable paging = PageRequest.of(page, size);

        if (sort != null) {
            if (sort.equals("ascending"))
                paging = PageRequest.of(page, size, Sort.by("price").ascending());
            if (sort.equals("descending"))
                paging = PageRequest.of(page, size, Sort.by("price").descending());
        }
        if (shoesSize != null && !shoesSize.isEmpty()) {
            return new ResponseEntity<>(sneakerService.sneakersPageableByNameAndSize(name, shoesSize, paging), HttpStatus.OK);
        }

        return new ResponseEntity<>(sneakerService.searchByNamePageable(name, paging), HttpStatus.OK);
    }

    @GetMapping(value = "/listPageable")
    ResponseEntity<Page<SneakerDto>> sneakersPageable(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "24") int size,
                                                      @RequestParam(required = false) String sort,
                                                      @RequestParam(required = false) String shoesSize) {
        Pageable paging = PageRequest.of(page, size);

        if (sort != null) {
            if (sort.equals("ascending"))
                paging = PageRequest.of(page, size, Sort.by("price").ascending());
            if (sort.equals("descending"))
                paging = PageRequest.of(page, size, Sort.by("price").descending());
        }
        if (shoesSize != null && !shoesSize.isEmpty()) {
            return new ResponseEntity<>(sneakerService.sneakersPageableBySize(shoesSize, paging), HttpStatus.OK);
        }

        return new ResponseEntity<>(sneakerService.sneakersPageable(paging), HttpStatus.OK);
    }
}
