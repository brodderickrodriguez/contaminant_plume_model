package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import org.nlogo.agent.AgentSetBuilder
import spm.helper.{Helper, MathHelper}


object TurnTowards {
    def go(): Unit = {
    
    }
}


class TurnTowardsReporter extends Reporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    override def report(args: Array[Argument], context: Context): AnyRef = {
        0.toLogoObject
    }
}
