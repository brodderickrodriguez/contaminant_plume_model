package spm.uav_behavior.check_uav_bounds.check_uav_inside_bounds

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import spm.helper.TurtleHelper


object CheckUavInsideBounds {
    
    def perform(uav: org.nlogo.agent.Turtle, threshold: Double, region: List[Double]): Boolean = {
        val (uavX, uavY) = TurtleHelper.getTurtleCoors(uav)
        val a = uavX - threshold < region.head
        val b = uavY - threshold < region(1)
        val c = Math.abs(uavX + threshold) > region(2)
        val d = Math.abs(uavY + threshold) > region(3)
        
        val tf = (a || b) || (c || d)
        !tf
    } // perform()
    
} // CheckUavInsideBounds()
