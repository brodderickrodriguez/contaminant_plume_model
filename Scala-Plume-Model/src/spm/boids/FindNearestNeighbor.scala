package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 12 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import spm.helper.{Helper, MathHelper}


class FindNearestNeighbor extends Command  {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val thisUav = Helper.ContextHelper.getTurtle(context)
        val thisUavCoor = Helper.TurtleHelper.getTurtleCoors(thisUav)
        val flockmates = Helper.BreedHelper.getBreedVariable(thisUav, "flockmates").asInstanceOf[org.nlogo.agent.AgentSet]
    
        if (flockmates.count == 0) {
            Helper.BreedHelper.setBreedVariable(thisUav, "nearest-neighbor", NobodyType)
            return
        }
    
        var nearestNeighbor = flockmates.randomOne(flockmates.count, 0).asInstanceOf[org.nlogo.agent.Turtle]
        val nearestNeighborCor = Helper.TurtleHelper.getTurtleCoors(nearestNeighbor)
        var nearestNeighborDistance = MathHelper.euclideanDistance(thisUavCoor, nearestNeighborCor)
    
        val it = flockmates.iterator
        while (it.hasNext) {
            val fm = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val fmCoor = Helper.TurtleHelper.getTurtleCoors(fm)
            val dist = MathHelper.euclideanDistance(thisUavCoor, fmCoor)
            
            if (dist <= nearestNeighborDistance) {
                nearestNeighbor = fm
                nearestNeighborDistance = dist
            }
        } // while
    
        Helper.BreedHelper.setBreedVariable(thisUav, "nearest-neighbor", nearestNeighbor.toLogoObject)
        
    } // perform()
    
} // FindNearestNeighbor
