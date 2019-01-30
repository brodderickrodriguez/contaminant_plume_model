
N=plume-scala

rm -r -f target
rm -f model/$N/$N.jar

sbt compile
sbt package

A=target/scala-2.12/$N.jar
B=model/$N/

cp $A $B

echo "gen terminating"
