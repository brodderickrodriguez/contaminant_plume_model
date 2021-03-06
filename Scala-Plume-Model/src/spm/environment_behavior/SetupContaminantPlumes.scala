package spm.environment_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 21 Feb. 2019

import scala.util.Random

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._

import spm.helper.{ContextHelper, TurtleHelper, BreedHelper}


class SetupContaminantPlumes  extends Command {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val world = ContextHelper.getWorld(context)
        val plumeSpreadRadius = ContextHelper.getObserverVariable(context, "plume-spread-radius").asInstanceOf[Double]
        val (wWidth, wHeight) = (world.worldWidth, world.worldHeight)
        val (xMin, yMin) = (wWidth * 0.25, wHeight * 0.25)
        val (xRange, yRange) = (wWidth / 2, wHeight / 2)
        val plumeSpreadPatches = wWidth * (plumeSpreadRadius / 2)
        val iter = world.getBreed("CONTAMINANT-PLUMES").iterator
        
        while (iter.hasNext) {
            val plume = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val x = (Random.nextFloat() * xRange) + xMin
            val y = (Random.nextFloat() * yRange) + yMin
            
            TurtleHelper.setTurtleVariable(plume, "shape", "circle".toLogoObject)
            TurtleHelper.setTurtleVariable(plume, "size", (plumeSpreadPatches * 2).toLogoObject)
            TurtleHelper.setTurtleVariable(plume, "color", 15.toLogoObject)
            TurtleHelper.setTurtleVariable(plume, "xcor", x.toLogoObject)
            TurtleHelper.setTurtleVariable(plume, "ycor", y.toLogoObject)
            BreedHelper.setBreedVariable(plume, "plume-spread-patches", plumeSpreadPatches.toLogoObject)
        }
    } // perform()
    
} // SetupContaminantPlumes
