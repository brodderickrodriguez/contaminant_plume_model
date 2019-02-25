package spm.helper

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api.Context


/**
  * ContextHelper should handle getting/setting context, observer, world variables/objects
  */
object ContextHelper {
    
    /**
      * Used to convert a org.nlogo.api.Agent to a org.nlogo.agent.Agent (which is more useful in the API)
      * @param context the org.nlogo.api.Context argument found in the
      *                org.nlogo.api.{Command.perform(), Reporter.report()} function
      * @return org.nlogo.agent.Agent
      */
    def getAgent(context: Context): org.nlogo.agent.Agent = context.getAgent.asInstanceOf[org.nlogo.agent.Agent]
    
    
    def getTurtle(context: Context): org.nlogo.agent.Turtle = getAgent(context).asInstanceOf[org.nlogo.agent.Turtle]
    
    
    def getGlobals(context: Context): Array[AnyRef] = context.world.observer.variables
    
    
    def getWorld(context: Context): org.nlogo.agent.World = context.world.asInstanceOf[org.nlogo.agent.World]
    
    
    def getTicks(context: Context): Double = getWorld(context).ticks
    
    
    /**
      * Can be used to get both global variables and GUI (model) parameters
      * @param context the org.nlogo.api.Context argument found in the
      *                org.nlogo.api.{Command.perform(), Reporter.report()} function
      * @param variableName the name of the variable we wish to retrieve
      * @return
      */
    def getObserverVariable(context: Context, variableName: String): AnyRef = {
        val i = context.world.observerOwnsIndexOf(variableName.toUpperCase)
        context.world.observer.getVariable(i)
    } // getObserverVariable()
    
    
    /**
      * Can be used to set both global variables and GUI (model) parameters
      * @param context the org.nlogo.api.Context argument found in the
      *                org.nlogo.api.{Command.perform(), Reporter.report()} function
      * @param variableName the name of the variable we wish to set
      * @param newValue the value we wish to assign
      */
    def setObserverVariable(context: Context, variableName: String, newValue: Any): Unit = {
        // both approaches below seem to work. Not sure which is better
        // val i = context.world.observerOwnsIndexOf(variableName.toUpperCase)
        // context.world.observer.setVariable(i, newValue.toLogoObject)
        context.world.setObserverVariableByName(variableName.toUpperCase, newValue.toLogoObject)
    } // setObserverVariable()
    
} // ContextHelper
