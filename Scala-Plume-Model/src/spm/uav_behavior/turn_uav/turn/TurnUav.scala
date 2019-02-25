package spm.uav_behavior.turn_uav.turn

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.Context

import spm.boids.turn_towards.TurnTowards
import spm.helper.BreedHelper
import spm.uav_behavior.check_uav_bounds.check_uav_inside_world_bounds.CheckUavInsideWorldBounds
import spm.uav_behavior.turn_uav.move_back_in_world_bounds.MoveBackInWorldBounds


object TurnUav {
    
    def perform(uav: org.nlogo.agent.Turtle, context: Context, turnAllowed: Double): Unit = {
        val desiredHeading = BreedHelper.getBreedVariable(uav, "desired-heading").asInstanceOf[Double]
        
        if (CheckUavInsideWorldBounds.perform(context, uav))
            TurnTowards.go(uav, desiredHeading, turnAllowed)
        else
            MoveBackInWorldBounds.perform(uav, context)
        
    } // go()
    
} // TurnUav
