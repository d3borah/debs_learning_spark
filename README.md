debs_learning_spark
===================
### notes and code while I learn Apache Spark, all error my own

###Links which are halping me. 

* [Spark Programming Guide](http://spark.apache.org/docs/latest/programming-guide.html)
* [Spark Core Doc](https://spark.apache.org/docs/0.9.1/api/core/index.html#org.apache.spark.package)
* [Spark Pair RDD Scala DOC](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.PairRDDFunctions)
* [Spark RDD Scala DOC](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.rdd.RDD)
* [More about flatmap in scala](http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten)
* [Pattern Matching in Scala](http://www.scala-lang.org/old/node/120)
* [Maven Tutorial](https://www.youtube.com/watch?v=al7bRZzz4oU)
* [Databricks Intro to Spark devops on DSE 4.5](https://docs.google.com/document/d/1TjOd3HjrhbbPFOawE3uH2IPyTPZDNh-Ma0TZ8l4eSNM/preview?sle=true)
* [Zhen He's Spark RDD API Examples](http://homepage.cs.latrobe.edu.au/zhe/ZhenHeSparkRDDAPIExamples.html)
* [splittable compression with lzo](http://blog.cloudera.com/blog/2009/11/hadoop-at-twitter-part-1-splittable-lzo-compression/)

## Spark Context Operations

### RDD Creation and RDD Transformation Operations. Not evaluated until Action. 
* .parallelize()  
  * *Parallelize a collection in your driver program. Must fit in memory on one machine.*
    * .parallelizePairs()
      * *Parallelize a tuple collection into a PairRDD (only needed in Java)*
* .wholeTextFile()
  * *Whole file loaded at once. Often used with unstructured data, or records spanning multiple lines, which need to be parsed from the whole file. returns tuple of (fileName, content)*
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
```
val x = sc.parallelize(1 to 5,3)
x.flatMap(List.fill(2)(_)).collect
Array[Int] = Array[1,1,2,2,3,3,4,4,5,5)
```
* .filter(func)
  * *Returns elements for which func returns True* 
* .mapPartitions(func)
  * *On a partition, given an iterator of element(s) in that partition's RDD, return an iterator of result elements. Use to avoid constructing expensive objects (eg partition specific counters, parsers, and writers) for each element, instead passing functions with these objects into .mapPartitions().*
* .filterWith(func, func)
  * the first function transforms the partition index to a type (simple example:  (i => i), the second function takes the transformed partition index and the RDD elements (simple example: ((a, i) =>  i == 0)  which filters for the elements in partition 0. )

### RDD Transformations Specific to particular forms of RDDs. Not evaluated until Action. 

###Pair RDDs
* .mapValues(func)
  * *An easier way to operate on the values of a PairRDD, analogous to map{case (x, y) (x, func(y)}*
* .reduceByKey()
  * *in contrast to the action reduce(), implemented as a transformation on PairRDDs, because there may be a large number of keys. function is of form (V,V) => V. Output with common key on each partition is combined before shuffling, making this more efficient than .groupByKey for large data*
 ```
.mapValues() and .reduceByKey() both take a parameter to set number of tasks.
There are examples of these functions being used together to compute per key averages. 
```
* .groupByKey()
  * *Group the values for each key in the RDD into a single sequence. In groupByKey(), all the key-value pairs are shuffled around.  Contrast to reduceByKey().*
* .foldByKey()

###Two RDDs
* .zip()
  * positional zip of 2 rdd's together. returns tuples, 2-tuple can be interpreted as pairRDDs. 
```
#zip 3 collections into a 3-tuple
a.zip(b).zip(c).map((x) => (x._1._1, x._1._2, x._2 )).collect
```

### Actions
* .reduce()
* .countByValue()
* .first()
* .collect()
  * *send RDD too driver. in many cases the RDD is too big to collect(), and is instead written out*
* .take(num)
* .saveAsTextFile()
* .saveAsSequenceFile()

### input and output formats
* .hadoopFile()
  * *takes 3 parameters {inputFormat, key class, value class} *. example inputFormats: KeyValueTextInputFormat. tab separated K,V.   
```
val input = sc.hadoopFile[Text,Text,KeyValueTextInputFormat](inputFile).map{case(x,y) => (x.toString, y.toString)
```
### Other
* .toDebugString()
  * *examine the DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  

