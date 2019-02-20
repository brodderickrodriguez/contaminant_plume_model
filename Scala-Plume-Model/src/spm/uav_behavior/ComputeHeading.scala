package spm.uav_behavior

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Context}
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper


object ComputeHeading {
    
    object GetHeadingTowardsPoint {
        def get(uav: org.nlogo.agent.Turtle, x: Double, y: Double): Double = {
            val (xcor, ycor) = Helper.TurtleHelper.getTurtleCoors(uav)
            val (dx, dy) = (xcor - x, ycor - y)
            if (dx == 0)
                if (dy > 0) 180 else 360
            else if (dy == 0)
                if (dx > 0) 270 else 90
            else
                (StrictMath.toDegrees(StrictMath.atan2(dx, dy)) % 360) + 180
    

        } // getHeadingTowardsPoint()
    
        class GetHeadingTowardsPointReporter extends Reporter {
            override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = NumberType)
    
            def report(args: Array[Argument], context: Context): AnyRef = {
                val argx = Helper.getInput(args, 0).getDoubleValue
                val argy = Helper.getInput(args, 1).getDoubleValue
                val uav = Helper.ContextHelper.getTurtle(context)
                get(uav, argx, argy).toLogoObject
    
    
              
//                validDouble(StrictMath.toDegrees(StrictMath.atan2(1, 1)) + 360, context) % 360
            }
        } // getHeadingTowardsPointReporter
    } // getHeadingTowardsPoint
    
} // ComputeHeading
