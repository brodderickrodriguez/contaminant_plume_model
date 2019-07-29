package spm.performance_metrics.compute_coverage

// Brodderick Rodriguez
// Auburn University - CSSE
// 23 Feb. 2019

import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import spm.helper.{ContextHelper, BreedHelper}

import scala.collection.mutable.ListBuffer


object ComputeCoverage {
    
    def compute(context: Context): Unit = {
        val coverageAll = computeCoverageAll(context)
        val mean = computeCoverageMean(context, coverageAll)
        val std = computeCoverageStd(context, coverageAll, mean)

        ContextHelper.setObserverVariable(context, "coverage-all", coverageAll.toLogoList)
        ContextHelper.setObserverVariable(context, "coverage-mean", mean.toLogoObject)
        ContextHelper.setObserverVariable(context, "coverage-std", std.toLogoObject)
    } // compute()
    
    
    def computeCoverageAll(context: Context): List[Double] = {
        val world = ContextHelper.getWorld(context)
        val ticks = ContextHelper.getTicks(context)
        val dataDecay = ContextHelper.getObserverVariable(context, "coverage-data-decay").asInstanceOf[Double]
        val coverageAll = ContextHelper.getObserverVariable(context, "coverage-all").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double]).to[ListBuffer]
        val uavs = world.getBreed("UAVS")
        val iter = uavs.iterator
        
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val sensorReading = BreedHelper.getBreedVariable(uav, "plume-reading").asInstanceOf[Double]
            coverageAll.append(sensorReading)
        }
        
        if (ticks > dataDecay)
            for (_ <- Range(0, uavs.count))
                coverageAll.remove(0)
        
        coverageAll.toList
    } // computeCoverageAll()
    
    
    def computeCoverageMean(context: Context, coverageAll: List[Double]): Double = coverageAll.sum / coverageAll.length
    
    
    def computeCoverageStd(context: Context, coverageAll: List[Double], mean: Double): Double = {
        math.sqrt(coverageAll.map(a => math.pow(a - mean, 2)).sum / coverageAll.length)
    } // computeCoverageStd()

    // the uavs coverageAll / the total density of the plumes
    def computeCoveragePerPlumeDensity(context: Context, coverageAll: List[Double]): Double = {
        val world = ContextHelper.getWorld(context)
        val plumes = world.getBreed("CONTAMINANT-PLUMES")
        val iter = world.getBreed("CONTAMINANT-PLUMES").iterator

        if (iter.hasNext) {
            val singlePlume = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val plumeSpreadPatches = BreedHelper.getBreedVariable(singlePlume, "plume-spread-patches").asInstanceOf[Double]
            var accumulativePlumeDensity = math.Pi * math.pow(plumeSpreadPatches, 2) + math.Pi * plumeSpreadPatches

            accumulativePlumeDensity *= plumes.count
            coverageAll.sum / accumulativePlumeDensity
        }
        else -1
    } // computeCoveragePerPlumeDensity()
    
} // Coverage
