package spm.boids

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._

import spm.Helper

class FindBestNeighbor extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val thisUav = Helper.ContextHelper.getTurtle(context)
        val flockmates = Helper.BreedHelper.getBreedVariable(thisUav, "flockmates").asInstanceOf[org.nlogo.agent.AgentSet]
        
        if (flockmates.count == 0) return
        
        var bestNeighbor = flockmates.randomOne(flockmates.count, 0)
        var bestPlumeReading: Double = -1.0
    
        val it = flockmates.iterator
        while (it.hasNext) {
            val fm = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val flockmatePlumeReading = Helper.BreedHelper.getBreedVariable(fm, "plume-reading").asInstanceOf[Double]
            if (flockmatePlumeReading >= bestPlumeReading) {
                bestNeighbor = fm
                bestPlumeReading = flockmatePlumeReading
            }
        } // while
        
        Helper.BreedHelper.setBreedVariable(thisUav, "best-neighbor", bestNeighbor.toLogoObject)
    } // perform()
    
} // FindBestNeighbor
