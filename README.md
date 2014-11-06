debs_learning_spark
===================
### notes and code while I learn Apache Spark, all error my own

###Links which are halping me. 
* [More about flatmap in scala](http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten)
* [Spark Programming Guide](http://spark.apache.org/docs/latest/programming-guide.html)
* [Pattern Matching in Scala](http://www.scala-lang.org/old/node/120)
* [Maven Tutorial](https://www.youtube.com/watch?v=al7bRZzz4oU)


## Spark Context Operations

### RDD Creation and RDD Transformation Operations. Not evaluated until Action. 
* .parallelize()  
  * *Parallelize a collection in your driver program. Must fit in memory on one machine.*
    * .parallelizePairs()
      * *Parallelize a tuple collection into a PairRDD (only needed in Java)*
* .wholeTextFile()
  * *Whole file loaded at once. Often used with unstructured data, or records spanning multiple lines, which need to be parsed from the whole file.*
* .textFile()
  * *Loads only the necessary lines from file. Can be more efficient in case of node loss, etc.*

### RDD Usage Planning. Not evaluated until Action. 
* .persist()
  * *Plan to reuse the RDD in multiple actions or for iterative algorithms. Set the StorageLevel Enum.* 
* .cache()  
  * *cache() is the default persist (StorageLevel.MEMORY_ONLY)*

### RDD Transformations. Not evaluated until Action. 
* .map(func)
  * *Applies a function that returns 1 element for each input element*
* .flatMap(func)
  * *First applies a function that returns a sequence of 0 or more elements for each element, then flattens that.*
* .filter(func)
  * *Returns elements for which func returns True* 
* .mapPartitions(func)
  * *On a partition, given an iterator of element(s) in that partition's RDD, return an iterator of result elements. Use to avoid constructing expensive objects (eg partition specific counters, parsers, and writers) for each element, instead passing functions with these objects into .mapPartitions().*

### RDD Transformations Specific to particular forms of RDDs. Not evaluated until Action. 
* .mapValues(func)
  * *An easier way to operate on the values of a PairRDD, analogous to map{case (x, y) (x, func(y)}*
* .reduceByKey()
  * *in contrast to the action reduce(), implemented as a transformation on PairRDDs, as there may be a large number of keys. function is of form (V,V) => V*
 ```
.mapValues() and .reduceByKey() both take a parameter to set number of tasks.
There are examples of these functions being used together to compute per key averages. 
```
* .foldByKey()
 
### Actions
* .reduce()
* .countByValue()
* .first()
* .collect()
  * *send RDD too driver. in many cases the RDD is too big to collect(), and is instead written out*
* .take(num)
* .saveAsTextFile()
* .saveAsSequenceFile()

### Other
* .toDebugString()
  * *examine the DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  

