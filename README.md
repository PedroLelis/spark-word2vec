# spark-word2vec
spark-word2vec creates vector representation of words in a text corpus. It is based on the implementation of word2vec in Spark MLlib. Several optimization techniques are used to make this algorithm more scalable and accurate.

# Features
  + Two models CBOW and Skip-gram are used in our implementation.
  + Both hierarchical softmax and negative sampling methods are supported to train the model.
  + The sub-sampling trick can be used to achieve both faster training and significantly better representations of uncommon words.

# Examples
## Scala API
```scala
val conf = new SparkConf()
      .setAppName("Word2Vec example")
      .setMaster("local[4]")
    val sc = new SparkContext(conf)
    val vectorsSize = 100

    // Input data: Each row is a bag of words from a sentence or document.
    val documentDF = sc.textFile("/home/linchen/text8").map(_.split(" ").toSeq)

    val startTime = System.currentTimeMillis()
    // Learn a mapping from words to Vectors.
    val model = new Word2Vec()
      .setVectorSize(vectorsSize)
      .setWindowSize(5)
      .setNumIterations(4)
      .setMinCount(0)
      .setCBOW(0)
      .setHS(0)
      .setNegative(5)
      .setNumPartitions(1)
      .setSample(1e-3)
      .fit(documentDF)
    val endTime = System.currentTimeMillis()

    println("Total time = " + (endTime - startTime) / 1000.0 + "s")
```

# Requirements
spark-word2vec is built against Spark 1.6.2.

# Build From Source
```scala
sbt package
```

# Licenses
spark-word2vec is available under Apache Licenses 2.0.

# Contact & Feedback
If you encounter bugs, feel free to submit an issue or pull request. Also you can mail to:
+ Chen Lin (m2linchen@gmail.com).