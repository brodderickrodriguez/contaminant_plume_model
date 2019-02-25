package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.commandSyntax

import spm.helper.ContextHelper


class UpdateRandomSearchCommand extends Command {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val world = ContextHelper.getWorld(context)
        val maxTurn = ContextHelper.getObserverVariable(context, "random-search-max-turn").asInstanceOf[Double]
        val iter = world.getBreed("UAVS").iterator
        
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            UpdateRandomSearch.behave(context, uav)
            spm.uav_behavior.TurnUav.go(uav, context, maxTurn)
        } // while
    } // perform()
    
} // UpdateRandomSearch
