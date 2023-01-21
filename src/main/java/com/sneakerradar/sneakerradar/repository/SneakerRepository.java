package com.sneakerradar.sneakerradar.repository;

import com.sneakerradar.sneakerradar.domain.Sneaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SneakerRepository extends JpaRepository<Sneaker, Long> {

    boolean existsByLink(String link);

    @Query("SELECT  s FROM Sneaker s WHERE lower(s.name) LIKE %:sneakerName%")
    Page<Sneaker> searchSneakerNamesPage(@Param("sneakerName") String sneakerName, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Sneaker s INNER JOIN s.sizes ss " +
            "WHERE ss.size = :shoeSize AND lower(s.name) LIKE %:sneakerName%")
    Page<Sneaker> searchSneakerNamesAndSizePage(@Param("sneakerName") String sneakerName,
                                                @Param("shoeSize") String shoeSize,
                                                Pageable pageable);

    @Query("SELECT DISTINCT s FROM Sneaker s INNER JOIN s.sizes ss WHERE ss.size = :shoeSize")
    Page<Sneaker> FilterByShoeSize(@Param("shoeSize") String shoeSize, Pageable pageable);
}
