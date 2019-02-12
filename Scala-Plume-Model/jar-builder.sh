#!/usr/bin/env bash

E_N=plume-scala   # extension name
E_D=nlogo-model   # extension directory
N_D=/Applications/NetLogo\ $N_V/NetLogo\ $N_V.app # NetLogo path
N_V=6.0.4         # NetLogo version

clear
echo "starting jar-builder..."
echo "killing NetLogo (unsafe)..."
killall NetLogo $N_V    # 'killall' is essentially force quit

echo "removing old compiled files..."
rm -r -f target
rm -f $E_D/$E_N/$E_N.jar
rm -f *.jar

echo "creating .jar..."
sbt clean compile package

echo "moving .jar to $E_N extension directory..."
mkdir $E_D/$E_N
mv $E_N.jar $E_D/$E_N

echo "launching NetLogo..."
# open -a $N_D $E_D/extended-plume.nlogo
# open -a /Applications/NetLogo\ $N_V/NetLogo\ $N_V.app ./$E_D/extended-plume.nlogo
open -a /Applications/NetLogo\ $N_V/NetLogo\ $N_V.app ./$E_D/tests_plume_scala.nlogo

echo "jar-builder done"
