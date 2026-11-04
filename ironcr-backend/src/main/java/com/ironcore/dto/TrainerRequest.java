package com.ironcore.dto;

/**
 * Data Transfer Object for Trainer request.
 * Used to receive trainer data from client.
 */
public class TrainerRequest {

    // Trainer name - required
    private String name;

    // Trainer bio/description
    private String bio;

    // Image filename (for update)
    private String imageName;

    // Default constructor
    public TrainerRequest() {}

    // ===== Getter and Setter Methods =====

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}