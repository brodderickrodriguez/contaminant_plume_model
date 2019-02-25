package spm.helper

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.ScalaConversions._

/**
  * Ability to set Turtle attributes
  */
//TODO: make agent helper and turtle helper separate
// TODO: add a distance function. need to convert to other agent type
object TurtleHelper {
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
} // TurtleHelper
