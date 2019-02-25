package spm.uav_behavior.turn_uav.turn

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType}
import spm.helper.{ContextHelper, InputHelper}

class TurnUavCommand extends Command {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        val turnAllowed = InputHelper.getInput(args, 0).getDoubleValue
        
        TurnUav.perform(uav, context, turnAllowed)
    } // perform()
    
} // TurnUavCommand
