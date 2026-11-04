package com.ironcore.repository;

import com.ironcore.entity.Equipment;
import com.ironcore.entity.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Equipment entity.
 * Provides database operations for equipment data.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Find all equipment by category
    List<Equipment> findByCategory(EquipmentCategory category);

    // Find all equipment ordered by category and name
    List<Equipment> findAllByOrderByCategoryAscNameAsc();
}