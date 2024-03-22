SET client_encoding TO 'SJIS';

create schema if not exists public;
alter schema public owner to eteam;
create extension if not exists pgcrypto with schema public;
