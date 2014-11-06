
##Building Apps

1) dependencies
  * Scala and Java: 
    * Maven dependency.

```
groupId = org.apache.spark
artifactId = spark-core_2.10
version = 1.1.0
```
    * Build with Maven or sbt. 

  * Python:
    * spark-submit script. 

2) In app, set a "conf" value, and pass it when initializing a spark context. 
  * When you initialize the Spark context and pass "local", Spark is running on one local thread only. 
  * shut down sc with stop(sc) or system exit. 
