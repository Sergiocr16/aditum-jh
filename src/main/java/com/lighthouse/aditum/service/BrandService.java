package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Brand;
import java.util.List;

/**
 * Service Interface for managing Brand.
 */
public interface BrandService {

    /**
     * Save a brand.
     *
     * @param brand the entity to save
     * @return the persisted entity
     */
    Brand save(Brand brand);

    /**
     *  Get all the brands.
     *  
     *  @return the list of entities
     */
    List<Brand> findAll();

    /**
     *  Get the "id" brand.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Brand findOne(Long id);

    /**
     *  Delete the "id" brand.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
