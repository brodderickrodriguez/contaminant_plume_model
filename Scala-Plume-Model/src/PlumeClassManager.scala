// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._
import spm.boids._
import spm.search_algorithms.symmetric_search.{OptimalSubregionDimensions, UAVRegionSetup}

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
        
        // spm.search_algorithms.symmetric_search
        manager.addPrimitive("get-optimal-subregion-dimensions", new OptimalSubregionDimensions)
        manager.addPrimitive("setup-uav-subregions", new UAVRegionSetup)
        
    } // load()
} // PlumeClassManager
