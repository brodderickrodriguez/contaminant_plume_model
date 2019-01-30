import org.nlogo.{ agent, api, core, nvm }
import core.Syntax._
import api.ScalaConversions._  // implicits
import org.nlogo.core.AgentKind

class ExtensionManager extends api.DefaultClassManager {
    def load(manager: api.PrimitiveManager) {
       // manager.addPrimitive("first-n-integers", IntegerList)
    }
}