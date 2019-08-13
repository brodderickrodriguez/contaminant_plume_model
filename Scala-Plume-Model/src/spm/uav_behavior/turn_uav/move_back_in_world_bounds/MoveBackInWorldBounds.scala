package spm.uav_behavior.turn_uav.move_back_in_world_bounds

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import scala.util.Random

import org.nlogo.api.Context
import org.nlogo.api.ScalaConversions._

import spm.boids.turn_towards.TurnTowards
import spm.helper.{BreedHelper, ContextHelper}
import spm.uav_behavior.compute_heading.ComputeHeading


object MoveBackInWorldBounds {
    
    def perform(uav: org.nlogo.agent.Turtle, context: Context): Unit = {
//        val ptx = (context.world.worldWidth / 4) + Random.nextInt(context.world.worldWidth / 2)
//        val pty = (context.world.worldHeight / 4) + Random.nextInt(context.world.worldHeight / 2)

        val ptx = (context.world.worldWidth / 2) + (context.world.worldWidth / 4)
        val pty = (context.world.worldHeight / 2) + (context.world.worldHeight / 4)
        
        val newDesiredHeading = ComputeHeading.get(uav, ptx, pty) - 180
        val worldThreshold = ContextHelper.getObserverVariable(context, "max-world-edge-turn").asInstanceOf[Double]
        
        BreedHelper.setBreedVariable(uav, "desired-heading", newDesiredHeading.toLogoObject)
        TurnTowards.go(uav, newDesiredHeading, worldThreshold)
    } // perform()
    
} // MoveBackInWorldBounds
