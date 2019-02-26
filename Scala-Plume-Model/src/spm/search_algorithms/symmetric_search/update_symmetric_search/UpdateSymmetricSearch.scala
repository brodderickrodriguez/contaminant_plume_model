package spm.search_algorithms.symmetric_search.update_symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import scala.collection.mutable.ListBuffer
import scala.util.Random

import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._

import spm.helper.{BreedHelper, ContextHelper}
import spm.uav_behavior.check_uav_bounds.check_uav_inside_bounds.CheckUavInsideBounds
import spm.uav_behavior.compute_heading.ComputeHeading
import spm.boids.find_flockmates.FindFlockmates
//import spm.search_algorithms.symmetric_search.paint_subregions.PaintSubregions
import spm.uav_behavior.check_uav_bounds.check_uav_inside_world_bounds.CheckUavInsideWorldBounds
import spm.uav_behavior.turn_uav.turn.TurnUav
import spm.search_algorithms.random_search.UpdateRandomSearch


object UpdateSymmetricSearch {
    
    def updateRegionSearchTime(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val maxRegionTime = ContextHelper.getObserverVariable(context, "symmetric-search-max-region-time").asInstanceOf[Double]
        val minRegionTime = ContextHelper.getObserverVariable(context, "symmetric-search-min-region-time").asInstanceOf[Double]
        val ticks = ContextHelper.getTicks(context)
        val range = math.abs(maxRegionTime - minRegionTime).toInt
        var newRegionTime = Random.nextInt(range) + ticks + minRegionTime
        if (newRegionTime < minRegionTime) newRegionTime = minRegionTime
    
        BreedHelper.setBreedVariable(uav, "symmetric-search-max-reading-region", 0.toLogoObject)
        BreedHelper.setBreedVariable(uav, "symmetric-search-region-time", newRegionTime.toLogoObject)
    } // updateRegionSearchTime()
    
    
    def moveRegionsAccordingToWeather(context: Context): Unit = {
        val windSpeed = ContextHelper.getObserverVariable(context, "wind-speed").asInstanceOf[Double]
        val windDirection = ContextHelper.getObserverVariable(context, "wind-heading").asInstanceOf[Double]
    
        val xAddition = math.sin(math.toRadians(windDirection)) * windSpeed
        val yAddition = math.cos(math.toRadians(windDirection)) * windSpeed
    
        val world = ContextHelper.getWorld(context)
        val (worldWidth, worldHeight) = (world.worldWidth, world.worldHeight)
        val iter = world.getBreed("UAVS").iterator
    
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val region = BreedHelper.getBreedVariable(uav, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
            var newRegion = List(region.head + xAddition, region(1) + yAddition, region(2) + xAddition, region(3) + yAddition)
            newRegion = adjustRegionForWorldBounds(newRegion, worldWidth, worldHeight)
            BreedHelper.setBreedVariable(uav, "UAV-region", newRegion.toLogoList)
        }
    } // updateRegionSearchTime()

    
    def adjustRegionForWorldBounds(region: List[Double], worldWidth: Double, worldHeight: Double): List[Double] = {
        val newRegion = (List() ++ region).to[ListBuffer]
        
        if (newRegion.head < 0) {
            newRegion(2) = newRegion(2) - newRegion.head
            newRegion(0) = 0
        }
        if (newRegion(2) > worldWidth) {
            val dif = newRegion(2) - worldWidth
            newRegion(2) = newRegion(2) - dif
            newRegion(0) = newRegion.head - dif
        }
        if (newRegion(1) < 0) {
            newRegion(3) = newRegion(3) - newRegion(1)
            newRegion(1) = 0
        }
        if (newRegion(3) > worldHeight) {
            val dif = newRegion(3) - worldHeight
            newRegion(3) = newRegion(3) - dif
            newRegion(1) = newRegion(1) - dif
        }
        
        newRegion.toList
    } // adjustRegionForWorldBounds()
    
    
    def updateSingleUav(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val threshold = ContextHelper.getObserverVariable(context, "symmetric-search-region-threshold").asInstanceOf[Double]
        val uavRegion = BreedHelper.getBreedVariable(uav, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
        val maxTurn = ContextHelper.getObserverVariable(context, "symmetric-search-max-turn").asInstanceOf[Double]
    
        if (CheckUavInsideWorldBounds.perform(context, uav)) {
            if (!CheckUavInsideBounds.perform(uav, threshold, uavRegion)) {
                val (regionCenterX, regionCenterY) = ((uavRegion(2) + uavRegion.head) / 2, (uavRegion(3) + uavRegion(1)) / 2)
                val newDesiredHeading = ComputeHeading.get(uav, regionCenterX, regionCenterY) - 180
            
                uav.penMode(org.nlogo.agent.Turtle.PEN_UP)
                BreedHelper.setBreedVariable(uav, "desired-heading", newDesiredHeading.toLogoObject)
            } else {
                val maxReading = BreedHelper.getBreedVariable(uav, "symmetric-search-max-reading-region").asInstanceOf[Double]
                val plumeReading = BreedHelper.getBreedVariable(uav, "plume-reading").asInstanceOf[Double]
                val newMaxReading = math.max(maxReading, plumeReading)
            
                uav.penMode(org.nlogo.agent.Turtle.PEN_DOWN)
                BreedHelper.setBreedVariable(uav, "symmetric-search-max-reading-region", newMaxReading.toLogoObject)
                UpdateRandomSearch.behave(context, uav)
            }
        }
        checkFlockmatesDetection(context, uav)
        TurnUav.perform(uav, context, maxTurn)
    } // updateSingleUav()
    
    
    def checkFlockmatesDetection(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val regionTime = BreedHelper.getBreedVariable(uav, "symmetric-search-region-time").asInstanceOf[Double]
        val ticks = ContextHelper.getTicks(context)
        
        if (regionTime < ticks) {
            val regionReading = BreedHelper.getBreedVariable(uav, "symmetric-search-max-reading-region").asInstanceOf[Double]
            
            if (regionReading == 0) {
                val flockmates = FindFlockmates.perform(context, uav).build()
                
                if (flockmates.count > 0) {
                    val it = flockmates.iterator
                    var bestNeighbor = flockmates.getByIndex(0)
                    var bestReading = 0.0
                    
                    while (it.hasNext) {
                        val flockmate = it.next().asInstanceOf[org.nlogo.agent.Turtle]
                        val flockmateMaxReading = BreedHelper.getBreedVariable(flockmate, "symmetric-search-max-reading-region").asInstanceOf[Double]
                        if (flockmateMaxReading > bestReading) {
                            bestNeighbor = flockmate
                            bestReading = flockmateMaxReading
                        }
                    } // while
                    
                    if (bestReading > regionReading) {
//                        PaintSubregions.paintSingleUavRegion(context, uav, black=true)
//                        PaintSubregions.paintSingleUavRegion(context, bestNeighbor.asInstanceOf[org.nlogo.agent.Turtle])
                        val flockmateRegion = BreedHelper.getBreedVariable(bestNeighbor, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
                        BreedHelper.setBreedVariable(uav, "UAV-region", flockmateRegion.toLogoList)
                    }
                } // if flockmates
            } // if regionReading > 0
    
            updateRegionSearchTime(context, uav)
        } // if regionTime < ticks
    } // checkFlockmatesDetection()
    
} // UpdateSymmetricSearch
