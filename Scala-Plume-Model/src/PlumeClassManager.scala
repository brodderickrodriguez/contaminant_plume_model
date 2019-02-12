import org.nlogo.api._

class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
        manager.addPrimitive("report-numbers", new TestReporter)
        manager.addPrimitive("calc-coverage",  new CalculateCoverage)
        
        
        manager.addPrimitive("is-prime", new spm.IsPrime)
        manager.addPrimitive("pythagorean", new spm.Pythagorean)
        
        
        // spm.boids
        manager.addPrimitive("find-flockmates", new spm.boids.FindFlockmates)
        manager.addPrimitive("find-best-neighbor", new spm.boids.FindBestNeighbor)
        manager.addPrimitive("find-nearest-neighbor", new spm.boids.FindNearestNeighbor)
        
    } // load()
} // PlumeClassManager
