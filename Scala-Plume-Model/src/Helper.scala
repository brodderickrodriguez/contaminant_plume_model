import org.nlogo.api._


object Helper {

    
    def getInput(args: Array[Argument], i: Int): Argument = {
        val a = try args(i)
        catch {
            case e: LogoException => throw new ExtensionException(e.getMessage)
        }
        a
    } // getInput()
    
    
} // Helper()
