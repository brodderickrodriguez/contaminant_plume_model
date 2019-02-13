package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 12 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import org.nlogo.api.ScalaConversions._
import spm.helper.Helper

class FindBestNeighbor extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val thisUav = Helper.ContextHelper.getTurtle(context)
        val flockmates = Helper.BreedHelper.getBreedVariable(thisUav, "flockmates").asInstanceOf[org.nlogo.agent.AgentSet]
        
        if (flockmates.count == 0) {
            Helper.BreedHelper.setBreedVariable(thisUav, "best-neighbor", NobodyType)
            return
        }
        
        var bestNeighbor = flockmates.randomOne(flockmates.count, scala.util.Random.nextInt(flockmates.count))
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
