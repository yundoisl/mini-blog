FROM postgres:11.7-alpine

COPY initialSetup.sql /docker-entrypoint-initdb.d/initialSetup.sql

ENV PGDATA=/data
