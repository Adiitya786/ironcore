// API Service Layer - Complete Backend Integration
// Base URL for all API calls
const API_BASE = "https://gym-trial.onrender.com/api";

// =====================================================
// AUTHENTICATION & TOKEN MANAGEMENT
// =====================================================

/**
 * Get JWT token from localStorage
 */
const getToken = () => {
  return localStorage.getItem('token');
};

/**
 * Get user role from localStorage
 */
export const getUserRole = () => {
  return localStorage.getItem('role');
};

/**
 * Check if user is logged in
 */
export const isAuthenticated = () => {
  return !!getToken();
};

/**
 * Check if user is admin
 */
export const isAdmin = () => {
  return getUserRole() === 'ROLE_ADMIN';
};

/**
 * Logout user (clear token and role)
 */
export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('role');
  localStorage.removeItem('username');
  localStorage.removeItem('email');
};

/**
 * Get headers with Authorization token
 */
const getHeaders = (isMultipart = false) => {
  const headers = {};
  const token = getToken();

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  // Don't set Content-Type for multipart/form-data (browser sets it with boundary)
  if (!isMultipart) {
    headers['Content-Type'] = 'application/json';
  }

  return headers;
};

// =====================================================
// AUTHENTICATION APIs
// =====================================================

export const authApi = {
  /**
   * Login user
   * POST /api/auth/login
   */
  login: async (username, password) => {
    const response = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'Login failed' }));
      throw new Error(error.message || 'Invalid credentials');
    }

    const data = await response.json();

    // Store token and user info
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('username', data.username);
    localStorage.setItem('email', data.email);

    return data;
  },

  /**
   * Register new user
   * POST /api/auth/register
   */
  register: async (username, email, password) => {
    const response = await fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: 'Registration failed' }));
      throw new Error(error.message || 'Registration failed');
    }

    const data = await response.json();

    // Store token and user info
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('username', data.username);
    localStorage.setItem('email', data.email);

    return data;
  }
};

// =====================================================
// PROGRAMS API
// =====================================================

export const programsApi = {
  /**
   * Get all programs (PUBLIC)
   * GET /api/programs
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/programs`);
    if (!response.ok) throw new Error('Failed to fetch programs');
    return await response.json();
  },

  /**
   * Get single program by ID (PUBLIC)
   * GET /api/programs/{id}
   */
  getById: async (id) => {
    const response = await fetch(`${API_BASE}/programs/${id}`);
    if (!response.ok) throw new Error('Program not found');
    return await response.json();
  },

  /**
   * Create new program (ADMIN ONLY)
   * POST /api/programs
   */
  create: async (formData) => {
    const response = await fetch(`${API_BASE}/programs`, {
      method: 'POST',
      headers: getHeaders(true), // multipart/form-data
      body: formData // FormData object with title, description, benefits, imageFile
    });

    if (!response.ok) throw new Error('Failed to create program');
    return await response.json();
  },

  /**
   * Update program (ADMIN ONLY)
   * PUT /api/programs/{id}
   */
  update: async (id, formData) => {
    const response = await fetch(`${API_BASE}/programs/${id}`, {
      method: 'PUT',
      headers: getHeaders(true),
      body: formData
    });

    if (!response.ok) throw new Error('Failed to update program');
    return await response.json();
  },

  /**
   * Delete program (ADMIN ONLY)
   * DELETE /api/programs/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/programs/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete program');
    return true;
  }
};

// =====================================================
// EQUIPMENT API
// =====================================================

export const equipmentApi = {
  /**
   * Get all equipment (PUBLIC)
   * GET /api/equipment
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/equipment`);
    if (!response.ok) throw new Error('Failed to fetch equipment');
    return await response.json();
  },

  /**
   * Get equipment by category (PUBLIC)
   * GET /api/equipment/category/{category}
   */
  getByCategory: async (category) => {
    const response = await fetch(`${API_BASE}/equipment/category/${category}`);
    if (!response.ok) throw new Error('Failed to fetch equipment');
    return await response.json();
  },

  /**
   * Create equipment (ADMIN ONLY)
   * POST /api/equipment
   */
  create: async (formData) => {
    const response = await fetch(`${API_BASE}/equipment`, {
      method: 'POST',
      headers: getHeaders(true),
      body: formData // name, category, description, imageFile
    });

    if (!response.ok) throw new Error('Failed to create equipment');
    return await response.json();
  },

  /**
   * Update equipment (ADMIN ONLY)
   * PUT /api/equipment/{id}
   */
  update: async (id, formData) => {
    const response = await fetch(`${API_BASE}/equipment/${id}`, {
      method: 'PUT',
      headers: getHeaders(true),
      body: formData
    });

    if (!response.ok) throw new Error('Failed to update equipment');
    return await response.json();
  },

  /**
   * Delete equipment (ADMIN ONLY)
   * DELETE /api/equipment/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/equipment/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete equipment');
    return true;
  }
};

// =====================================================
// TRAINERS API
// =====================================================

export const trainersApi = {
  /**
   * Get all trainers (PUBLIC)
   * GET /api/trainers
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/trainers`);
    if (!response.ok) throw new Error('Failed to fetch trainers');
    return await response.json();
  },

  /**
   * Create trainer (ADMIN ONLY)
   * POST /api/trainers
   */
  create: async (formData) => {
    const response = await fetch(`${API_BASE}/trainers`, {
      method: 'POST',
      headers: getHeaders(true),
      body: formData // name, bio, imageFile
    });

    if (!response.ok) throw new Error('Failed to create trainer');
    return await response.json();
  },

  /**
   * Update trainer (ADMIN ONLY)
   * PUT /api/trainers/{id}
   */
  update: async (id, formData) => {
    const response = await fetch(`${API_BASE}/trainers/${id}`, {
      method: 'PUT',
      headers: getHeaders(true),
      body: formData
    });

    if (!response.ok) throw new Error('Failed to update trainer');
    return await response.json();
  },

  /**
   * Delete trainer (ADMIN ONLY)
   * DELETE /api/trainers/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/trainers/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete trainer');
    return true;
  }
};

// =====================================================
// GALLERY API
// =====================================================

export const galleryApi = {
  /**
   * Get all gallery images (PUBLIC)
   * GET /api/gallery
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/gallery`);
    if (!response.ok) throw new Error('Failed to fetch gallery');
    return await response.json();
  },

  /**
   * Get gallery images by category (PUBLIC)
   * GET /api/gallery/category/{category}
   */
  getByCategory: async (category) => {
    const response = await fetch(`${API_BASE}/gallery/category/${category}`);
    if (!response.ok) throw new Error('Failed to fetch gallery');
    return await response.json();
  },

  /**
   * Upload gallery image (ADMIN ONLY)
   * POST /api/gallery
   */
  create: async (formData) => {
    const response = await fetch(`${API_BASE}/gallery`, {
      method: 'POST',
      headers: getHeaders(true),
      body: formData // title, category, description, displayOrder, imageFile
    });

    if (!response.ok) throw new Error('Failed to upload image');
    return await response.json();
  },

  /**
   * Update gallery image (ADMIN ONLY)
   * PUT /api/gallery/{id}
   */
  update: async (id, formData) => {
    const response = await fetch(`${API_BASE}/gallery/${id}`, {
      method: 'PUT',
      headers: getHeaders(true),
      body: formData
    });

    if (!response.ok) throw new Error('Failed to update image');
    return await response.json();
  },

  /**
   * Delete gallery image (ADMIN ONLY)
   * DELETE /api/gallery/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/gallery/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete image');
    return true;
  }
};

// =====================================================
// TRIALS API (PUBLIC SUBMISSION + ADMIN VIEW)
// =====================================================

export const trialsApi = {
  /**
   * Book a trial session (PUBLIC)
   * POST /api/trials
   */
  create: async (data) => {
    const response = await fetch(`${API_BASE}/trials`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (!response.ok) throw new Error('Failed to book trial');
    return await response.json();
  },

  /**
   * Get all trial requests (ADMIN ONLY)
   * GET /api/trials
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/trials`, {
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to fetch trial requests');
    return await response.json();
  },

  /**
   * Update trial request status (ADMIN ONLY)
   * PUT /api/trials/{id}/status?status={NEW|CONTACTED|CONFIRMED|REJECTED}
   */
  updateStatus: async (id, status) => {
    const response = await fetch(`${API_BASE}/trials/${id}/status?status=${status}`, {
      method: 'PUT',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to update status');
    return await response.json();
  },

  /**
   * Delete trial request (ADMIN ONLY)
   * DELETE /api/trials/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/trials/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete trial request');
    return true;
  }
};

// =====================================================
// MEMBERSHIP REQUESTS API
// =====================================================

export const membershipApi = {
  /**
   * Submit membership join request (PUBLIC)
   * POST /api/membership-requests
   */
  create: async (data) => {
    const response = await fetch(`${API_BASE}/membership-requests`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (!response.ok) throw new Error('Failed to submit membership request');
    return await response.json();
  },

  /**
   * Get all membership requests (ADMIN ONLY)
   * GET /api/membership-requests
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/membership-requests`, {
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to fetch membership requests');
    return await response.json();
  },

  /**
   * Update membership request status (ADMIN ONLY)
   * PUT /api/membership-requests/{id}/status?status={NEW|CONTACTED|CONFIRMED|REJECTED}
   */
  updateStatus: async (id, status) => {
    const response = await fetch(`${API_BASE}/membership-requests/${id}/status?status=${status}`, {
      method: 'PUT',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to update status');
    return await response.json();
  },

  /**
   * Delete membership request (ADMIN ONLY)
   * DELETE /api/membership-requests/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/membership-requests/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete request');
    return true;
  }
};

// =====================================================
// CONTACT MESSAGES API
// =====================================================

export const contactApi = {
  /**
   * Send contact message (PUBLIC)
   * POST /api/contact-messages
   */
  create: async (data) => {
    const response = await fetch(`${API_BASE}/contact-messages`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (!response.ok) throw new Error('Failed to send message');
    return await response.json();
  },

  /**
   * Get all contact messages (ADMIN ONLY)
   * GET /api/contact-messages
   */
  getAll: async () => {
    const response = await fetch(`${API_BASE}/contact-messages`, {
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to fetch messages');
    return await response.json();
  },

  /**
   * Update contact message status (ADMIN ONLY)
   * PUT /api/contact-messages/{id}/status?status={NEW|READ|RESPONDED|CLOSED}
   */
  updateStatus: async (id, status) => {
    const response = await fetch(`${API_BASE}/contact-messages/${id}/status?status=${status}`, {
      method: 'PUT',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to update status');
    return await response.json();
  },

  /**
   * Delete contact message (ADMIN ONLY)
   * DELETE /api/contact-messages/{id}
   */
  delete: async (id) => {
    const response = await fetch(`${API_BASE}/contact-messages/${id}`, {
      method: 'DELETE',
      headers: getHeaders()
    });

    if (!response.ok) throw new Error('Failed to delete message');
    return true;
  }
};

// =====================================================
// LEGACY API (backwards compatibility)
// =====================================================

export const api = {
  // Legacy methods that map to new API
  createTrial: trialsApi.create,
  createLead: membershipApi.create,
  sendContactMessage: contactApi.create,
  getPrograms: programsApi.getAll,
  getEquipment: equipmentApi.getAll,
  getTrainers: trainersApi.getAll
};
