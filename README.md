# redis-leader-by-lock
Simple implementation of Cluster Leadership Election using redis lock

Using Spring-Boot and Redis only

## Motivation
Almost all examples of Leadership Election using Spring Boot go to Spring Cloud Cluster in Hazelcast (now deprecated) and Zookeeper(overpower in simple cases), and that's kinda strange, since [Redis can use Set Lock](http://redis.io/topics/distlock) to create an almost trivial Leadership Election. That's why I did it.

## Requirements
Java 8

Maven

Redis



