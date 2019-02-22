package spm.search_algorithms.symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper
import spm.uav_behavior.{CheckBoundsUav, ComputeHeading}


object UavUpdateSymmetricSearchRegionTime {
    def go(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val maxRegionTime = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-max-region-time").asInstanceOf[Double]
        val minRegionTime = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-min-region-time").asInstanceOf[Double]
        val ticks = Helper.ContextHelper.getTicks(context)
        val range = math.abs(maxRegionTime - minRegionTime + 1).toInt
        val newRegionTime = scala.util.Random.nextInt(range) + ticks
        Helper.BreedHelper.setBreedVariable(uav, "symmetric-search-max-reading-region", 0.toLogoObject)
        Helper.BreedHelper.setBreedVariable(uav, "symmetric-search-region-time", newRegionTime.toLogoObject)
    }
}

object MoveRegionsAccordingToWeather {
    def go(context: Context): Unit = {
        val windSpeed = Helper.ContextHelper.getObserverVariable(context, "wind-speed").asInstanceOf[Double]
        val windDirection = Helper.ContextHelper.getObserverVariable(context, "wind-heading").asInstanceOf[Double]

        val xAddition = math.sin(math.toRadians(windDirection)) * windSpeed
        val yAddition = math.cos(math.toRadians(windDirection)) * windSpeed
        
        val world = Helper.ContextHelper.getWorld(context)
        val (worldWidth, worldHeight) = (world.worldWidth, world.worldHeight)
        val iter = world.getBreed("UAVS").iterator
    
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            moveSingleUavRegion(uav, xAddition, yAddition, worldWidth, worldHeight)
            PaintRegionOfUAV.go(context, uav)
        }
    } // go()
    
    def moveSingleUavRegion(uav: org.nlogo.agent.Agent, xAddition: Double, yAddition: Double, worldWidth: Double, worldHeight: Double): Unit = {
        val region = Helper.BreedHelper.getBreedVariable(uav, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
        val newRegion = List(region.head + xAddition, region(1) + yAddition, region(2) + xAddition, region(3) + yAddition)
        
        if (newRegion.head < 0) {
            newRegion(2) -= newRegion.head
            newRegion(0) = 0
        }
        if (newRegion(2) > worldWidth) {
            val dif = newRegion(2) - worldWidth
            newRegion(2) -= dif
            newRegion.head -= dif
        }
        if (newRegion(1) < 0) {
            newRegion(3) -= newRegion(1)
            newRegion(1) = 0
        }
        
        newRegion = adjustRegionForWorldBounds(newRegion, worldWidth, worldHeight)
        
        Helper.BreedHelper.setBreedVariable(uav, "UAV-region", newRegion.toLogoList)
    } // moveSingleUavRegion()
    
    
    def adjustRegionForWorldBounds(region: List[Double], worldWidth: Double, worldHeight: Double): List[Double] = {
        val newRegion = List() ++ region
    
        if (newRegion.head < 0) {
            newRegion(2) -= newRegion.head
            newRegion(0) = 0
        }
        if (newRegion(2) > worldWidth) {
            val dif = newRegion(2) - worldWidth
            newRegion(2) -= dif
            newRegion.head -= dif
        }
        if (newRegion(1) < 0) {
            newRegion(3) -= newRegion(1)
            newRegion(1) = 0
        }
    
    
    }
    
} // MoveRegionsAccordingToWeather


object _UavUpdateSymmetricSearchIndividual {
    def behave(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val threshold = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-region-threshold").asInstanceOf[Double]
        val uavRegion = Helper.BreedHelper.getBreedVariable(uav, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
        val maxTurn = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-max-turn").asInstanceOf[Double]
    
        if (CheckBoundsUav.uavInsideWorld(context, uav)) {
            if (!spm.uav_behavior.CheckBoundsUav.uavInside(uav, threshold, uavRegion)) {
                val (regionCenterX, regionCenterY) = ((uavRegion(2) + uavRegion.head) / 2, (uavRegion(3) + uavRegion(1)) / 2)
                val newDesiredHeading = ComputeHeading.get(uav, regionCenterX, regionCenterY) - 180
                
                uav.penMode(org.nlogo.agent.Turtle.PEN_UP)
                Helper.BreedHelper.setBreedVariable(uav, "desired-heading", newDesiredHeading.toLogoObject)
            } else {
                val maxReading = Helper.BreedHelper.getBreedVariable(uav, "symmetric-search-max-reading-region").asInstanceOf[Double]
                val plumeReading = Helper.BreedHelper.getBreedVariable(uav, "plume-reading").asInstanceOf[Double]
                val newMaxReading = math.max(maxReading, plumeReading)
    
                uav.penMode(org.nlogo.agent.Turtle.PEN_DOWN)
                Helper.BreedHelper.setBreedVariable(uav, "symmetric-search-max-reading-region", newMaxReading.toLogoObject)
                spm.search_algorithms.random_search._UavRandomSearchBehavior.behave(context, uav)
            }
        }
        checkFlockmatesDetection(context, uav)
        spm.uav_behavior.TurnUav.go(uav, context, maxTurn)
    } // behave()
    
    
    def checkFlockmatesDetection(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val regionTime = Helper.BreedHelper.getBreedVariable(uav, "symmetric-search-region-time").asInstanceOf[Double]
        val ticks = Helper.ContextHelper.getTicks(context)
        
        if (regionTime < ticks) {
            val regionReading = Helper.BreedHelper.getBreedVariable(uav, "symmetric-search-max-reading-region").asInstanceOf[Double]
            
            if (regionReading == 0) {
                val flockmates = spm.boids.FindFlockmates.go2(context, uav).build()
            
                if (flockmates.count > 0) {
                    val it = flockmates.iterator
                    var bestNeighbor = flockmates.getByIndex(0)
                    var bestReading = 0.0
    
                    while (it.hasNext) {
                        val flockmate = it.next().asInstanceOf[org.nlogo.agent.Turtle]
                        val flockmateMaxReading = Helper.BreedHelper.getBreedVariable(flockmate, "symmetric-search-max-reading-region").asInstanceOf[Double]
                        if (flockmateMaxReading > bestReading) {
                            bestNeighbor = flockmate
                            bestReading = flockmateMaxReading
                        }
                    } // while
    
                    PaintRegionOfUAV.go(context, uav, true)
                    val flockmateRegion = Helper.BreedHelper.getBreedVariable(bestNeighbor, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
                    Helper.BreedHelper.setBreedVariable(uav, "UAV-region", flockmateRegion.toLogoList)
    
                } // if flockmates
            } // if regionReading > 0
            
            UavUpdateSymmetricSearchRegionTime.go(context, uav)
        } // if regionTime < ticks
    } // checkFlockmatesDetection()
    
} // _UavUpdateSymmetricSearch


class UavUpdateSymmetricSearch extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val it = Helper.ContextHelper.getWorld(context).getBreed("UAVS").iterator
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            _UavUpdateSymmetricSearchIndividual.behave(context, uav)
        } // while
    
        MoveRegionsAccordingToWeather.go(context)
        
    } // perform()
} // UavUpdateSymmetricSearch


//class UavUpdateSymmetricSearchIndividual extends Command {
//    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
//
//    override def perform(args: Array[Argument], context: Context): Unit = {
//        val uav = Helper.ContextHelper.getTurtle(context)
//        _UavUpdateSymmetricSearchIndividual.behave(context, uav)
//    } // perform()
//} // UavUpdateSymmetricSearchIndividual
