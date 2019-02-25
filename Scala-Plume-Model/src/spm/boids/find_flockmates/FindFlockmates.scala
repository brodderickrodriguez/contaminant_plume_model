package spm.boids.find_flockmates

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.agent.AgentSetBuilder
import org.nlogo.api.Context

import spm.helper.{ContextHelper, MathHelper, TurtleHelper}


object FindFlockmates {
    
    def perform(context: Context): AgentSetBuilder = _findFlockmates(context, ContextHelper.getTurtle(context))
    
    
    def perform(context: Context, uav: org.nlogo.agent.Turtle): AgentSetBuilder = _findFlockmates(context, uav)
    
    
    def _findFlockmates(context: Context, uav: org.nlogo.agent.Turtle): AgentSetBuilder = {
        val flockmates = new AgentSetBuilder(org.nlogo.core.AgentKind.Turtle)
        val uavVision = ContextHelper.getObserverVariable(context, "uav-vision").asInstanceOf[Double]
        val thisUavCoor = TurtleHelper.getTurtleCoors(uav)
        val world = ContextHelper.getWorld(context)
        val iter = world.getBreed("UAVS").iterator
        
        while (iter.hasNext) {
            val otherUav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val otherUavCoor = TurtleHelper.getTurtleCoors(otherUav)
            
            if (otherUav != uav && MathHelper.euclideanDistance(thisUavCoor, otherUavCoor) <= uavVision)
                flockmates.add(otherUav)
        } // while
        
        flockmates
    } // _findFlockmates()
    
} // FindFlockmates