
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType, commandSyntax}
import spm.helper.Helper


class TestReporter extends Reporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    
    def report(args: Array[Argument], context: Context): AnyRef = {
//        val n = try args(0).getIntValue

        (0 until 89).toLogoList
    }
}


/*


        val uav = Helper.ContextHelper.getAgent(context)
        
        
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", "hope")
        
        val c = Helper.BreedHelper.getTurtleVariable(uav, "color").asInstanceOf[Double]
        
        Helper.BreedHelper.setTurtleVariable(uav, "color", c / 2)
        


 */



class TestCommander extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        var cov = Helper.ContextHelper.getObserverVariable(context, "coverage-all").asInstanceOf[Double]
        cov *= 10
        Helper.ContextHelper.setObserverVariable(context, "coverage-all", cov)
        val param = Helper.ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double]
        val np = param * 2
        Helper.ContextHelper.setObserverVariable(context, "population", np)

        val uav = Helper.ContextHelper.getAgent(context)
        var heading = Helper.TurtleHelper.getTurtleVariable(uav, "heading").asInstanceOf[Double]
        heading += 10
        Helper.TurtleHelper.setTurtleVariable(uav, "heading", heading)
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", List("test"))

    } // perform()
}



// calc coverage

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType}

import org.nlogo.{ agent, api, core, nvm }
import org.nlogo.api.Argument
import core.Syntax._
import api.ScalaConversions._  // implicits
import org.nlogo.core.AgentKind


class CalculateCoverage extends Reporter {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    
    
    override def report(args: Array[Argument], context: Context): AnyRef = {
        
        
        val x = context.world.allStoredValues.toArray
        
        
        context.world.setObserverVariableByName("population", 5.toLogoObject)
        
        val p = context.world.getPatch(100)
        
        
        //        p.setVariable(1, 15.toLogoObject)
        
        x.toLogoList
    }
    
    //    override def perform(args: Array[Argument], context: Context): Unit = {
    //        val world = context.getAgent.world.asInstanceOf[agent.World]
    //
    //        val ticks = world.ticks
    //
    //
    //        context.world.allStoredValues
    //
    //
    //
    //    } // perform()
    
}



object CalculateCoverage extends api.Command with nvm.CustomAssembled {
    override def getSyntax =
        commandSyntax(right = List(NumberType, CommandBlockType | OptionalType),
            agentClassString = "O---",
            blockAgentClassString = Some("-T--"))
    
    // only box this once
    private val red = Double.box(15)
    
    def perform(args: Array[api.Argument], context: api.Context) {
        
        println("testing")
        // the api package have what we need, so we'll often
        // be dropping down to the agent and nvm packages
        val n = args(0).getIntValue
        val world = context.getAgent.world.asInstanceOf[agent.World]
        val eContext = context.asInstanceOf[nvm.ExtensionContext]
        val nvmContext = eContext.nvmContext
        val agents =
            new agent.AgentSetBuilder(AgentKind.Turtle, n)
        for(_ <- 0 until n) {
            val turtle = world.createTurtle(world.turtles)
            turtle.colorDoubleUnchecked(red)
            agents.add(turtle)
            eContext.workspace.joinForeverButtons(turtle)
        }
        // if the optional command block wasn't supplied, then there's not
        // really any point in calling this, but it won't bomb, either
        nvmContext.runExclusiveJob(agents.build(), nvmContext.ip + 1)
        // prim._extern will take care of leaving nvm.Context ip in the right place
    }
    
    def assemble(a: nvm.AssemblerAssistant) {
        a.block()
        a.done()
    }
} // CalculateCoverage
