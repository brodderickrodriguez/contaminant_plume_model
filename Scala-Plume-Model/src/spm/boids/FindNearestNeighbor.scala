package spm.boids

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import spm.{Helper, MathHelper}


class FindNearestNeighbor extends Command  {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val thisUav = Helper.ContextHelper.getTurtle(context)
        val thisUavCoor = Helper.TurtleHelper.getTurtleCoors(thisUav)
        val flockmates = Helper.BreedHelper.getBreedVariable(thisUav, "flockmates").asInstanceOf[org.nlogo.agent.AgentSet]
    
        if (flockmates.count == 0) return
    
        var nearestNeighbor = flockmates.randomOne(flockmates.count, 0).asInstanceOf[org.nlogo.agent.Turtle]
        val nearestNeighborCor = Helper.TurtleHelper.getTurtleCoors(nearestNeighbor)
        var nearestNeighborDistance = MathHelper.euclideanDistance(thisUavCoor, nearestNeighborCor)
    
        val it = flockmates.iterator
        while (it.hasNext) {
            val fm = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val fmCoor = Helper.TurtleHelper.getTurtleCoors(fm)
            
            val dist = MathHelper.euclideanDistance(thisUavCoor, fmCoor)
            Helper.BreedHelper.setBreedVariable(fm, "nnd", dist.toLogoObject)
            if (dist <= nearestNeighborDistance) {
                nearestNeighbor = fm
                nearestNeighborDistance = dist
            }
        } // while
    
        Helper.BreedHelper.setBreedVariable(thisUav, "nearest-neighbor", nearestNeighbor.toLogoObject)
        
    } // perform()
    
} // FindNearestNeighbor
