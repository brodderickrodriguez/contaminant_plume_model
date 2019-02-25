package spm.uav_behavior.compute_heading

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Context}
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._

import spm.helper.{InputHelper, ContextHelper}


class GetHeadingTowardsPointReporter extends Reporter {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = NumberType)
    
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val argx = InputHelper.getInput(args, 0).getDoubleValue
        val argy = InputHelper.getInput(args, 1).getDoubleValue
        val uav = ContextHelper.getTurtle(context)
        ComputeHeading.get(uav, argx, argy).toLogoObject
    } // report()
    
} // getHeadingTowardsPointReporter
