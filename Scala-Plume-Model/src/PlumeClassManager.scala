import org.nlogo.api._

class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
        manager.addPrimitive("report-numbers", new TestReporter)
        manager.addPrimitive("calc-coverage",  new CalculateCoverage)
        
        
        // from Math.scala
        manager.addPrimitive("is-prime", new IsPrime)
        manager.addPrimitive("pythagorean", new Pythagorean)
    }
}