from operator import add

#create BASE RDD (DAG)
inputfile = sc.textFile("README.md")

#like the scala example, need to use flatMap because the lines are lists of lists. 
keywords = inputfile.flatMap(lambda x: x.split(' ')) #x is each line of the input file

myMap = keywords.map(lambda x: (x,1))  #x is each keyword
myReduce = myMap.reduceByKey(add)

wc.saveAsTextFile("wc_out")
