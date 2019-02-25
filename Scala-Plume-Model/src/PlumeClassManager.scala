// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._

import spm._
import boids._
import uav_behavior._
import search_algorithms.random_search._
import search_algorithms.symmetric_search._
import performance_metrics._


class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
        
        // spm.performance_metrics
        manager.addPrimitive("compute-coverage-metrics", new ComputeCoverage)
        
        // spm.boids
        // manager.addPrimitive("find-flockmates", new FindFlockmates)
        manager.addPrimitive("find-best-neighbor", new FindBestNeighbor)
        manager.addPrimitive("find-nearest-neighbor", new FindNearestNeighbor)
        manager.addPrimitive("turn-at-most", new TurnAtMostReporter)
        manager.addPrimitive("turn-towards", new TurnTowardsReporter)
        
        // spm.uav_behavior
        manager.addPrimitive("update-uavs-sensor-reading", new UavsUpdateSensorReadingCommand)
        
        // spm.uav_behavior
        manager.addPrimitive("uav-inside-bounds", new CheckTurtleInsideBounds)
        manager.addPrimitive("uav-inside-world-bounds", new CheckUavInsideWorldBounds)
        
        // spm.uav_behavior.ComputeHeading
        manager.addPrimitive("compute-heading-towards-point", new GetHeadingTowardsPointReporter)
        
        // spm.uav_behavior.TurnUav
        manager.addPrimitive("turn-uav", new uav_behavior.TurnUavReporter)
        manager.addPrimitive("move-uav-inside-world-bounds", new MoveUavBackInsideWorldBoundsCommand)
        
        // spm.search_algorithms.random_search
        manager.addPrimitive("update-random-search", new UpdateRandomSearch)
    
        // spm.search_algorithms.symmetric_search
        manager.addPrimitive("setup-uav-subregions", new UAVRegionSetup)
        manager.addPrimitive("paint-subregions", new UavSymmetricSearchPaintSubregions)
        manager.addPrimitive("update-symmetric-search", new UavUpdateSymmetricSearch)
        
        // spm.environment_behavior
        manager.addPrimitive("setup-contaminant-plumes", new environment_behavior.SetupContaminantPlumes)
        
        
    } // load()
} // PlumeClassManager
