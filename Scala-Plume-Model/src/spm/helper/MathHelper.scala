package spm.helper

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import scala.util.Random


object MathHelper {
    def euclideanDistance(a: (Double, Double), b: (Double, Double)): Double = {
        val c = b._1 - a._1
        val d = b._2 - a._2
        Math.sqrt(Math.pow(c, 2) + Math.pow(d, 2))
    } // euclideanDistance()
    
    def isPrime(n: Int): Boolean = {
        for (i <- 2 until n / 2) if (n % i == 0) return false
        true
    } // isPrime()
    
    def random1(): Int = if (Random.nextInt(2) == 0) -1 else 1
} // MathHelper
