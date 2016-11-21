# Microbule

Microbule serves as a framework for developing [Microservices](http://www.martinfowler.com/articles/microservices.html).

## Getting Started

Microbule is an extension of the [Apache Karaf](http://karaf.apache.org) container.  Getting started with Microbule is
 simple:

1. Download [Apache Karaf](http://karaf.apache.org/download.html) (verson 4.0.x).
2. Start Apache Karaf:

 ```
 cd [KARAF_HOME]
 bin/karaf
 ```

3. Install the Microbule Karaf Feature:

 ```
 karaf@root()> repo-add mvn:org.microbule/microbule-features/<VERSION>/xml/features
 karaf@root()> feature:install microbule
 ```

4. Start The Examples

 ```
 karaf@root()> feature:install microbule-examples
 ```

5. Profit!

## What's in a Name?

A "microbule" is a unit of length used in Marvel's
[Guardians of the Galaxy](http://marvel.com/characters/70/guardians_of_the_galaxy) movie.  Microbule strives to be the
microservices framework that the others will try to measure up to!