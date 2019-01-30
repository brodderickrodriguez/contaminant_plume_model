//package main.scala
//
//import org.nlogo.{agent, api, core, nvm}
//import core.Syntax._
//import api.ScalaConversions._
//import org.nlogo.api.{Argument, Context, ExtensionException}
//import org.nlogo.core.{AgentKind, Syntax}
//
//class ExtensionManager extends api.DefaultClassManager {
//    def load(manager: api.PrimitiveManager) {
//        manager.addPrimitive("gen-double", RandomDoubleGenerator)
//    } // load()
//} // ExtensionManager
//
//
//
//object RandomDoubleGenerator extends api.Reporter {
//
//    override def getSyntax: Syntax = {
//        reporterSyntax(right = List(NumberType), ret = ListType)
//    } // getSyntax()
//
//
//    override def report(args: Array[Argument], context: Context): AnyRef = {
//
//        val n = try args(0).getDoubleValue
//
//
//        catch {
//            case e: api.LogoException =>
//                throw new ExtensionException(e.getMessage)
//        }
//
//        (0 until 5).toLogoList
//
//
//
//    } // report()
//
//
//
//} // RandomDoubleGenerator