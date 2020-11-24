create schema if not exists sorder;

create table if not exists orders (
  id UUID PRIMARY KEY,
  forename VARCHAR(256),
  surname VARCHAR(256),
  city VARCHAR(256),
  street VARCHAR(256),
  post_code VARCHAR(256),
  phone_number VARCHAR(256),
  email VARCHAR(256),
  comment VARCHAR(256),
  time TIMESTAMP,
  user_id UUID,
  price NUMERIC,
  restaurant_id UUID,
  payed BOOLEAN,
  payu_url TEXT,
  payu_order_id VARCHAR(256)
);

create table if not exists meals (
  id UUID PRIMARY KEY,
  order_id UUID REFERENCES orders(id),
  name VARCHAR(256),
  image VARCHAR(256),
  price NUMERIC,
  ingredients VARCHAR(256),
  time_to_do NUMERIC,
  quantity INTEGER
);

create table if not exists payments (
  restaurant_id UUID PRIMARY KEY,
  pos_id VARCHAR(256),
  md5 VARCHAR(256),
  client_id VARCHAR(256),
  client_secret VARCHAR(256)
);