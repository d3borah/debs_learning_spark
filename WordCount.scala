//base RDD from text file - each element of the RDD is a line

val inputfile = sc.textFile("README.md")  

/*
each partition generates a list of keywords, so its a list of lists. what we really want is a 
list of keywords. flatMap works applying a map function that returns a sequence for each element 
in the list, and then flattening the results interesting resource with other flatmap examples 
(not Spark specific):
http://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten
*/

val keywords = inputfile.flatMap(l => l.split(" "))
val myMap = keywords.map(word => (word,1))
val myReduce = myMap.reduceByKey(_ + _)

/*
with saveAsTextFile you will get some partition files back. would need post-processing to force 
it into one file. or consider Apache Avro.
*/

myReduce.saveAsTextFile("wc_out")
