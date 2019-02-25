package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 19 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import spm.helper.{InputHelper, ContextHelper}


object TurnAtMost {
    def go(uav: org.nlogo.agent.Turtle, requestedTurn: Double, maxTurnAllowed: Double): Unit = {
        if (math.abs(requestedTurn) > maxTurnAllowed)
            if (requestedTurn > 0)
                uav.turnRight(maxTurnAllowed)
            else
                uav.turnRight(-maxTurnAllowed)
        else
            uav.turnRight(requestedTurn)
    } // go()
} // TurnAtMost


class TurnAtMostCommand extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List(NumberType, NumberType))
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        val requestedTurn = InputHelper.getInput(args, 0).getDoubleValue
        val maxTurnAllowed = InputHelper.getInput(args, 1).getDoubleValue
        
        TurnAtMost.go(uav, requestedTurn, maxTurnAllowed)
    } // perform()
} // TurnAtMostReporter
