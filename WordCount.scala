import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
val conf = new SparkConf().setMaster("local").setAppName("My App")
val sc = new SparkContext("local", "My App")


//BASE RDD (HADOOP RDD) from text file. Each element of the RDD is a line.

val inputfile = sc.textFile("README.md")  

/*
Each partition generates a list of keywords, and you end up with a list of lists. 
What we really want to map is a list of keywords. flatMap works by applying a map function 
that returns a sequence for each element in the list, and then flattening the results.
Potentially nteresting resource with other flatmap examples (not Spark specific):
http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten
*/

val keywords = inputfile.flatMap(l => l.split(" "))
val myMap = keywords.map(word => (word,1))
val myReduce = myMap.reduceByKey(_ + _)

/*
With saveAsTextFile() you get some partition files back. If you need one file, would need post-processing. 
or consider Apache Avro.
*/

myReduce.saveAsTextFile("wc_out")
