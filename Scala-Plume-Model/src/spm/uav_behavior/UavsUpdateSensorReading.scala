package spm.uav_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 23 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper


object UavsUpdateSensorReadingCompute {
    def go(context: Context): Unit = {
        val world = Helper.ContextHelper.getWorld(context)
        val iter = world.getBreed("UAVS").iterator
    
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val patch = uav.getPatchHere
            val patchPlumeDensityIndex = Helper.PatchHelper.getVariableIndex(patch, "plume-density")
            val patchPlumeDensity = Helper.PatchHelper.getPatchVariableByIndex(patch, patchPlumeDensityIndex).asInstanceOf[Double]
            
            Helper.BreedHelper.setBreedVariable(uav, "plume-reading", patchPlumeDensity.toLogoObject)
        }
    } // go()
} // UavsUpdateSensorReadingCompute


class UavsUpdateSensorReadingCommand extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    override def perform(args: Array[Argument], context: Context): Unit = UavsUpdateSensorReadingCompute.go(context)
} // UavsUpdateSensorReadingCommand()
