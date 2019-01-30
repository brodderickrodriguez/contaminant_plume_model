import org.nlogo.core.Syntax.{ListType, NumberType, reporterSyntax}
import org.nlogo.{agent, api, core, nvm}
import api.ScalaConversions._
import org.nlogo.core.Syntax  // implicits


class PlumeModelExtension extends api.DefaultClassManager {
    def load(manager: api.PrimitiveManager) {
        manager.addPrimitive("first-n-integers", IntegerList)
    }
}

object IntegerList extends api.Reporter {
    override def getSyntax: Syntax =
        reporterSyntax(right = List(NumberType), ret = ListType)
    
    def report(args: Array[api.Argument], context: api.Context): AnyRef = {
        val n = try args(0).getIntValue
        catch {
            case e: api.LogoException =>
                throw new api.ExtensionException(e.getMessage)
        }
        if (n < 0)
            throw new api.ExtensionException("input must be positive")
        (0 until 100).toLogoList
    }
}
