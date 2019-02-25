package spm.uav_behavior.check_uav_bounds.check_uav_inside_world_bounds

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.{Argument, Context, Reporter}
import org.nlogo.core.Syntax.BooleanType
import org.nlogo.api.ScalaConversions._
import org.nlogo.core.Syntax

import spm.helper.ContextHelper


class CheckUavInsideWorldBoundsReporter extends Reporter {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = BooleanType)
    
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val uav = ContextHelper.getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
        CheckUavInsideWorldBounds.perform(context, uav).toLogoObject
    } // perform()
    
} // CheckUavInsideWorldBounds()
