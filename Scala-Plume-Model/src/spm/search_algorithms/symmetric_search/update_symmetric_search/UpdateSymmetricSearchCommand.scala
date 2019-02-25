package spm.search_algorithms.symmetric_search.update_symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 25 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType

import spm.helper.ContextHelper


class UpdateSymmetricSearchCommand extends Command {
    
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val it = ContextHelper.getWorld(context).getBreed("UAVS").iterator
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            UpdateSymmetricSearch.updateSingleUav(context, uav)
        } // while
    
        UpdateSymmetricSearch.moveRegionsAccordingToWeather(context)
    } // perform()
    
} // UavUpdateSymmetricSearch
