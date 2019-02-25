package spm.boids

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.agent.AgentSetBuilder
import spm.helper.{MathHelper, ContextHelper, TurtleHelper, BreedHelper}


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


class FindFlockmatesCommand extends Command {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        val flockmates = FindFlockmates.perform(context, uav)
        BreedHelper.setBreedVariable(uav, "flockmates", flockmates.build())
    } // perform()
    
} // FindFlockmates
