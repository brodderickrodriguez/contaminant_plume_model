#!/usr/bin/env bash

N=plume-scala   # extension name
N_V=6.0.4       # NetLogo version

clear
echo "starting jar-builder..."
echo "killing NetLogo (unsafe)..."
killall NetLogo $N_V    # 'killall' is essentially force quit

echo "removing old compiled files..."
rm -r -f target
rm -f nlogo-model/$N/$N.jar
rm -f *.jar

echo "creating .jar..."
sbt clean compile package

echo "moving .jar to $N extension directory..."
mkdir nlogo-model/$N
mv $N.jar nlogo-model/$N

echo "launching NetLogo..."
open -a /Applications/NetLogo\ $N_V/NetLogo\ $N_V.app ./nlogo-model/extended-plume.nlogo

echo "build-jar terminating"
