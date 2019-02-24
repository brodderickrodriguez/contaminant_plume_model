package spm.environment_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 23 Feb. 2019

import scala.util.Random

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._

import spm.helper.Helper

class UpdateContaminantPlumes  extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
//        val world = Helper.ContextHelper.getWorld(context)
//        val plumeSpreadRadius = Helper.ContextHelper.getObserverVariable(context, "plume-spread-radius").asInstanceOf[Double]
//        val (wWidth, wHeight) = (world.worldWidth, world.worldHeight)
//        val (xMin, yMin) = (wWidth * 0.25, wHeight * 0.25)
//        val (xRange, yRange) = (wWidth / 2, wHeight / 2)
//        val plumeSpreadPatches = wWidth * (plumeSpreadRadius / 2)
//        val iter = world.getBreed("CONTAMINANT-PLUMES").iterator
//
//        while (iter.hasNext) {
//            val plume = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
//            val x = (Random.nextFloat() * xRange) + xMin
//            val y = (Random.nextFloat() * yRange) + yMin
//
//            Helper.TurtleHelper.setTurtleVariable(plume, "shape", "circle".toLogoObject)
//            Helper.TurtleHelper.setTurtleVariable(plume, "size", (plumeSpreadPatches * 2).toLogoObject)
//            Helper.TurtleHelper.setTurtleVariable(plume, "color", 15.toLogoObject)
//            Helper.TurtleHelper.setTurtleVariable(plume, "xcor", x.toLogoObject)
//            Helper.TurtleHelper.setTurtleVariable(plume, "ycor", y.toLogoObject)
//            Helper.BreedHelper.setBreedVariable(plume, "plume-spread-patches", plumeSpreadPatches.toLogoObject)
//        }
    } // perform()
    
} // SetupContaminantPlumes
