//package spm.uav_behavior
//
//// Brodderick Rodriguez
//// Auburn University - CSSE
//// 13 Feb. 2019
//
//import org.nlogo.api.ScalaConversions._
//import org.nlogo.api._
//import org.nlogo.core.Syntax
//import org.nlogo.core.Syntax._
//import spm.helper.{InputHelper, ContextHelper, TurtleHelper}
//
//
//class CheckUavInsideWorldBoundsReporter extends Reporter {
//    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = BooleanType)
//
//    def report(args: Array[Argument], context: Context): AnyRef = {
//        val uav = ContextHelper.getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
//        CheckBoundsUav.uavInsideWorld(context, uav).toLogoObject
//    } // perform()
//} // CheckUavInsideWorldBounds()
//
////
//////NOTE: might end up removing this class
////class CheckTurtleInsideBounds extends Reporter {
////    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = BooleanType)
////
////    def report(args: Array[Argument], context: Context): AnyRef = {
////        val uav = ContextHelper.getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
////        val threshold = InputHelper.getInput(args, 0).getDoubleValue
////        val region = InputHelper.getInput(args, 1).getList.toList.map(_.asInstanceOf[Double])//.toArray.map(_.asInstanceOf[Double])
////        CheckBoundsUav.uavInside(uav, threshold, region).toLogoObject
////    } // perform()
////} // CheckTurtleInsideBounds
//
//
//object CheckBoundsUav {
//    // TODO: fix this crap logic
//    def uavInside(uav: org.nlogo.agent.Turtle, threshold: Double, region: List[Double]): Boolean = {
//        val (uavX, uavY) = TurtleHelper.getTurtleCoors(uav)
//        val a = uavX - threshold < region.head
//        val b = uavY - threshold < region(1)
//        val c = Math.abs(uavX + threshold) > region(2)
//        val d = Math.abs(uavY + threshold) > region(3)
//
//        val tf = (a || b) || (c || d)
//        !tf
//    } // UavInsideBounds()
//
//
//    def uavInsideWorld(context: Context, uav: org.nlogo.agent.Turtle): Boolean = {
//        val threshold = ContextHelper.getObserverVariable(context, "world-edge-threshold").asInstanceOf[Double]
//        val worldRegion = List(0.0, 0.0, context.world.worldWidth.toDouble, context.world.worldHeight.toDouble)
//        CheckBoundsUav.uavInside(uav, threshold, worldRegion)
//    } // uavInsideWorld()
//} // CheckBoundsUav()
