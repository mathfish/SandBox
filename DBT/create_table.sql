-- create the tables
DROP table if exists customers;
CREATE table customers
(
id serial primary key,
first_name varchar,
last_name varchar
);

DROP table if exists orders;
CREATE table orders
(
id serial primary key,
user_id integer references customers (id),
order_date date,
status varchar
);

DROP table if exists payments;
CREATE table payments
(
id serial primary key,
order_id integer references orders (id),
payment_method varchar,
amount integer
);

-- populate the table with data from the csv file. Download the file locally before completing this step
COPY customers 
FROM '/opt/raw_customers.csv' 
DELIMITER ','
CSV HEADER;

COPY orders
FROM '/opt/raw_orders.csv' 
DELIMITER ','
CSV HEADER;

COPY payments
FROM '/opt/raw_payments.csv' 
DELIMITER ','
CSV HEADER;
