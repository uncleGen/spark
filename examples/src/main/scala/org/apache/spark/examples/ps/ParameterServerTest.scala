package org.apache.spark.examples.ps

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.ps.PSContext

/**
 * Created by genmao.ygm on 15-3-19.
 */
object ParameterServerTest {

  def main(args: Array[String]) {
    println("before conf")
    val conf = new SparkConf().setAppName("ParameterServerTest")
    println("after conf")
    println("before sc")
    val sc = new SparkContext(conf)
    println("after sc")
    val pssc = new PSContext[(String, String)](sc)

    val rdd = sc.parallelize(Array(1, 2, 3))
    println(rdd.count())
  }

}
