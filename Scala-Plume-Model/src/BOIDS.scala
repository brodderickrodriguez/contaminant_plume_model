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
        
        var cov = Helper.ContextHelper.getObserverVariable(context, "coverage-all").asInstanceOf[Double]
        
        cov *= 10
        
        Helper.ContextHelper.setObserverVariable(context, "coverage-all", cov)
        
        
        val param = Helper.ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double]
        
        require(0>1, param)
        

        
        
        val uav = Helper.ContextHelper.getAgent(context)
        var heading = Helper.TurtleHelper.getTurtleVariable(uav, "heading").asInstanceOf[Double]
        
        heading += 10
        
        Helper.TurtleHelper.setTurtleVariable(uav, "heading", heading)
        
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", List("test"))

        
        
        
        
//        require(0>1, "tst popup")
    

    } // perform()

} // FindFlockmates
