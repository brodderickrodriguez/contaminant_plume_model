// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._

import spm._
import boids._
import find_flockmates._
import turn_at_most._
import uav_behavior._
import check_uav_bounds.check_uav_inside_world_bounds.CheckUavInsideWorldBoundsReporter
import compute_heading.GetHeadingTowardsPointReporter
import search_algorithms.random_search.UpdateRandomSearchCommand
import search_algorithms.symmetric_search._
import symmetric_search_setup._
import update_symmetric_search.UpdateSymmetricSearchCommand
import paint_subregions.PaintSubregionsCommand
import performance_metrics.compute_coverage.ComputeCoverageCommand
import turn_uav.move_back_in_world_bounds.MoveBackInWorldBoundsCommand
import turn_uav.turn.TurnUavCommand

class PlumeClassManager extends DefaultClassManager {
    
    def load(manager: PrimitiveManager): Unit = {
        
        // spm.performance_metrics
        manager.addPrimitive("compute-coverage-metrics", new ComputeCoverageCommand)
        
        // spm.boids
        manager.addPrimitive("find-flockmates", new FindFlockmatesCommand)
        manager.addPrimitive("turn-at-most", new TurnAtMostCommand)
        
        // spm.uav_behavior
        manager.addPrimitive("uav-inside-world-bounds", new CheckUavInsideWorldBoundsReporter)
        
        // spm.uav_behavior.ComputeHeading
        manager.addPrimitive("compute-heading-towards-point", new GetHeadingTowardsPointReporter)
        
        // spm.uav_behavior.TurnUav
        manager.addPrimitive("turn-uav", new TurnUavCommand)
        manager.addPrimitive("move-uav-inside-world-bounds", new MoveBackInWorldBoundsCommand)
        
        // spm.search_algorithms.random_search
        manager.addPrimitive("update-random-search", new UpdateRandomSearchCommand)
    
        // spm.search_algorithms.symmetric_search
        manager.addPrimitive("setup-uav-subregions", new UavRegionSetupCommand)
        manager.addPrimitive("paint-subregions", new PaintSubregionsCommand)
        manager.addPrimitive("update-symmetric-search", new UpdateSymmetricSearchCommand)
        
        // spm.environment_behavior
        manager.addPrimitive("setup-contaminant-plumes", new environment_behavior.SetupContaminantPlumes)
        
    } // load()
    
} // PlumeClassManager
