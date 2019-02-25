package spm.performance_metrics.compute_coverage

// Brodderick Rodriguez
// Auburn University - CSSE
// 21 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.commandSyntax


class ComputeCoverageCommand extends Command {
    
        override def getSyntax: Syntax = commandSyntax(right = List())
    
    
        override def perform(args: Array[Argument], context: Context): Unit = ComputeCoverage.compute(context)
    
} // ComputeCoverage
