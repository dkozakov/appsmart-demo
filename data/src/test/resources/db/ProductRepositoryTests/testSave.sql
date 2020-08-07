CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into customers (id, created_at, is_deleted, title) values ('b19d597c-8f54-41ba-ba73-02299c1adf92', CURRENT_TIMESTAMP, FALSE, 'Customer 1');
insert into products (id, customer_id, created_at, is_deleted, title, price) values (uuid_generate_v4(), 'b19d597c-8f54-41ba-ba73-02299c1adf92', CURRENT_TIMESTAMP, FALSE, 'Product 1', 1.1);
insert into products (id, customer_id, created_at, is_deleted, title, price) values (uuid_generate_v4(), 'b19d597c-8f54-41ba-ba73-02299c1adf92', CURRENT_TIMESTAMP, FALSE, 'Product 2', 2.2);
