import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType}
import org.nlogo.{agent, api, core, nvm}
import core.Syntax._
import org.nlogo.api.ScalaConversions._
import org.nlogo.agent.AgentSetBuilder
import org.nlogo.core.AgentKind
import org.nlogo.window.GUIWorkspace



trait BOIDs


class FindFlockmates extends Command with BOIDs {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        
        val uav = Helper.ContextHelper.getAgent(context)
        
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", "hope")
        
        val c = Helper.BreedHelper.getTurtleVariable(uav, "color").asInstanceOf[Double]
        
        Helper.BreedHelper.setTurtleVariable(uav, "color", c / 2)
        
    }

} // FindFlockmates
