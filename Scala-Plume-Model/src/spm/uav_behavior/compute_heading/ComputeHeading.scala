package spm.uav_behavior.compute_heading

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import spm.helper.TurtleHelper


object ComputeHeading {
    
    def get(uav: org.nlogo.agent.Turtle, x: Double, y: Double): Double = {
        val (xcor, ycor) = TurtleHelper.getTurtleCoors(uav)
        val (dx, dy) = (xcor - x, ycor - y)
        if (dx == 0)
            if (dy > 0) 180 else 360
        else if (dy == 0)
            if (dx > 0) 270 else 90
        else
            (StrictMath.toDegrees(StrictMath.atan2(dx, dy)) % 360) + 180
    } // get()
    
} // ComputeHeading
