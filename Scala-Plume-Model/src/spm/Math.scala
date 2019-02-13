
package spm

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.NumberType

trait Math


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
} // MathHelper


class IsPrime extends Reporter with Math {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = NumberType)
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val n = Helper.getInput(args, 0).getIntValue
        for (i <- 2 until n / 2) if (n % i == 0) return false.toLogoObject
        true.toLogoObject
    } // report()
} // IsPrime


class Pythagorean extends Reporter with Math {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = NumberType)
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val a = Helper.getInput(args, 0).getDoubleValue
        val b = Helper.getInput(args, 1).getDoubleValue
        Math.sqrt(a * a + b * b).toLogoObject
    } // report()
} // IsPrime







