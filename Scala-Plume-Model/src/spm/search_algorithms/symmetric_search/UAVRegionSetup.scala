package spm.search_algorithms.symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper

import scala.collection.mutable.ListBuffer

class UAVRegionSetup extends Command {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    def getSubregionConfiguration(n: Int): (Int, Int, Int) = {
        var optimal = (0, 0, Int.MaxValue)
        for (y <- Range(1, (n / 2) + 1) if n % y == 0) {
            val x = n / y
            val cost = Math.abs(x - y)
            if (cost < optimal._3) optimal = (x, y, cost)
        } // for
        optimal
    } // getSubregionConfiguration()
    
    def buildSubregions(context: Context): ListBuffer[(Double, Double, Double, Double)] = {
        val population = Helper.ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double].toInt
        val subregionConfig = getSubregionConfiguration(population)
        val result = new ListBuffer[(Double, Double, Double, Double)]()
        val (worldWidth, worldHeight) = (context.world.worldWidth, context.world.worldHeight)
        val (regionWidth, regionHeight) = (worldWidth / subregionConfig._1, worldHeight / subregionConfig._2)
        var s =  ""
        
        for (y <- Range(0, worldHeight, regionHeight); x <- Range(0, worldWidth, regionWidth)) {
            result.append((x, y, x + regionWidth, y + regionHeight))
            s +=
                "(" + x.toString +
                    ", " + y.toString +
                    ", " + (x + regionWidth).toString +
                    ", " + (y + regionHeight).toString + "), "
        }
        
//        require(0>1, s)
        result
    } // buildSubregions()
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val it = Helper.ContextHelper.getWorld(context).getBreed("UAVS").iterator
        val subregions = buildSubregions(context)
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val subregion = subregions.head.productIterator.toArray.toLogoList
            Helper.BreedHelper.setBreedVariable(uav, "UAV-region", subregion)
            subregions.remove(0)
        } // while
    } // perform()
    
} // OptimalSubregionDimensions
