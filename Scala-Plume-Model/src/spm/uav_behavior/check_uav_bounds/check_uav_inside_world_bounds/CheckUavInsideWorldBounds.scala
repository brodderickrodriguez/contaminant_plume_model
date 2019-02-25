package spm.uav_behavior.check_uav_bounds.check_uav_inside_world_bounds

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api._

import spm.helper.ContextHelper
import spm.uav_behavior.check_uav_bounds.check_uav_inside_bounds.CheckUavInsideBounds


object CheckUavInsideWorldBounds {
    
    def perform(context: Context, uav: org.nlogo.agent.Turtle): Boolean = {
        val threshold = ContextHelper.getObserverVariable(context, "world-edge-threshold").asInstanceOf[Double]
        val worldRegion = List(0.0, 0.0, context.world.worldWidth.toDouble, context.world.worldHeight.toDouble)
        CheckUavInsideBounds.perform(uav, threshold, worldRegion)
    } // uavInsideWorld()
    
} // CheckUavInsideWorldBounds()
