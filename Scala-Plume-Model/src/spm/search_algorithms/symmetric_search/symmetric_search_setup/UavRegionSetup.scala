package spm.search_algorithms.symmetric_search.symmetric_search_setup

// Brodderick Rodriguez
// Auburn University - CSSE
// 22 Feb. 2019

import org.nlogo.api.{Argument, Context}
import spm.helper.ContextHelper

import scala.collection.mutable.ListBuffer


object UavRegionSetup {

    def getOptimalSubregionDimensions(n: Int): (Int, Int, Int) = {
        var optimal = (0, 0, Int.MaxValue)
        for (y <- Range(1, (n / 2) + 1) if n % y == 0) {
            val x = n / y
            val cost = Math.abs(x - y)
            if (cost < optimal._3) optimal = (x, y, cost)
        }
        optimal
    } // getOptimalSubregionDimensions()
    
    
    def buildRegions(args: Array[Argument], context: Context): ListBuffer[List[Double]] = {
        val population = ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double].toInt
        val SubregionDimensions = getOptimalSubregionDimensions(population)
        val (worldWidth, worldHeight): (Double, Double) = (context.world.worldWidth - 1, context.world.worldHeight - 1)
        val (regionWidth, regionHeight) = (worldWidth / SubregionDimensions._1, worldHeight / SubregionDimensions._2)
        var (x, y) = (0.0, 0.0)
        val regions = new ListBuffer[List[Double]]()
        
        while (y <= worldHeight) {
            while (Math.round(x) < worldWidth) {
                regions.append(List(x, y, x + regionWidth, y + regionHeight))
                x += regionWidth
            } // while x
            y += regionHeight
            x = 0
        } // while y
        regions
    } // buildRegions()
    
} // _UAVSubregionGenerator
