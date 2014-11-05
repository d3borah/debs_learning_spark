debs_learning_spark
===================
### notes and code while I learn Apache Spark

### RDD creation and transformations

* .wholeTextFile()
  * *Upon action or node loss, whole file loaded at once.*
* .textFile()
  * *Upon action or node loss, loads only the necessary lines from file.*
* .parallelize()  
  * *Upon action or node loss, parallelize a collection in your driver program. Must fit in memory on one machine.*


### Actions

* .persist()
    * *set the StorageLevel Enum* 
* .cache()  
  * *cache() is the default persist (StorageLevel.MEMORY_ONLY_SER)*
* .flatMap()
  * *Applies a map function that returns a sequence for each element in a list, flattens the results.*
* .toDebugString()
  * *examine your DAG, which is like a query plan. does not contain info on optimizer decisions or what is in cache.*  
* .reduceByKey()
* .countByValue()
* .saveAsTextFile()

###Links

[More about flatmap in scala](http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten)
