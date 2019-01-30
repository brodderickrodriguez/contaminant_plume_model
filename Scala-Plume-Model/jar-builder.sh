#!/usr/bin/env bash

N=plume-scala

clear
echo "starting jar-builder..."
echo "killing NetLogo (unsafe)..."
# 'killall' is essentially force quit
killall NetLogo 6.0.4

echo "removing old compiled files..."
rm -r -f target
rm -f nlogo-model/$N/$N.jar
rm -f *.jar

echo "creating .jar..."
sbt clean compile package

echo "moving .jar to $N extension directory..."
mv $N.jar nlogo-model/$N

echo "launching NetLogo..."
open -a /Applications/NetLogo\ 6.0.4/NetLogo\ 6.0.4.app ./nlogo-model/extended-plume.nlogo

echo "build-jar terminating"
