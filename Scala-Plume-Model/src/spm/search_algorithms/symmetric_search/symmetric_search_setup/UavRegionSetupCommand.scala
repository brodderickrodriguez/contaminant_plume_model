package spm.search_algorithms.symmetric_search.symmetric_search_setup

// Brodderick Rodriguez
// Auburn University - CSSE
// 22 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType
import org.nlogo.api.ScalaConversions._
import spm.helper.{ContextHelper, BreedHelper}

import spm.search_algorithms.symmetric_search.update_symmetric_search.UpdateSymmetricSearch


class UavRegionSetupCommand extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val regions = UavRegionSetup.buildRegions(args, context)
        val it = ContextHelper.getWorld(context).getBreed("UAVS").iterator
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val subregion = regions.head.toLogoList
            BreedHelper.setBreedVariable(uav, "UAV-region", subregion)
            UpdateSymmetricSearch.updateRegionSearchTime(context, uav)
            regions.remove(0)
        } // while
    } // perform()
    
} // AssignUAVSubregions()
