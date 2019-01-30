import org.nlogo.api._

class PlumeClassManager extends DefaultClassManager {
    def load(manager: PrimitiveManager) {
        manager.addPrimitive("report-numbers", new TestReporter)
    }
}