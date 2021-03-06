create schema if not exists restaurant;

create table if not exists restaurants (
  id UUID PRIMARY KEY,
  name VARCHAR(256),
  image VARCHAR(256),
  owner_id UUID,
  city VARCHAR(256),
  street VARCHAR(256),
  house_number VARCHAR(256),
  phone_number VARCHAR(256),
  deleted boolean,
  payment_online boolean,
  description VARCHAR(256),
  nip VARCHAR(256),
  regon VARCHAR(256),
  post_code VARCHAR(256),
  opinion_id UUID,
  rate NUMERIC,
  category TEXT
);

create table if not exists opinions (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  user_id UUID,
  rate NUMERIC,
  description VARCHAR(256)
);

create table if not exists tables (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  name VARCHAR(256),
  number_of_seats INTEGER,
  can_reserve boolean
);

create table if not exists reservations (
  id UUID PRIMARY KEY,
  table_id UUID REFERENCES tables(id),
  restaurant_id UUID REFERENCES restaurants(id),
  restaurant_name VARCHAR(256),
  size_of_table VARCHAR(256),
  table_name VARCHAR(256),
  user_id UUID,
  reservation_day DATE,
  from_time TIME,
  to_time TIME,
  forename VARCHAR(256),
  surname VARCHAR(256),
  phone_number VARCHAR(256)
);

create table if not exists meals (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  name VARCHAR(256),
  image VARCHAR(256),
  price NUMERIC,
  ingredients VARCHAR(256),
  time_to_do NUMERIC,
  category VARCHAR(256)
);

create table if not exists work_time (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  work_day VARCHAR(256),
  from_time TIME,
  to_time TIME
);