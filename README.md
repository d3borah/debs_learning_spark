debs_learning_spark
===================
### notes and code while I learn Apache Spark, all error my own

## Spark Context Operations

### RDD Creation and RDD Transformation Operations. Do not get evaluated until passed to an Action. 
* .parallelize()  
  * *Parallelize a collection in your driver program. Must fit in memory on one machine.*
* .wholeTextFile()
  * *Whole file loaded at once. Often used with unstructured data, or records spanning multiple lines, which need to be parsed from the whole file.*
* .textFile()
  * *Loads only the necessary lines from file. Can be more efficient in case of node loss, etc.*

### RDD Usage Planning. Do not get evaluated until passed to an Action.
* .persist()
  * *Plan to reuse the RDD in multiple actions or for iterative algorithms. Set the StorageLevel Enum.* 
* .cache()  
  * *cache() is the default persist (StorageLevel.MEMORY_ONLY_SER)*

### RDD Transformations. Do not get evaluated until passed to an Action.
* .map(func)
  * *Applies a function that returns 1 element for each input element*   * 
* .flatMap(func)
  * *First applies a function that returns a sequence of 0 or more elements for each element, then flattens that.*
* .filter(func)
  * *Returns elements for which func returns True* 
* .mapPartitions(func)
  * *On a partition, given an iterator of element(s) in that partition's RDD, return an iterator of result elements. Use to avoid constructing expensive objects (eg partition specific counters, parsers, and writers) for each element, instead passing functions with these objects into .mapPartitions().*
 
### Actions
* .reduceByKey()
* .countByValue()
* .first()
* .collect()
* .take(num)
* .saveAsTextFile()

### Other
* .toDebugString()
  * *examine the DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  

###Links
* [More about flatmap in scala](http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten)
* [Spark Programming Guide](http://spark.apache.org/docs/latest/programming-guide.html)
