package com.ironcore.entity;

/**
 * Gallery image categories.
 * Matches the frontend Gallery.jsx filter tabs exactly:
 * cats = ["All", "Gym", "Equipment", "Trainers", "Members", "Events"]
 *
 * "All" is handled by returning every category — no enum value needed.
 */
public enum GalleryCategory {
    GYM,
    EQUIPMENT,
    TRAINERS,
    MEMBERS,
    EVENTS
}
