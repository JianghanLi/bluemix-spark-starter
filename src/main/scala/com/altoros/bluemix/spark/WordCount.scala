package com.altoros.bluemix.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.TreeMap
import scala.io.Source

/**
  * Created by siarhei.sidarau on 3/5/2016.
  */
object WordCount {
  val pathToObjectStorageFile: String = "swift://notebooks.spark/spark-overview.txt"
  val pathToLocalFile: String = "/spark-overview.txt"

  //main method invoked when running as a standalone Spark Application
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Bluemix Spark")

//    conf.setMaster("local[4]")

    val sc = new SparkContext(conf)

    val words: Map[String, Int] = getWordCount(sc.makeRDD(Source.fromInputStream(getClass
      .getResourceAsStream(pathToLocalFile)).getLines.toList))

    words.foreach(p => println(">>> word = " + p._1 + ", count = " + p._2))

    sc.stop()
  }

  //Library method that can be invoked from Jupyter Notebook
  def countOfWord(sc: SparkContext): Map[String, Int] = {
    val rdd = buildRDDFromFile(sc, pathToObjectStorageFile)

    getWordCount(rdd)
  }

  private def getWordCount(rdd: RDD[String]): Map[String, Int] = {
    val words = rdd.flatMap(x => x.split(" "))
    val wordCount: RDD[(String, Int)] = words.map(x => (trimPrefixSuffixChars(x, (c) => !c.isLetterOrDigit), 1))
      .reduceByKey((x, y) => x + y)

    TreeMap(wordCount.collect():_*)
  }

  private def buildRDDFromFile(sc: SparkContext, pathToFile: String): RDD[String] = {
    setConfig(sc, "spark")
    sc.textFile(pathToFile)
  }

  private def setConfig(sc: SparkContext, name: String): Unit = {
    val pfx = "fs.swift.service." + name

    val conf = sc.getConf
    conf.set(pfx + ".auth.url", "https://identity.open.softlayer.com")
    conf.set(pfx + ".tenant", "sf56-d54664602866ee-20565106c03e")
    conf.set(pfx + ".username", "Admin_58ad00f71fbcbebe819624b6d70df9ec6a494887")
    conf.set(pfx + ".auth.endpoint.prefix", "endpoints")
    conf.set(pfx + ".password", "cvU.GV&3yIMdUDl6")
    conf.set(pfx + ".apikey", "cvU.GV&3yIMdUDl6")
    conf.set(pfx + ".region", "dallas")
    conf.set(pfx + ".hostname", "notebooks")
  }

  private def trimPrefixSuffixChars(str: String, invalidCharsFunction: (Char) => Boolean = (c) => c == ' '): String =
    if (str.nonEmpty) {
      str
        .dropWhile(char => invalidCharsFunction(char)) //trim prefix
        .reverse
        .dropWhile(char => invalidCharsFunction(char)) //trim suffix
        .reverse
    }
    else {
      str
    }
}
