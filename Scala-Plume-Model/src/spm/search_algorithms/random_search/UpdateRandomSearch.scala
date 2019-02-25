package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import scala.util.Random
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._

import spm.helper.{MathHelper, ContextHelper, BreedHelper}


object UpdateRandomSearch {
    
    def behave(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val randomSearchTime = BreedHelper.getBreedVariable(uav, "random-search-time").asInstanceOf[Double]
        val ticks = ContextHelper.getTicks(context)
        
        if (ticks > randomSearchTime) {
            val randomSearchMaxHeadingTime = ContextHelper.getObserverVariable(context, "random-search-max-heading-time").asInstanceOf[Double]
            val newSearchTime = Random.nextInt(randomSearchMaxHeadingTime.toInt) + ticks
            val newHeading = Random.nextInt(360) * MathHelper.random1()
            
            BreedHelper.setBreedVariable(uav, "random-search-time", newSearchTime.toLogoObject)
            BreedHelper.setBreedVariable(uav, "desired-heading", newHeading.toLogoObject)
        } // if
    } // behave()
    
} // UavRandomBehavior()
