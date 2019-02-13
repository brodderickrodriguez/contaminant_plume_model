package spm.search_algorithms.symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper


class OptimalSubregionDimensions extends Reporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val population = Helper.ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double].toInt
        var optimal = (0, 0, Int.MaxValue)
        
        for (y <- Range(1, (population / 2) + 1) if population % y == 0) {
            val x = population / y
            val cost = Math.abs(x - y)
            if (cost < optimal._3) optimal = (x, y, cost)
        }
        
        optimal.productIterator.toArray.toLogoList
    } // perform()
} // OptimalSubregionDimensions
