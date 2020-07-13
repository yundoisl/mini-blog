#!/bin/bash

sbt stage
docker-compose up --build
