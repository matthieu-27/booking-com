-- Seed data for booking-com
-- Usage: mariadb --default-character-set=utf8mb4 -u robert -phue spring-things-again < seed.sql

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE booking;
TRUNCATE TABLE room;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO room (name, capacity) VALUES
  ('Salle A', 10),
  ('Salle B', 20),
  ('Grande salle', 50),
  ('Salle de réunion', 8),
  ('Salle informatique', 30);

INSERT INTO booking (desired_at, scheduled_at, ended_at, room_id) VALUES
  ('2026-05-20', '09:00:00', NULL, 1),
  ('2026-05-20', '14:00:00', NULL, 2),
  ('2026-05-21', '10:00:00', NULL, 3),
  ('2026-05-22', '08:30:00', NULL, 1),
  ('2026-05-23', '16:00:00', NULL, 4);
