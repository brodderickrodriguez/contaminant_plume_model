package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 18 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.{LogoList, Syntax}
import org.nlogo.core.Syntax.ListType
import spm.helper.Helper


class UavRandomSearchPaintSubregions extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uavs = Helper.ContextHelper.getWorld(context).getBreed("UAVS").iterator
    
        while (uavs.hasNext) {
            val uav = uavs.next().asInstanceOf[org.nlogo.agent.Turtle]
            val uavColor = uav.color().asInstanceOf[Double] + 4
            val region = Helper.BreedHelper.getBreedVariable(uav, "uav-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
            val (rx1, ry1, rx2, ry2) = (region.head, region(1), region(2), region(3))
            val patches = Helper.ContextHelper.getWorld(context).patches.iterator
            
            while (patches.hasNext) {
                val patch = patches.next().asInstanceOf[org.nlogo.agent.Patch]
                val (pxcor, pycor) = (patch.pxcor, patch.pycor)
                
                if (pxcor >= rx1 && pxcor <= rx2 && pycor >= ry1 && pycor <= ry2)
                    patch.pcolor(uavColor)
            } // while patches
        } // while uavs
    } // perform()
} // UavRandomSearchPaintSubregions
