from operator import add
from pyspark import SparkConf, SparkContext
conf = SparkConf().setMaster("local").setAppName("My App")
sc = SparkContext(conf = conf)

#create BASE RDD (DAG)
inputfile = sc.textFile("README.md")

#like the scala example, need to use flatMap because the lines are lists of lists. 

keywords = inputfile.flatMap(lambda x: x.split(' ')) #x is each line of the input file

myMap = keywords.map(lambda x: (x,1))  #x is each keyword
myReduce = myMap.reduceByKey(add)
myReduce.saveAsTextFile("wc_out")

#you could also do:
# result = keywords.countByValue() 
#     for key, value in result.iteritems():
#        print "%s %i" % (key, value)
#or perhaps this, but need to test. 
# result.saveAsTextFile("wc_out")
