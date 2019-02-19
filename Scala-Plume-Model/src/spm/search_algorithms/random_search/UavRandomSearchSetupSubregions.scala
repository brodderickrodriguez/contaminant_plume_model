package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType
import org.nlogo.api.ScalaConversions._
import spm.helper.Helper
import spm.search_algorithms.symmetric_search._UAVSubregionGenerator


class UavRandomSearchSetupSubregions extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
    } // perform()
}
