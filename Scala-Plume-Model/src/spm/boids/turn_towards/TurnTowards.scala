package spm.boids.turn_towards

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import spm.helper.TurtleHelper
import spm.boids.turn_at_most.TurnAtMost


object TurnTowards {
    
    def go(uav: org.nlogo.agent.Turtle, newHeading: Double, maxTurn: Double): Unit = {
        val currentHeading = TurtleHelper.getTurtleVariable(uav, "heading").asInstanceOf[Double]
        val computedHeading = org.nlogo.agent.Turtle.subtractHeadings(currentHeading, newHeading)
        
        TurnAtMost.go(uav, computedHeading, maxTurn)
    } // go()
    
} // TurnTowards
