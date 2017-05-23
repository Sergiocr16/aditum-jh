package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Brand;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Brand entity.
 */
@SuppressWarnings("unused")
public interface BrandRepository extends JpaRepository<Brand,Long> {

}
