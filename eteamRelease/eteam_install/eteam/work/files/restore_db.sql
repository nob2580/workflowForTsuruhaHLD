SET client_encoding TO 'SJIS';

drop database eteam;
create database eteam owner=eteam encoding='UTF8';
\c eteam;
drop schema public;
create schema public;
alter schema public owner to eteam;
create extension if not exists pgcrypto with schema public;
