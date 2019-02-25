package spm.boids.turn_at_most

// Brodderick Rodriguez
// Auburn University - CSSE
// 19 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{NumberType, commandSyntax}
import spm.helper.{ContextHelper, InputHelper}

class TurnAtMostCommand extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List(NumberType, NumberType))
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        val requestedTurn = InputHelper.getInput(args, 0).getDoubleValue
        val maxTurnAllowed = InputHelper.getInput(args, 1).getDoubleValue
        
        TurnAtMost.go(uav, requestedTurn, maxTurnAllowed)
    } // perform()
} // TurnAtMostReporter
