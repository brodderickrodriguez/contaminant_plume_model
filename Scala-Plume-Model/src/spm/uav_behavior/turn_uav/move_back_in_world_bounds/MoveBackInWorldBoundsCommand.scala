package spm.uav_behavior.turn_uav.move_back_in_world_bounds

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType
import spm.helper.ContextHelper


class MoveBackInWorldBoundsCommand extends Command {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        MoveBackInWorldBounds.perform(uav, context)
    } // perform()
    
} // MoveBackInWorldBoundsCommand
