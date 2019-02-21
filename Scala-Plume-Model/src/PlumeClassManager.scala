// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._

import spm._
import boids._
import uav_behavior._
import search_algorithms.random_search._
import search_algorithms.symmetric_search._


class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
        
        // spm.boids
        manager.addPrimitive("find-flockmates", new FindFlockmates)
        manager.addPrimitive("find-best-neighbor", new FindBestNeighbor)
        manager.addPrimitive("find-nearest-neighbor", new FindNearestNeighbor)
        manager.addPrimitive("turn-at-most", new TurnAtMostReporter)
        manager.addPrimitive("turn-towards", new TurnTowardsReporter)
        
        // smp.uav_behavior
        manager.addPrimitive("uav-inside-bounds", new CheckTurtleInsideBounds)
        manager.addPrimitive("uav-inside-world-bounds", new CheckUavInsideWorldBounds)
        
        // smp.uav_behavior.ComputeHeading
        manager.addPrimitive("compute-heading-towards-point", new GetHeadingTowardsPointReporter)
        
        // smp.uav_behavior.TurnUav
        manager.addPrimitive("turn-uav", new uav_behavior.TurnUavReporter)
        
        // spm.search_algorithms.random_search
        manager.addPrimitive("update-random-search", new UpdateRandomSearch)
        manager.addPrimitive("update-random-search-single-uav", new UpdateRandomSearchSingleUAV)
    
        // spm.search_algorithms.symmetric_search
        manager.addPrimitive("setup-uav-subregions", new UAVRegionSetup)
        manager.addPrimitive("paint-subregions", new UavSymmetricSearchPaintSubregions)
        manager.addPrimitive("update-symmetric-search", new UavUpdateSymmetricSearch)
        manager.addPrimitive("update-symmetric-search-single-uav", new UavUpdateSymmetricSearchIndividual)
        
    } // load()
} // PlumeClassManager
