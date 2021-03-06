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
* [Zhen He's seriously awesome Spark RDD API Examples](http://homepage.cs.latrobe.edu.au/zhe/ZhenHeSparkRDDAPIExamples.html)
* [splittable compression with lzo](http://blog.cloudera.com/blog/2009/11/hadoop-at-twitter-part-1-splittable-lzo-compression/)
* [Databricks Spark knowledge base](http://databricks.gitbooks.io/databricks-spark-knowledge-base/content/)
* [Spark user list](http://apache-spark-user-list.1001560.n3.nabble.com/)
* [Video - A Deeper Understanding of Spark Internals (Aaron Davidson)](https://www.youtube.com/watch?v=dmL0N3qfSc8)
* [Spark 1.1 sort-based shuffle](https://databricks.com/blog/2014/10/10/spark-petabyte-sort.html)

# Spark Context Operations
##Transformations

### RDD Creation (transformations)
* .parallelize()  
  * *Parallelize a collection in your driver program. Must fit in memory on one machine.*
    * .parallelizePairs()
      * *Parallelize a tuple collection into a PairRDD (only needed in Java)*
* .wholeTextFile()
  * *Whole file loaded at once. Often used with unstructured data, or records spanning multiple lines, which need to be parsed from the whole file. returns tuple of (fileName, content)*
* .textFile()
  * *Loads only the necessary lines from file. Can be more efficient than .wholeTextFile() in case of node loss, etc.*

### RDD Usage Planning and Concurrency (transformations)
* .persist()
  * *Plan to reuse the RDD in multiple actions or for iterative algorithms. Set the StorageLevel Enum.* 
* .cache()  
  * *cache() is the default persist (StorageLevel.MEMORY_ONLY)*
* .repartition()
  * *dude, repartition can be expensive but could be a good idea for good concurrency. consider instead combining a repartition with an operation which needs a shuffle anyways (takes numPartitions, eg distinct(numPartitions = 6))
  
### common RDD Transformations. 
* .map(func)
  * *Applies a function that returns 1 element for each input element. Can return elements of different type*
* .flatMap(func)
  * *First applies a function that returns a sequence of 0 or more elements for each element, then flattens that.*
```
val x = sc.parallelize(1 to 5,3)
x.flatMap(List.fill(2)(_)).collect
Array[Int] = Array[1,1,2,2,3,3,4,4,5,5)
```
* .filter(func)
  * *Returns elements for which func returns True* 

### RDD Transformations which utilize partitions explicitly 
* .mapPartitions(func)
  * *On a partition, given an iterator of element(s) in that partition's RDD, return an iterator of result elements, which will be combined to form new RDD. Use to avoid constructing expensive or nonserializable objects for each element (eg partition specific counters, parsers, writers, random number generators). instead pass functions with these objects into .mapPartitions().*
```
val x = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9,10), 3)
def myfunc(iter: Iterator[Int]) : Iterator[Int] = {
  var result = List[Int]()
  while (iter.hasNext) {
    val current = iter.next;
    result = result ::: List.fill(scala.util.Random.nextInt(10))(current)
  }
  result.iterator
}
x.mapPartitions(myfunc).collect
```
  * *note the following will fail because it has a second serialization problem:  because of "_" result is partially applied function and doesn't infer correct RDD signature.*
```
val newRDD = oldRDD.mapPartitions { iter =>
  val rand = new Scala.util.random
  iter.map(x => (x, Array.fill(10)(rand.nextDouble _)))
}
````  
* .mapPartitionsWithIndex()
  * *like mapPartitions, but exposes a partition index for partition specificity. example shows partition specific RNG set with partition index and seed, which is possibly good practice for reproducibility in case of failure*
```
val myAppSeed = 9311
val newRDD = myRDD.mapPartitionsWithIndex { (indx, iter) =>
  val rand = new scala.util.Random(indx+myAppSeed)
  iter.map(x => (x, Array.fill(10)(rand.nextDouble)))
}
```
* .filterWith(func, func)
  *the first function transforms the partition index to a type. the second function takes the rdd elements and the transformed partition index*
```
myrdd.filterWith(i => i)((x,i) => x % 2 == 0 || i % 2 == 0).collect()
```
* .foreachWith()
  * *like .filterWith() but for parameterless functions besides filter*
```
myrdd.foreachWith(i => i)((x,i) => if (x % 2 == 1 && i % 2 == 0) println(x) )
```  
* .foreachPartition()
  * *like .foreachWith() for a parameterless function, over an iterator*
```
myrdd.foreachPartition(x => println(x.reduce(_ + _)))
```
```
myrdd.foreachPartition(x => { NotSerializable notSerializable = new NotSerializable(); notSerializable.doSomething(x);  } )
```


###Pair RDD transformations

* .mapValues(func)
  * *An easier way to operate on the values of a PairRDD, analogous to map{case (x, y) (x, func(y)}*
* .sortByKey()

####reductions
Reductions (and sortby) are done with hashmaps on each partition. In Spark 1.1, all these ops will be able to spill to disk. If individual K,V pair is large, could still create memory pressure and GC issues. 
However, wrt shuffle, individual K,V pair must fit in memory (is this still true with the new sort-based shuffle??)
* .groupByKey()
  * *Group the values for each key in the RDD into a single sequence. In groupByKey(), all the key-value pairs are shuffled around.  Instead use reduceByKey, foldByKey, combineByKey*
* .reduceByKey()
  * *in contrast to the action reduce(), implemented as a transformation on PairRDDs, because there may be a large number of keys. function is of form (V,V) => V. Output with common key on each partition is combined before shuffling, making this more efficient than .groupByKey for large data*

 ```
.mapValues() and .reduceByKey() both take a parameter to set number of tasks.
There are examples of these functions being used together to compute per key averages. 
```
* .foldByKey()
  * *merge key values with associative function and a neutral value* 
* .combineByKey()
  * *use when return type differs from input value type* 

###Two RDD tranformations
* .zip()
  * positional zip of 2 rdd's together. returns tuples, 2-tuple can be interpreted as pairRDDs. 
```
#zip 3 collections into a 3-tuple
a.zip(b).zip(c).map((x) => (x._1._1, x._1._2, x._2 )).collect
```

## Actions

* .reduce()
* .collect()
  * *send RDD too driver as Array. in many cases the RDD is too big to collect(), and is instead written out, or use .take() instead. Can also apply a map first if provided a map function*
* .take(num)
* .first()
* .count()
* .countByValue()
  * *Returns a map that contains all unique values of the RDD and their respective occurrence counts. This operation will finally aggregate the information on a single reducer. Avoid if data does not fit in memory*

## Actions for pair RDDs
* .countByKey(pair)
  * *like .count(), but for [K,V] - counts the values for each distinct key separately. Avoid if data does not fit in memory*
* .collectAsMap(pair)
  * *like .collect(), but works on key-value RDD and returns a map. Avoid if data does not fit in memory*

### Actions to Sample
.takeSample()
  * *def takeSample(withReplacement: Boolean, num: Int, seed: Int): Array[T]*
  * *internally randomizes, which is why it has seed parameter*
  * *returns an Array not an RDD (unless you pass it in a parallelize function)*

## Actions to save 
* .saveAsTextFile()
* .saveAsSequenceFile()

## input and output formats
* .hadoopFile()
  * *takes 3 parameters {inputFormat, key class, value class} *. example inputFormats: KeyValueTextInputFormat. tab separated K,V.   
```
val input = sc.hadoopFile[Text,Text,KeyValueTextInputFormat](inputFile).map{case(x,y) => (x.toString, y.toString)
```
## Other
* .toDebugString()
  * *examine the DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  

