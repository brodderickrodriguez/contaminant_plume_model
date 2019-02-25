package spm.boids.find_flockmates

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.commandSyntax
import spm.helper.{BreedHelper, ContextHelper}

class FindFlockmatesCommand extends Command {
    
    override def getSyntax: Syntax = commandSyntax(right = List())
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = ContextHelper.getTurtle(context)
        val flockmates = FindFlockmates.perform(context, uav)
        BreedHelper.setBreedVariable(uav, "flockmates", flockmates.build())
    } // perform()
    
} // FindFlockmates
