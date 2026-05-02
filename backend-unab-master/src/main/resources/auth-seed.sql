-- =========================================
-- STARTUP USERS AND ROLES
-- Idempotent seed for local/dev environments
-- Change these credentials after first use
-- =========================================

INSERT IGNORE INTO roles (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_STAFF');

INSERT IGNORE INTO users (full_name, username, email, password, enabled, created_at) VALUES
    ('System Administrator', 'admin', 'admin@veterinary.local', '$2a$10$uwq8TV6jQp9qLBi9LWO1ReITVk.VNgTHbVXeeheD9CPnE9C.9J/CG', 1, NOW()),
    ('Support Staff', 'staff', 'staff@veterinary.local', '$2a$10$hWcLinFMnlTYzPVe471G5.ohKV8XikIDP086mfa5HjiiYqJYn8nVW', 1, NOW());

INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
INNER JOIN roles ON roles.name = 'ROLE_ADMIN'
WHERE users.username = 'admin';

INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT users.id, roles.id
FROM users
INNER JOIN roles ON roles.name = 'ROLE_STAFF'
WHERE users.username = 'staff';
