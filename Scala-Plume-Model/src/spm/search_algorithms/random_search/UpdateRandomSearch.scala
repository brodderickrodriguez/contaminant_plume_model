package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 14 Feb. 2019

import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.helper.Helper


class UpdateRandomSearch extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val world = Helper.ContextHelper.getWorld(context)
        val iter = world.getBreed("UAVS").iterator
        
        while (iter.hasNext) {
            val uav = iter.next().asInstanceOf[org.nlogo.agent.Turtle]
            spm.search_algorithms.random_search.UavRandomBehavior.behave(context, uav)
        } // while
    } // perform()
} // UpdateRandomSearch
