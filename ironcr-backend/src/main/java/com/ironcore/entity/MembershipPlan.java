package com.ironcore.entity;

import jakarta.persistence.*;

/**
 * Enum representing membership plan types.
 * Must match the IDs from frontend: basic, standard, premium
 */
public enum MembershipPlan {
    BASIC,      // ₹999/month
    STANDARD,   // ₹2499/3 months
    PREMIUM     // ₹7999/year
}