package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._

import spm.helper.Helper


object TurnTowards {
    def go(uav: org.nlogo.agent.Turtle, newHeading: Double, maxTurn: Double): Unit = {
        val currentHeading = Helper.TurtleHelper.getTurtleVariable(uav, "heading").asInstanceOf[Double]
        val computedHeading = org.nlogo.agent.Turtle.subtractHeadings(currentHeading, newHeading)
        
        TurnAtMost.go(uav, computedHeading, maxTurn)
    } // go()
} // TurnTowards


class TurnTowardsReporter extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = Helper.ContextHelper.getTurtle(context)
        val newHeading = Helper.getInput(args, 0).getDoubleValue
        val maxTurn = Helper.getInput(args, 1).getDoubleValue
    
        TurnTowards.go(uav, newHeading, maxTurn)
    } // perform()

} // TurnTowardsReporter
