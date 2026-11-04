package com.ironcore.repository;

import com.ironcore.entity.GalleryCategory;
import com.ironcore.entity.GalleryImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for GalleryImageEntity CRUD operations.
 *
 * Spring Data JPA auto-generates SQL for all methods here.
 * You do NOT write any SQL — just define the method name and JPA figures it out.
 */
@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImageEntity, Long> {

    /**
     * Find all gallery images for a specific category.
     * Used by frontend filter tabs: Gym, Equipment, Trainers, Members, Events.
     *
     * Example: findByCategory(GalleryCategory.TRAINERS) → all trainer images
     */
    List<GalleryImageEntity> findByCategory(GalleryCategory category);

    /**
     * Find all gallery images for a category, sorted by display order.
     * Used when you want to control the image display sequence.
     */
    List<GalleryImageEntity> findByCategoryOrderByDisplayOrderAsc(GalleryCategory category);

    /**
     * Find all gallery images sorted by display order.
     * Used for the "All" tab — returns everything in order.
     */
    List<GalleryImageEntity> findAllByOrderByDisplayOrderAsc();

    /**
     * Find all gallery images sorted by newest first.
     * Alternative view — latest uploads first.
     */
    List<GalleryImageEntity> findAllByOrderByCreatedAtDesc();
}
