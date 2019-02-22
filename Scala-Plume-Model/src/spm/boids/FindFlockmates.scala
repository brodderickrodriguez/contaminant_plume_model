package spm.boids

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import org.nlogo.agent.AgentSetBuilder
import spm.helper.{Helper, MathHelper}


object FindFlockmates {
    def go(context: Context): AgentSetBuilder = {
        val flockmates = new AgentSetBuilder(org.nlogo.core.AgentKind.Turtle)
        val uavVision = Helper.ContextHelper.getObserverVariable(context, "uav-vision").asInstanceOf[Double]
    
        val thisUav = Helper.ContextHelper.getTurtle(context)
        val thisUavCoor = Helper.TurtleHelper.getTurtleCoors(thisUav)
    
        val world = Helper.ContextHelper.getWorld(context)
        val iter = world.getBreed("UAVS").iterator
    
        while (iter.hasNext) {
            val otherUav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val otherUavCoor = Helper.TurtleHelper.getTurtleCoors(otherUav)
        
            if (otherUav != thisUav && MathHelper.euclideanDistance(thisUavCoor, otherUavCoor) <= uavVision)
                flockmates.add(otherUav)
        } // while
    
        Helper.BreedHelper.setBreedVariable(thisUav, "flockmates", flockmates.build().toLogoObject)
        flockmates
    }
    
    def go2(context: Context, uav: org.nlogo.agent.Turtle): AgentSetBuilder = {
        val flockmates = new AgentSetBuilder(org.nlogo.core.AgentKind.Turtle)
        val uavVision = Helper.ContextHelper.getObserverVariable(context, "uav-vision").asInstanceOf[Double]
    
        val thisUavCoor = Helper.TurtleHelper.getTurtleCoors(uav)
    
        val world = Helper.ContextHelper.getWorld(context)
        val iter = world.getBreed("UAVS").iterator
    
        while (iter.hasNext) {
            val otherUav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val otherUavCoor = Helper.TurtleHelper.getTurtleCoors(otherUav)
        
            if (otherUav != uav && MathHelper.euclideanDistance(thisUavCoor, otherUavCoor) <= uavVision)
                flockmates.add(otherUav)
        } // while
    
//        Helper.BreedHelper.setBreedVariable(thisUav, "flockmates", flockmates.build().toLogoObject)
        flockmates
        
    }
}


class FindFlockmatesCommand extends Command {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        FindFlockmates.go(context)
    } // perform()
    
} // FindFlockmates
