package spm.uav_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import scala.util.Random

import org.nlogo.core.Syntax.ListType
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._

import spm.helper.Helper
import spm.boids._


object MoveUavBackInsideWorldBounds {
    def go(uav: org.nlogo.agent.Turtle, context: Context): Unit = {
        val ptx = (context.world.worldWidth / 4) + Random.nextInt(context.world.worldWidth / 2)
        val pty = (context.world.worldHeight / 4) + Random.nextInt(context.world.worldHeight / 2)
    
        val newDesiredHeading = ComputeHeading.get(uav, ptx, pty) - 180
        val worldThreshold = Helper.ContextHelper.getObserverVariable(context, "max-world-edge-turn").asInstanceOf[Double]
    
        Helper.BreedHelper.setBreedVariable(uav, "desired-heading", newDesiredHeading.toLogoObject)
        TurnTowards.go(uav, newDesiredHeading, worldThreshold)
    }
}

object TurnUav {
    def go(uav: org.nlogo.agent.Turtle, context: Context, turnAllowed: Double): Unit = {
        val desiredHeading = Helper.BreedHelper.getBreedVariable(uav, "desired-heading").asInstanceOf[Double]
        
        if (CheckBoundsUav.uavInsideWorld(context, uav)) {
            TurnTowards.go(uav, desiredHeading, turnAllowed)
        } else {
            MoveUavBackInsideWorldBounds.go(uav, context)
        }
    } // go()
} // TurnUav


class TurnUavReporter extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = Helper.ContextHelper.getTurtle(context)
        val turnAllowed = Helper.getInput(args, 0).getDoubleValue
        
        TurnUav.go(uav, context, turnAllowed)
    } // perform()
} // UavUpdateSymmetricSearch


class MoveUavBackInsideWorldBoundsCommand extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = Helper.ContextHelper.getTurtle(context)
        MoveUavBackInsideWorldBounds.go(uav, context)
    } // perform()
}
