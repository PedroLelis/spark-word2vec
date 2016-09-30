package org.apache.spark.embedding

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io._

import org.apache.spark.{SparkConf, SparkContext}

object Word2VecSuite {
  def main(args: Array[String]) {
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

    // write word vectors to working dir
    val pw = new PrintWriter(new File("spark_vectors"))
    pw.append(model.numWords + " " + model.vectorSize).write("\n")
    model.getVectors.foreach { case (k, v) =>
      pw.append(k + " ")
      v.foreach { f =>
        pw.append(f + " ")
      }
      pw.write("\n")
    }
    pw.flush
    pw.close

    println("Total time = " + (endTime - startTime) / 1000.0 + "s")
  }
}
