-- Insert user record
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'John', 'Doe', 'john.doe', '{noop}password', true);

-- Insert trainee record
INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (1, DATE '2000-08-15', '123 Main St', 1);
