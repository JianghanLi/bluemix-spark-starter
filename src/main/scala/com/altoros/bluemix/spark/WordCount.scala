package com.altoros.bluemix.spark

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
  * Created by siarhei.sidarau on 3/5/2016.
  */
object WordCount {
  val PathToFile = "swift://notebooks.spark/spark-overview.txt"

  //main method invoked when running as a standalone Spark Application
  def main(args: Array[String]) {

    val sc = new SparkContext("local[4]", "LocalTest")

    val words: Map[String, Int] = countOfWord(sc)

    words.foreach(p => println(">>> word = " + p._1 + ", count = " + p._2))
  }

  //Library method that can be invoked from Jupyter Notebook
  def countOfWord(sc: SparkContext): Map[String, Int] = {

    val rdd = buildRDDFromFile(sc, PathToFile)

    val words = rdd.flatMap(x => x.split(" "))
    val wordCount: RDD[(String, Int)] = words.map(x => (x, 1)).reduceByKey((x, y) => x + y)

    wordCount.collect().toMap
  }

  def buildRDDFromFile(sc: SparkContext, pathToFile: String): RDD[String] = {
    setConfig(sc, "spark")
    sc.textFile(pathToFile)
  }

  def setConfig(sc:SparkContext, name:String) : Unit = {
    val pfx = "fs.swift.service." + name

    val conf = sc.getConf
    conf.set(pfx + ".auth.url", "https://identity.open.softlayer.com")
    conf.set(pfx + ".tenant", "sf83-0c85d486bd8a4d-19824259ab62")
    conf.set(pfx + ".username", "Admin_80afce25e705871b4b1e0e75a66ef5d8801abb73")
    conf.set(pfx + ".auth.endpoint.prefix", "endpoints")
    conf.set(pfx + ".password", "y(7O8Vr?([(5f..j")
    conf.set(pfx + ".apikey", "y(7O8Vr?([(5f..j")
    conf.set(pfx + ".region", "dallas")
    conf.set(pfx + ".hostname", "notebooks")
  }
}
