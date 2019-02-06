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


import scala.collection.mutable.ListBuffer


trait BOIDs


class FindFlockmates extends Command with BOIDs {
    
    override def getSyntax: Syntax = commandSyntax(right = List(NumberType))
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
    
        val uav = Helper.ContextHelper.getAgent(context)
        val vision = Helper.getInput(args, 0).getDoubleValue
        
        val turtles = context.world.turtles.asInstanceOf[org.nlogo.api.AgentSet]
        var uavs = ListBuffer[org.nlogo.agent.Agent]()
        var uavsInVision = ListBuffer[org.nlogo.agent.Agent]()
        
        for (turtle <- turtles) {
            val a = turtle.asInstanceOf[org.nlogo.agent.Agent]
            if (Helper.TurtleHelper.getTurtleVariable(a, "breed") == "UAVS")
                uavs.append(a)
        }
        
        
        for (uav <- uavs) {
        
        }
        
        
        
        
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", List(uavsInVision))
//        require(0>1, vision)
        

    } // perform()

} // FindFlockmates
