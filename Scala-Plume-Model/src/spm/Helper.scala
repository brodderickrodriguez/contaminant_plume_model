package spm

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api.ScalaConversions._
import org.nlogo.api._


/**
  * A singleton interface to set/set inputs from the nlogo file
  *
  * NOTE TO USERS:
  * When using the Helper object to get variables, you will need to be weary of the expected return type.
  * You must used the asInstanceOf[...].
  * However, when setting, you do not need to use <VAR>.toLogoObject since Helper will do the conversion for you.
  *
  */
object Helper {
    /**
      * Safely gets an input in an argument array
      * @param args the array argument found in the org.nlogo.api.{Command.perform(), Reporter.report()} functions
      * @param i the index of the argument we wish to retrieve
      * @return org.nlogo.api.Argument
      */
    def getInput(args: Array[Argument], i: Int): Argument = {
        val a = try args(i)
        catch {
            case e1: LogoException => throw new ExtensionException(e1.getMessage)
            case e2: IndexOutOfBoundsException => throw new ArrayIndexOutOfBoundsException(e2.getMessage)
        } // catch
        a
    } // getInput()
    
    
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
    
    /**
      * _TurtleHelper is inherited by both TurtleHelper and BreedHelper since both will want to have the
      * ability to set Turtle attributes
      */
    //TODO: make agent helper and turtle helper separate
    // TODO: add a distance function. need to convert to other agent type
    trait _TurtleHelper {
        /**
          * Used to set a Turtle variable. This cannot be used to set a Breed's variable.
          * @param a the org.nlogo.agent.Ag we want to access
          * @param variableName the name of the variable we wish to set
          * @param newValue the value we wish to assign
          */
        def setTurtleVariable(a: org.nlogo.agent.Agent, variableName: String, newValue: Any): Unit =
            a.setTurtleOrLinkVariable(variableName.toUpperCase, newValue.toLogoObject)
    
        /**
          * Used to get a Turtle variable. This cannot be used to get a Breed's variable.
          * @param a the org.nlogo.agent.Turtle we want to access
          * @param variableName the name of the variable we wish to retrieve
          * @return the value stored in the Turtle's variable
          */
        def getTurtleVariable(a: org.nlogo.agent.Agent, variableName: String): AnyRef =
            a.getTurtleOrLinkVariable(variableName.toUpperCase)
        
        
        def getTurtleCoors(a: org.nlogo.agent.Turtle): (Double, Double) = (a.xcor(), a.ycor())
    } // _TurtleHelper
    
    
    // TODO: revise--set up like this to get "class inheritance" out of objects.
    object TurtleHelper extends _TurtleHelper { }
    
    /**
      * BreedHelper has the ability to set both Turtle and Breed variables. Breed variables are those declared in
      * breeds-own [...] in a .nlogo file.
      */
    object BreedHelper extends _TurtleHelper {
        /**
          * Used to set a Breed's variable
          * @param a the org.nlogo.agent.Agent we want to access
          * @param variableName the name of the variable we wish to set
          * @param newValue the value we wish to assign
          */
        def setBreedVariable(a: org.nlogo.agent.Agent, variableName: String, newValue: Any): Unit =
            a.setBreedVariable(variableName.toUpperCase, newValue.toLogoObject)
    
        /**
          * Used to get a Breed's variable
          * @param a the org.nlogo.agent.Agent we want to access
          * @param variableName the name of the variable we wish to retrieve
          * @return the value stored in the Breed's variable
          */
        def getBreedVariable(a: org.nlogo.agent.Agent, variableName: String): AnyRef =
            a.getBreedVariable(variableName.toUpperCase)
    } // BreedHelper
    
} // Helper()
