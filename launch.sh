#!/usr/bin/env sh

while true; do
	./mvnw clean compile exec:exec
	sleep 1s
done

