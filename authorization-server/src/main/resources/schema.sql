create schema if not exists auth;

create table if not exists oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
);

create table if not exists oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
);

create table if not exists users (
  id UUID PRIMARY KEY,
  email VARCHAR(256),
  login VARCHAR(256),
  password VARCHAR(256),
  forename VARCHAR(256),
  surname VARCHAR(256),
  street VARCHAR(256),
  city VARCHAR(256),
  post_code VARCHAR(256),
  phone_number VARCHAR(256),
  house_number VARCHAR(256),
  identity_number VARCHAR(256),
  nip VARCHAR(256),
  regon VARCHAR(256),
  base_role VARCHAR(256)
);

create table if not exists user_roles (
  id UUID PRIMARY KEY,
  user_id UUID,
  role VARCHAR(256),
  restaurant_id UUID
);