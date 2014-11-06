
##Building Apps

###dependencies

* Scala and Java: 
    * Maven dependency. Build with Maven or sbt.
```
groupId = org.apache.spark
artifactId = spark-core_2.10
version = 1.1.0
```

* Python:
    * spark-submit script. 

### Spark Context
* In app, set a "conf" value, and pass it when initializing a spark context. 
* When you initialize the Spark context and pass "local", Spark is running on one local thread only. 
* shut down sc with stop(sc) or system exit. 

### Passing functions
* functions in the driver prog are serialized and shipped out to the cluster. 

* Python, can pass lambda functions, top level functions, or locally defined functions. 
  * Be careful when passing functions that are member of objects or contain references to fields within the object. Don't pass these in your function (eg. self.method) because otherwise the whole object will attempt to be serialized and shipped... instead extract to local ref within your function. 

Do this:
```
def getMatchesNoReference(self, rdd)
    query = self.query
    return rdd.filter(lambda x: query in x)
```
* Scala, can pass functions defined inline or references to methods or static functions. 
  * the function and data referenced needs to implement Java's Serializable interface. 
  * same issue with whole objects being serialized, but not as obvious with no "self" keyword. 

Do this:
```
def getMatchesNoReference(rdd: RDD[String]): RDD[String] = {
  val query_ = this.query
  rdd.map(x => x.split(query_))
  ```

