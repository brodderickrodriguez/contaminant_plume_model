// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._
import spm.boids._
import spm.uav_behavior.{CheckTurtleInsideBounds, CheckUavInsideWorldBounds}
import spm.uav_behavior.ComputeHeading.GetHeadingTowardsPoint
import spm.uav_behavior.TurnUav
import spm.search_algorithms.random_search.{UavRandomSearchPaintSubregions, UpdateRandomSearch, UpdateRandomSearchIndividual}
import spm.search_algorithms.symmetric_search.{UAVRegionSetup, UavUpdateSymmetricSearch, UavUpdateSymmetricSearchIndividual}

class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
//        manager.addPrimitive("report-numbers", new TestReporter)
//        manager.addPrimitive("calc-coverage",  new CalculateCoverage)
//        manager.addPrimitive("is-prime", new spm.IsPrime)
//        manager.addPrimitive("pythagorean", new spm.Pythagorean)
        
        // spm.boids
        manager.addPrimitive("find-flockmates", new FindFlockmates)
        manager.addPrimitive("find-best-neighbor", new FindBestNeighbor)
        manager.addPrimitive("find-nearest-neighbor", new FindNearestNeighbor)
        
        // smp.uav_behavior.CheckBoundsUav
        manager.addPrimitive("uav-inside-bounds", new CheckTurtleInsideBounds)
        manager.addPrimitive("uav-inside-world-bounds", new CheckUavInsideWorldBounds)
        
        // smp.uav_behavior.ComputeHeading
        manager.addPrimitive("compute-heading-towards-point", new GetHeadingTowardsPoint.GetHeadingTowardsPointReporter)
        
        // smp.uav_behavior.TurnUav
        manager.addPrimitive("turn-uav", new TurnUav)
        
        // spm.search_algorithms.random_search
        manager.addPrimitive("update-random-search", new UpdateRandomSearch)
        manager.addPrimitive("update-random-search-single-uav", new UpdateRandomSearchIndividual)
    
        // spm.search_algorithms.symmetric_search
        manager.addPrimitive("setup-uav-subregions", new UAVRegionSetup)
        manager.addPrimitive("paint-subregions", new UavRandomSearchPaintSubregions)
        manager.addPrimitive("update-symmetric-search", new UavUpdateSymmetricSearch)
        manager.addPrimitive("update-symmetric-search-single-uav", new UavUpdateSymmetricSearchIndividual)
    
        
    } // load()
} // PlumeClassManager
