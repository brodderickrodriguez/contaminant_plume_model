// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api._
import org.nlogo.api.ScalaConversions._


object Helper {
    
    def getInput(args: Array[Argument], i: Int): Argument = {
        val a = try args(i)
        catch { case e: LogoException => throw new ExtensionException(e.getMessage) }
        a
    } // getInput()
    
    
    object ContextHelper {
        def getAgent(context: Context): org.nlogo.agent.Agent = context.getAgent.asInstanceOf[org.nlogo.agent.Agent]
    } // ContextHelper
    
    
    trait _TurtleHelper {
        def setTurtleVariable(a: org.nlogo.agent.Agent, variableName: String, newValue: Any): Unit =
            a.setTurtleOrLinkVariable(variableName.toUpperCase, newValue.toLogoObject)
        
        def getTurtleVariable(a: org.nlogo.agent.Agent, variableName: String): AnyRef =
            a.getTurtleOrLinkVariable(variableName.toUpperCase)
    } // _TurtleHelper
    
    
    object TurtleHelper extends _TurtleHelper { }
    
    
    object BreedHelper extends _TurtleHelper {
        def setBreedVariable(a: org.nlogo.agent.Agent, variableName: String, newValue: Any): Unit =
            a.setBreedVariable(variableName.toUpperCase, newValue.toLogoObject)
    
        def getBreedVariable(a: org.nlogo.agent.Agent, variableName: String): AnyRef =
            a.getBreedVariable(variableName.toUpperCase)
    } // BreedHelper
    
} // Helper()
