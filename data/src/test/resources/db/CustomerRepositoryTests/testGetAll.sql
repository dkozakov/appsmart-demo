CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into customers (id, created_at, is_deleted, title) values (uuid_generate_v4(), CURRENT_TIMESTAMP, FALSE, 'Customer 1');
