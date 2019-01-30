
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType}

trait Math


class IsPrime extends Reporter with Math {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = NumberType)
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val n = Helper.getInput(args, 0).getIntValue
        for (i <- 2 until n / 2) if (n % i == 0) return false.toLogoObject
        true.toLogoObject
    } // report()
} // IsPrime


class Pythagorean extends Reporter with Math {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType, NumberType), ret = NumberType)
    
    def report(args: Array[Argument], context: Context): AnyRef = {
        val a = Helper.getInput(args, 0).getDoubleValue
        val b = Helper.getInput(args, 1).getDoubleValue
        Math.sqrt(a * a + b * b).toLogoObject
    } // report()
} // IsPrime
