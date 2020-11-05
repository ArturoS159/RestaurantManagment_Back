create schema if not exists restaurant;

create table if not exists restaurants (
  id UUID PRIMARY KEY,
  name VARCHAR(256),
  image VARCHAR(256),
  owner_id UUID,
  city VARCHAR(256),
  street VARCHAR(256),
  house_number VARCHAR(256),
  phone_number VARCHAR(256)
);

create table if not exists meals (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  name VARCHAR(256),
  image VARCHAR(256),
  price NUMERIC,
  ingredients VARCHAR(256),
  time_to_do NUMERIC
);

create table if not exists restaurant_category (
  restaurant_id UUID PRIMARY KEY REFERENCES restaurants(id),
  category VARCHAR(256)
);

create table if not exists work_time (
  id UUID PRIMARY KEY,
  restaurant_id UUID REFERENCES restaurants(id),
  work_day VARCHAR(256),
  from_time TIME,
  to_time TIME
);