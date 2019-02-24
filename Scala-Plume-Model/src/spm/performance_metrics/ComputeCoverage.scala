package spm.performance_metrics

// Brodderick Rodriguez
// Auburn University - CSSE
// 23 Feb. 2019

import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper
import spm.uav_behavior.{CheckBoundsUav, ComputeHeading}

import scala.collection.mutable.ListBuffer


object Coverage {
    def compute(context: Context): Unit = {
        val coverageAll = computeCoverageAll(context)
        val mean = computeCoverageMean(context, coverageAll)
        val std = computeCoverageStd(context, coverageAll, mean)
        val coveragePerPlumeDensity = computeCoveragePerPlumeDensity(context, coverageAll)
    
        Helper.ContextHelper.setObserverVariable(context, "coverage-all", coverageAll.toLogoList)
        Helper.ContextHelper.setObserverVariable(context, "coverage-mean", mean.toLogoObject)
        Helper.ContextHelper.setObserverVariable(context, "coverage-std", std.toLogoObject)
        Helper.ContextHelper.setObserverVariable(context, "coverage-per-plume-density", coveragePerPlumeDensity.toLogoObject)
    }
    
    def computeCoverageAll(context: Context): List[Double] = {
        val world = Helper.ContextHelper.getWorld(context)
        val ticks = Helper.ContextHelper.getTicks(context)
        val dataDecay = Helper.ContextHelper.getObserverVariable(context, "coverage-data-decay").asInstanceOf[Double]
        val coverageAll = Helper.ContextHelper.getObserverVariable(context, "coverage-all").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double]).to[ListBuffer]
        val uavs = world.getBreed("UAVS")
        val iter = uavs.iterator
    
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val sensorReading = Helper.BreedHelper.getBreedVariable(uav, "plume-reading").asInstanceOf[Double]
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
    
    def computeCoveragePerPlumeDensity(context: Context, coverageAll: List[Double]): Double = {
        val world = Helper.ContextHelper.getWorld(context)
        val plumes = world.getBreed("CONTAMINANT-PLUMES")
        val iter = world.getBreed("CONTAMINANT-PLUMES").iterator
        
        if (iter.hasNext) {
            val singlePlume = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            val plumeSpreadPatches = Helper.BreedHelper.getBreedVariable(singlePlume, "plume-spread-patches").asInstanceOf[Double]
            var accumulativePlumeDensity = math.Pi * math.pow(plumeSpreadPatches, 2) + math.Pi * plumeSpreadPatches
            
            accumulativePlumeDensity *= plumes.count
            coverageAll.sum / accumulativePlumeDensity
        }
        else -1
        
    } // computeCoveragePerPlumeDensity()
    
} // Coverage




class ComputeCoverage extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    override def perform(args: Array[Argument], context: Context): Unit = Coverage.compute(context)
} // ComputeCoverage
