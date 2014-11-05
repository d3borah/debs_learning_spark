debs_learning_spark
===================
### notes and code while I learn Apache Spark, all error my own

### RDD creation and transformations

* .wholeTextFile()
  * *Upon action, whole file loaded at once.*
* .textFile()
  * *Upon action, loads only the necessary lines from file.*
* .parallelize()  
  * *Upon action, parallelize a collection in your driver program. Must fit in memory on one machine.*
* .persist()
    * *plan to reuse the RDD in multiple actions. set the StorageLevel Enum* 
* .cache()  
  * *cache() is the default persist (StorageLevel.MEMORY_ONLY_SER)*
* .flatMap()
  * *Applies a map function that returns a sequence for each element in a list, flattens the results.*
* .map()
* .filter()
* .mapPartitions()
  * *On a partition, given an iterator of element(s) in that partition's RDD, return an iterator of result elements. Use cases include avoiding constructing expensive objects (eg partition specific counters, parsers, and writers) for each element, instead passing functions with these objects into .mapPartitions().*
 

### Actions
* .reduceByKey()
* .countByValue()
* .saveAsTextFile()
* .first()
* .collect()

### Other
* .toDebugString()
  * *examine the DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  

###Links
* [More about flatmap in scala](http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten)
* [Spark Programming Guide](http://spark.apache.org/docs/latest/programming-guide.html)
