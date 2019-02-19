package spm.uav_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType
import spm.helper.Helper
import spm.search_algorithms.symmetric_search._UavUpdateSymmetricSearchIndividual

import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper
import spm.search_algorithms.random_search._UavRandomSearchBehavior
import spm.uav_behavior.{CheckBoundsUav, ComputeHeading}

import scala.collection.mutable.ListBuffer


class TurnUav extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType | OptionalType), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
    } // perform()
} // UavUpdateSymmetricSearch
