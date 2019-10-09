#!/usr/bin/env sh

while true; do
	./mvnw compile exec:exec
	sleep 1s
done

