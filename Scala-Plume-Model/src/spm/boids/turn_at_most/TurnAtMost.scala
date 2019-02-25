package spm.boids.turn_at_most

// Brodderick Rodriguez
// Auburn University - CSSE
// 19 Feb. 2019

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
