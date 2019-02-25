package spm.helper

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.{Argument, ExtensionException, LogoException}

object InputHelper {
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
        }
        a
    } // getInput()
    
} // InputHelper()
