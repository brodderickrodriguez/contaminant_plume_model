#!/usr/bin/env bash

clear
echo "starting jar-builder..."

echo "killing NetLogo (unsafe)..."

killall NetLogo 6.0.4

N=plume-scala
B=nlogo-model/$N

echo "removing old compiled files..."

rm -r -f target
rm -f nlogo-model/$N/$N.jar
rm -f *.jar

echo "creating .jar..."

sbt clean compile package

echo "moving .jar to $N extension directory..."

mv $N.jar $B


echo "launching NetLogo..."

open -a /Applications/NetLogo\ 6.0.4/NetLogo\ 6.0.4.app ./nlogo-model/extended-plume.nlogo

echo "build-jar terminating"
