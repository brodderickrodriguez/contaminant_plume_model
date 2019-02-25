package spm.helper

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.ScalaConversions._


/**
  * BreedHelper has the ability to set both Turtle and Breed variables. Breed variables are those declared in
  * breeds-own [...] in a .nlogo file.
  */
object BreedHelper {
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
