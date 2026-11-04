package com.ironcore.entity;

/**
 * User role types for Role-Based Access Control (RBAC).
 *
 * ROLE_USER: Regular gym member/visitor
 *   - Can book trials
 *   - Can submit membership join requests
 *   - Can send contact messages
 *   - Can view programs, equipment, trainers, gallery (GET only)
 *
 * ROLE_ADMIN: Gym owner/administrator
 *   - Can do everything USER can do
 *   - Plus: Create/Update/Delete programs, equipment, trainers, gallery images
 *   - Plus: View/Update/Delete membership requests, contact messages
 *   - Plus: Manage leads, update statuses, view analytics
 */
public enum UserRole {
    ROLE_USER,   // Regular visitor/member
    ROLE_ADMIN   // Gym owner/administrator
}
