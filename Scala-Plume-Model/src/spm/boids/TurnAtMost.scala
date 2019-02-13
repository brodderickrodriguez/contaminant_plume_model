
package spm.boids

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import org.nlogo.agent.AgentSetBuilder
import spm.helper.{Helper, MathHelper}



class TurnAtMost extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List(NumberType, NumberType))
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val requestedTurn = Math.abs(Helper.getInput(args, 0).asInstanceOf[Double])
        val maxTurnAllowed = Helper.getInput(args, 1).asInstanceOf[Double]
        //val UAV = Helper.ContextHelper.getAgent(context)
        //val currentHeading = Helper.TurtleHelper.getTurtleVariable()
        //var thisTurn = 0
        
        if (requestedTurn > maxTurnAllowed) {
        
        } else {
        
        }
        
        
    } // perform()
    
} // TurnAtMost




