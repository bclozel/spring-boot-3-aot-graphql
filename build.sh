#!/usr/bin/env bash
./mvnw -Pnative -DskipTests clean native:compile &&  ./target/graphql
