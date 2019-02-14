package spm.uav_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper


//NOTE: might end up removing this class
class CheckTurtleInsideBounds extends Reporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = BooleanType)
    
    def ad(a: AnyRef): Double = a.asInstanceOf[Double]
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val uav = Helper.ContextHelper.getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
        val threshold = Helper.getInput(args, 0).getDoubleValue
        val r = Helper.getInput(args, 1).getList.toArray
        val region = (ad(r(0)), ad(r(1)), ad(r(2)), ad(r(3)))
        CheckBoundsUav.uavInsideBounds(context, uav, threshold, region).toLogoObject
    } // perform()
}


object CheckBoundsUav {
    
    def UavInsideWorldBoundsThreshold(context: Context): Boolean = {
        val uav = Helper.ContextHelper.getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
        val threshold = Helper.ContextHelper.getObserverVariable(context, "world-edge-threshold").asInstanceOf[Double]
        val worldRegion = (0.0, 0.0, context.world.worldWidth.toDouble, context.world.worldHeight.toDouble)
        uavInsideBounds(context, uav, threshold, worldRegion)
    } // UavInsideWorldBoundsThreshold()
    
    
    // TODO: fix this crap logic
    def uavInsideBounds(context: Context,
                        uav: org.nlogo.agent.Turtle,
                        threshold: Double,
                        region: (Double, Double, Double, Double)): Boolean = {
     
        val (uavX, uavY) = Helper.TurtleHelper.getTurtleCoors(uav)
        
        val a = uavX - threshold < region._1
        val b = uavY - threshold < region._2
        val c = Math.abs(uavX + threshold) > region._3
        val d = Math.abs(uavY + threshold) > region._4
        
        val tf = (a || b) || (c || d)
        !tf
    } // UavInsideBounds()
    
} // CheckBoundsUav()
