HorseBettingSystem
==================

A system that manages horse bets, developed for a software engineering course.

2 iterations were developed within a team, from analysis to implementation,
following the Unified Process (UP).

## Activities
- writing use cases
- UML modelling
- applying GOF and GRASP design patterns
- writing Java code.

## IDEs:
- Visual Paradigm for UML
- NetBeans

## Doc
Here is the documentation produced (in Italian):
http://bootstraphped.altervista.org/HorseBettingSystem/

## Usage
First of all you need to setup Derby.
Please follow [the Getting Started guide](https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/getstart/index.html).

Once derby is installed, you can run

        ./run.sh

which will:

- start derby
- create the DB and populate it with some sample data, by executing

        mvn clean compile exec:java -Dexec.mainClass=me.alb_i986.hbs.db.DbSetupMain
- finally, run the system, by executing

        mvn exec:java
