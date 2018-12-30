; Plume.nlogo
;
; Author: Brodderick Rodriguez, Xueqian Li
; Created on: Sep 05 2018
; Modified on: Sep 18 2018
;
; Simulation of a contaminant plume and UAV behaviors which
; cause the UAVs to fly around the center of the plume and map it
; Extending it by implementing searching algorithms outlined in:
; Multi-Agent Control Algorithms for Chemical Cloud Detection and Mapping Using Unmanned Air Vehicles, by
; Michael Kovacinal, Daniel Palmer, Guang Yang,Ravi Vaidyanathan
;
; Acknowledgements
; Copyright 1998 Uri Wilensky.
; See Info tab for full copyright and license.
; Approximately 40% of this program is barrowed from Uri Wilensky's Netlogo flocking progam
; The Netlogo flocking program is based on Reynold's 1987 BOIDS program
; Alex Madey March 2013


globals[
  coverageA ;;all the measurements over the last "coverage-data-decay" ticks
  coverage ;;standard deviation of coverageA
  TIME
  first-detection
  last-detection
  UAVs-detected-plume ; number of UAVs that have detected the plume so far
  total-coverage
]

breed [ regions region ]

regions-own [
  xcor-min
  xcor-max
  ycor-min
  ycor-max
]

turtles-own [
  flockmates ;;other UAVs in vision range
  nearest-neighbor
  best-neighbor ;;neighbor w/ highest reading
  contaminant-reading ;;most recent reading

  detected-plume

  random-time-for-heading
  random-heading

  my-region
  set-my-region
  my-region-xmin
  my-region-xmax
  my-region-ymin
  my-region-ymax
] ; turtles-own


patches-own [
  plume ;;plume concentration at a given patch
  plumeposx ;;plume position
  plumeposy
]


to setup
  clear-all
  crt population ;;spawn UAVs
    [ ;set color orange
      set size 7
      set shape "airplane"
      setxy max-pxcor max-pycor

      set my-region nobody
      set set-my-region false
  ]
  reset-ticks

  set coverageA [] ;;initializes as an array

  if search-algorithm = "symmetric" [  setup-symmetric-regions  ]

  set first-detection -1
  set last-detection -1
  set UAVs-detected-plume 0

  setup-plume

end


to setup-plume

 ; import-pcolors "SmuggleBackground.png" ;;background import

  ask patches [
    set plumeposx world-width / 2
    set plumeposy world-height / 2
    set plume 100 - (((distancexy plumeposx plumeposy) / (( plume-spread-radius / 2)))) ;;plume spread: based on distance from center
    if plume < 0 [set plume 0]
    let col red
    set pcolor pcolor + scale-color col plume 1 100
  ] ; ask patches

end




to go
  if search-algorithm = "default" [ default-search ]
  if search-algorithm = "random" [ random-search ]
  if search-algorithm = "symmetric" [ symmetric-search ]

  set TIME ticks

  ask turtles [ get-reading ]
  calc-coverage
  repeat 1 [ ask turtles [ fd 1 ] display ] ; UAV movement



  tick
end


to default-search
  ask turtles [ flock ]
end


; ---------------------------------------------------------------------------------
; random search algorithm
to random-search
  ; ask each UAV to do random search
  ask turtles [ random-search-UAV self ]
end


to random-search-UAV [current-UAV]
  ; if duration to go straight in is over
  if ticks > random-time-for-heading [
    set random-time-for-heading ticks + random random-search-max-time
    set random-heading random 360
  ] ; if ticks > random-time-for-heading

  ; ease the UAV into the turn
  if heading != random-heading [ turn-towards random-heading max-random-max-turn ]
end
; ---------------------------------------------------------------------------------



; ---------------------------------------------------------------------------------
to symmetric-search
  let ssbt symmetric-search-border-threshold
  ask turtles [

    ; if about to be outside of region (based on region bounds and symmetric-search-border-threshold)
    ifelse (xcor < my-region-xmin + ssbt) or xcor > (my-region-xmax - ssbt) or ycor < (my-region-ymin + ssbt) or ycor > (my-region-ymax - ssbt) [

      ; find self region center
      let region-xcenter (my-region-xmin + my-region-xmax) / 2
      let region-ycenter (my-region-ymin + my-region-ymax) / 2

      ; compute distances to center
      let x-difference xcor - region-xcenter
      let y-difference ycor - region-ycenter

      ; set new heading based on inverse tangent
      let new-heading (atan x-difference y-difference) - 180
      turn-towards new-heading symmetric-search-max-turn

    ] ; if outside of region
    [ pen-down random-search-UAV self ]
  ] ; ask turtles
end


to-report get-optimal-subregion-dimensions
  let UAVs count turtles
  let optimal [1 1 999999999999999]
  let h 1

  while [h <= (UAVs / 2)] [
    if UAVs mod h = 0 [
      let w UAVs / h
      let cost abs(w - h)
      if cost < item 2 optimal [ set optimal (list w h cost) ]
    ] ; if UAVs mod h = 0

    set h h + 1
  ] ; while [h < (UAVs / 2)]

  report optimal
end


to setup-symmetric-regions
  ; initialize local varibles
  let optimal-configuration get-optimal-subregion-dimensions

  show optimal-configuration

  let UAV-count count turtles
  let region-width world-width / (item 0 optimal-configuration)
  let region-height world-height / (item 1 optimal-configuration)

  ; initialize x and y to be top left corner
  let x 0
  let y 0

  ; loop and create individual regions
  while [y < world-height] [
    while [x < world-width ] [
      ; get a UAV who has no region set
      let current-UAV one-of turtles with [set-my-region = false]

      ; set patches color based on current-UAV color
      ask patches with [pxcor >= x and pxcor < x + region-width and pycor >= y and pycor < y + region-height] [

      ask current-UAV [
          ; set UAV region bounds
          set set-my-region true
          set my-region-xmin x
          set my-region-ymin y
          set my-region-xmax x + region-width
          set my-region-ymax y + region-height
          ;setxy ((my-region-xmin + my-region-xmax) / 2) ((my-region-ymin + my-region-ymax) / 2)
        ] ; ask current-UAV

        ; set patch color to current-UAV color
        set pcolor ([color] of current-UAV) - 2
      ] ; ask patches

      ; increment x
      set x (x + region-width)
    ] ; while x < world-width - region-width

    ; reset x
    set x 0

    ; increment y
    set y (y + region-height)
  ] ; while y < world-height
end
; ---------------------------------------------------------------------------------




to get-reading


  set coverageA lput plume coverageA ;;puts measurement on coverage array
  set contaminant-reading plume ;;stores most recent measurement as a turtle var

  if contaminant-reading > 0 and first-detection = -1 [ set first-detection ticks ]

  if contaminant-reading > 0 and detected-plume != True [
    set detected-plume True
    set UAVs-detected-plume UAVs-detected-plume  + 1

    if UAVs-detected-plume = population [ set last-detection ticks ]
  ]


end



to calc-coverage
  if ticks > coverage-data-decay
  [repeat population[ set coverageA butfirst coverageA ]] ;;recycles coverageA
  set coverage standard-deviation coverageA
  set total-coverage sum coverageA
end



to flock  ;;if a UAV has a higher "turtlei" than 0, cohere toward UAV with highest "turtlei"
  find-flockmates
  if any? flockmates [
    find-best-neighbor
    find-nearest-neighbor
    ifelse distance nearest-neighbor < minimum-separation [ separate ] [ align cohere ]
  ]
end



;;>>>the following is all original, unmodified BOIDS code<<<

to find-flockmates  ;; turtle procedure
  set flockmates other turtles in-radius vision
end



to find-best-neighbor ;; turtle procedure
  set best-neighbor max-one-of flockmates [ contaminant-reading ]
end



to find-nearest-neighbor ;; turtle procedure
  set nearest-neighbor min-one-of flockmates [ distance myself ]
end



;;; SEPARATE

to separate  ;; turtle procedure
  turn-away ([heading] of nearest-neighbor) max-separate-turn
end



;;; ALIGN

to align  ;; turtle procedure
  if contaminant-reading < [ contaminant-reading ] of best-neighbor [ turn-towards average-flockmate-heading max-align-turn ]
end



to-report average-flockmate-heading  ;; turtle procedure
  ;; We can't just average the heading variables here.
  ;; For example, the average of 1 and 359 should be 0,
  ;; not 180.  So we have to use trigonometry.
  let x-component sum [dx] of flockmates
  let y-component sum [dy] of flockmates
  ifelse x-component = 0 and y-component = 0 [ report heading ] [ report atan x-component y-component ]
end



;;; COHERE

to cohere  ;; turtle procedure]
  if contaminant-reading < [ contaminant-reading ] of best-neighbor [turn-towards average-heading-towards-flockmates max-cohere-turn ]
end



to-report average-heading-towards-flockmates  ;; turtle procedure
  ;; "towards myself" gives us the heading from the other turtle
  ;; to me, but we want the heading from me to the other turtle,
  ;; so we add 180
  let x-component [sin (towards myself + 180)] of best-neighbor
  let y-component [cos (towards myself + 180)] of best-neighbor
  ifelse x-component = 0 and y-component = 0
  [ report heading show heading ]
  [ report atan x-component y-component ]
end



;;; HELPER PROCEDURES

to turn-towards [new-heading max-turn]  ;; turtle procedure
  turn-at-most (subtract-headings new-heading heading) max-turn
end



to turn-away [new-heading max-turn]  ;; turtle procedure
  turn-at-most (subtract-headings heading new-heading) max-turn
end


;; turn right by "turn" degrees (or left if "turn" is negative),
;; but never turn more than "max-turn" degrees
to turn-at-most [turn max-turn]  ;; turtle procedure
  ifelse abs turn > max-turn
    [ ifelse turn > 0
        [ rt max-turn ]
        [ lt max-turn ] ]
    [ rt turn ]
end
@#$#@#$#@
GRAPHICS-WINDOW
498
12
1508
653
-1
-1
2.0
1
10
1
1
1
0
0
0
1
0
500
0
315
1
1
1
ticks
30.0

BUTTON
67
96
144
129
NIL
setup
NIL
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

BUTTON
149
95
230
128
NIL
go
T
1
T
OBSERVER
NIL
NIL
NIL
NIL
1

SLIDER
9
51
232
84
population
population
1.0
100
6.0
1.0
1
NIL
HORIZONTAL

SLIDER
4
217
237
250
max-align-turn
max-align-turn
0.0
20.0
1.0
0.25
1
degrees
HORIZONTAL

SLIDER
4
251
237
284
max-cohere-turn
max-cohere-turn
0.0
10
6.7
0.1
1
degrees
HORIZONTAL

SLIDER
4
285
237
318
max-separate-turn
max-separate-turn
0.0
20.0
10.0
0.25
1
degrees
HORIZONTAL

SLIDER
9
135
232
168
vision
vision
0.0
200
145.5
0.5
1
patches
HORIZONTAL

SLIDER
9
169
232
202
minimum-separation
minimum-separation
0.0
5.0
5.0
0.25
1
patches
HORIZONTAL

SLIDER
4
319
236
352
plume-spread-radius
plume-spread-radius
0
1
0.99
.01
1
NIL
HORIZONTAL

SLIDER
4
353
236
386
coverage-data-decay
coverage-data-decay
1
60
60.0
1
1
NIL
HORIZONTAL

PLOT
4
389
235
563
Coverage
Ticks
St. Deviation
0.0
1.0
0.0
0.3
true
false
"" ";;set-plot-y-range 0 .5\n;;set-plot-x-range ticks - 100 ticks + 1"
PENS
"default" 1.0 0 -16777216 true "" "plot coverage"

CHOOSER
254
54
392
99
search-algorithm
search-algorithm
"default" "random" "symmetric"
0

SLIDER
254
110
487
143
random-search-max-time
random-search-max-time
0
100
42.0
1
1
NIL
HORIZONTAL

SLIDER
252
153
487
186
max-random-max-turn
max-random-max-turn
0
20
4.0
0.5
1
NIL
HORIZONTAL

TEXTBOX
262
32
429
52
Search inputs
11
0.0
1

SLIDER
254
213
488
246
symmetric-search-max-turn
symmetric-search-max-turn
0
20
20.0
0.01
1
NIL
HORIZONTAL

SLIDER
253
253
487
286
symmetric-search-border-threshold
symmetric-search-border-threshold
0
25
4.5
0.1
1
NIL
HORIZONTAL

PLOT
6
572
479
906
plume map
NIL
NIL
0.0
500.0
0.0
315.0
false
false
"" "  ask turtles [\n    if contaminant-reading > 0 [\n      set-plot-pen-color color\n      plotxy xcor ycor\n    ]  \n  ]"
PENS
"pen-0" 1.0 0 -7500403 true "" ""

@#$#@#$#@
## WHAT IS IT?

The objective of our contaminant plume scenario was to simulate the mapping of a contaminant plume by a UAV swarm. In this scenario, the ground controller can control the maximum coherence turn of the UAVs. A higher maximum coherence turn results in a tighter grouping for the swarm, while a lower value results in a more dispersed swarm. Figure 4a demonstrates a sub-optimal maximum coherence value, while Figure 4b demonstrates an improved value. Figure 4c shows a graph of the standard deviation of the entire swarm's sensor measurements, representing how well distributed the measurements are. Ground controllers would be provided with an up-to-date version of the graph, giving them feedback and allowing them to find optimal parameters.


## HOW TO CITE

This model is describe in the paper:

A. G. Madey and G. R. Madey, "Design and Evaluation of UAV Swarm Command and Control Strategies," In Proceedings of the 2013 Symposium on Agent Directed Simulation (ADS '13/SpringSim 2013). Society for Computer Simulation International, San Diego, CA, 2013.

Approximately 40% of this program is barrowed from Uri Wilensky's Netlogo flocking progam. The Netlogo flocking program is based on Reynold's 1987 BOIDS program.

If you mention NetLogo in a publication, we ask that you include these citations for the model itself and for the NetLogo software:

* Wilensky, U. (1998).  NetLogo Flocking model.  http://ccl.northwestern.edu/netlogo/models/Flocking.  Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.
* Wilensky, U. (1999). NetLogo. http://ccl.northwestern.edu/netlogo/. Center for Connected Learning and Computer-Based Modeling, Northwestern University, Evanston, IL.

## COPYRIGHT AND LICENSE

New code in this program Copyright 2013 Alexander Madey

Copyright 1998 Uri Wilensky.

![CC BY-NC-SA 3.0](http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png)

This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License.  To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ or send a letter to Creative Commons, 559 Nathan Abbott Way, Stanford, California 94305, USA.

Commercial licenses are also available. To inquire about commercial licenses, please contact Uri Wilensky at uri@northwestern.edu.

This model was created as part of the project: CONNECTED MATHEMATICS: MAKING SENSE OF COMPLEX PHENOMENA THROUGH BUILDING OBJECT-BASED PARALLEL MODELS (OBPML).  The project gratefully acknowledges the support of the National Science Foundation (Applications of Advanced Technologies Program) -- grant numbers RED #9552950 and REC #9632612.

This model was converted to NetLogo as part of the projects: PARTICIPATORY SIMULATIONS: NETWORK-BASED DESIGN FOR SYSTEMS LEARNING IN CLASSROOMS and/or INTEGRATED SIMULATION AND MODELING ENVIRONMENT. The project gratefully acknowledges the support of the National Science Foundation (REPP & ROLE programs) -- grant numbers REC #9814682 and REC-0126227. Converted from StarLogoT to NetLogo, 2002.
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

arrow
true
0
Polygon -7500403 true true 150 0 0 150 105 150 105 293 195 293 195 150 300 150

box
false
0
Polygon -7500403 true true 150 285 285 225 285 75 150 135
Polygon -7500403 true true 150 135 15 75 150 15 285 75
Polygon -7500403 true true 15 75 15 225 150 285 150 135
Line -16777216 false 150 285 150 135
Line -16777216 false 150 135 15 75
Line -16777216 false 150 135 285 75

bug
true
0
Circle -7500403 true true 96 182 108
Circle -7500403 true true 110 127 80
Circle -7500403 true true 110 75 80
Line -7500403 true 150 100 80 30
Line -7500403 true 150 100 220 30

butterfly
true
0
Polygon -7500403 true true 150 165 209 199 225 225 225 255 195 270 165 255 150 240
Polygon -7500403 true true 150 165 89 198 75 225 75 255 105 270 135 255 150 240
Polygon -7500403 true true 139 148 100 105 55 90 25 90 10 105 10 135 25 180 40 195 85 194 139 163
Polygon -7500403 true true 162 150 200 105 245 90 275 90 290 105 290 135 275 180 260 195 215 195 162 165
Polygon -16777216 true false 150 255 135 225 120 150 135 120 150 105 165 120 180 150 165 225
Circle -16777216 true false 135 90 30
Line -16777216 false 150 105 195 60
Line -16777216 false 150 105 105 60

car
false
0
Polygon -7500403 true true 300 180 279 164 261 144 240 135 226 132 213 106 203 84 185 63 159 50 135 50 75 60 0 150 0 165 0 225 300 225 300 180
Circle -16777216 true false 180 180 90
Circle -16777216 true false 30 180 90
Polygon -16777216 true false 162 80 132 78 134 135 209 135 194 105 189 96 180 89
Circle -7500403 true true 47 195 58
Circle -7500403 true true 195 195 58

circle
false
0
Circle -7500403 true true 0 0 300

circle 2
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240

cow
false
0
Polygon -7500403 true true 200 193 197 249 179 249 177 196 166 187 140 189 93 191 78 179 72 211 49 209 48 181 37 149 25 120 25 89 45 72 103 84 179 75 198 76 252 64 272 81 293 103 285 121 255 121 242 118 224 167
Polygon -7500403 true true 73 210 86 251 62 249 48 208
Polygon -7500403 true true 25 114 16 195 9 204 23 213 25 200 39 123

cylinder
false
0
Circle -7500403 true true 0 0 300

dot
false
0
Circle -7500403 true true 90 90 120

face happy
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 255 90 239 62 213 47 191 67 179 90 203 109 218 150 225 192 218 210 203 227 181 251 194 236 217 212 240

face neutral
false
0
Circle -7500403 true true 8 7 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Rectangle -16777216 true false 60 195 240 225

face sad
false
0
Circle -7500403 true true 8 8 285
Circle -16777216 true false 60 75 60
Circle -16777216 true false 180 75 60
Polygon -16777216 true false 150 168 90 184 62 210 47 232 67 244 90 220 109 205 150 198 192 205 210 220 227 242 251 229 236 206 212 183

fish
false
0
Polygon -1 true false 44 131 21 87 15 86 0 120 15 150 0 180 13 214 20 212 45 166
Polygon -1 true false 135 195 119 235 95 218 76 210 46 204 60 165
Polygon -1 true false 75 45 83 77 71 103 86 114 166 78 135 60
Polygon -7500403 true true 30 136 151 77 226 81 280 119 292 146 292 160 287 170 270 195 195 210 151 212 30 166
Circle -16777216 true false 215 106 30

flag
false
0
Rectangle -7500403 true true 60 15 75 300
Polygon -7500403 true true 90 150 270 90 90 30
Line -7500403 true 75 135 90 135
Line -7500403 true 75 45 90 45

flower
false
0
Polygon -10899396 true false 135 120 165 165 180 210 180 240 150 300 165 300 195 240 195 195 165 135
Circle -7500403 true true 85 132 38
Circle -7500403 true true 130 147 38
Circle -7500403 true true 192 85 38
Circle -7500403 true true 85 40 38
Circle -7500403 true true 177 40 38
Circle -7500403 true true 177 132 38
Circle -7500403 true true 70 85 38
Circle -7500403 true true 130 25 38
Circle -7500403 true true 96 51 108
Circle -16777216 true false 113 68 74
Polygon -10899396 true false 189 233 219 188 249 173 279 188 234 218
Polygon -10899396 true false 180 255 150 210 105 210 75 240 135 240

house
false
0
Rectangle -7500403 true true 45 120 255 285
Rectangle -16777216 true false 120 210 180 285
Polygon -7500403 true true 15 120 150 15 285 120
Line -16777216 false 30 120 270 120

leaf
false
0
Polygon -7500403 true true 150 210 135 195 120 210 60 210 30 195 60 180 60 165 15 135 30 120 15 105 40 104 45 90 60 90 90 105 105 120 120 120 105 60 120 60 135 30 150 15 165 30 180 60 195 60 180 120 195 120 210 105 240 90 255 90 263 104 285 105 270 120 285 135 240 165 240 180 270 195 240 210 180 210 165 195
Polygon -7500403 true true 135 195 135 240 120 255 105 255 105 285 135 285 165 240 165 195

line
true
0
Line -7500403 true 150 0 150 300

line half
true
0
Line -7500403 true 150 0 150 150

pentagon
false
0
Polygon -7500403 true true 150 15 15 120 60 285 240 285 285 120

person
false
0
Circle -7500403 true true 110 5 80
Polygon -7500403 true true 105 90 120 195 90 285 105 300 135 300 150 225 165 300 195 300 210 285 180 195 195 90
Rectangle -7500403 true true 127 79 172 94
Polygon -7500403 true true 195 90 240 150 225 180 165 105
Polygon -7500403 true true 105 90 60 150 75 180 135 105

plant
false
0
Rectangle -7500403 true true 135 90 165 300
Polygon -7500403 true true 135 255 90 210 45 195 75 255 135 285
Polygon -7500403 true true 165 255 210 210 255 195 225 255 165 285
Polygon -7500403 true true 135 180 90 135 45 120 75 180 135 210
Polygon -7500403 true true 165 180 165 210 225 180 255 120 210 135
Polygon -7500403 true true 135 105 90 60 45 45 75 105 135 135
Polygon -7500403 true true 165 105 165 135 225 105 255 45 210 60
Polygon -7500403 true true 135 90 120 45 150 15 180 45 165 90

square
false
0
Rectangle -7500403 true true 30 30 270 270

square 2
false
0
Rectangle -7500403 true true 30 30 270 270
Rectangle -16777216 true false 60 60 240 240

star
false
0
Polygon -7500403 true true 151 1 185 108 298 108 207 175 242 282 151 216 59 282 94 175 3 108 116 108

target
false
0
Circle -7500403 true true 0 0 300
Circle -16777216 true false 30 30 240
Circle -7500403 true true 60 60 180
Circle -16777216 true false 90 90 120
Circle -7500403 true true 120 120 60

tree
false
0
Circle -7500403 true true 118 3 94
Rectangle -6459832 true false 120 195 180 300
Circle -7500403 true true 65 21 108
Circle -7500403 true true 116 41 127
Circle -7500403 true true 45 90 120
Circle -7500403 true true 104 74 152

triangle
false
0
Polygon -7500403 true true 150 30 15 255 285 255

triangle 2
false
0
Polygon -7500403 true true 150 30 15 255 285 255
Polygon -16777216 true false 151 99 225 223 75 224

truck
false
0
Rectangle -7500403 true true 4 45 195 187
Polygon -7500403 true true 296 193 296 150 259 134 244 104 208 104 207 194
Rectangle -1 true false 195 60 195 105
Polygon -16777216 true false 238 112 252 141 219 141 218 112
Circle -16777216 true false 234 174 42
Rectangle -7500403 true true 181 185 214 194
Circle -16777216 true false 144 174 42
Circle -16777216 true false 24 174 42
Circle -7500403 false true 24 174 42
Circle -7500403 false true 144 174 42
Circle -7500403 false true 234 174 42

turtle
true
0
Polygon -10899396 true false 215 204 240 233 246 254 228 266 215 252 193 210
Polygon -10899396 true false 195 90 225 75 245 75 260 89 269 108 261 124 240 105 225 105 210 105
Polygon -10899396 true false 105 90 75 75 55 75 40 89 31 108 39 124 60 105 75 105 90 105
Polygon -10899396 true false 132 85 134 64 107 51 108 17 150 2 192 18 192 52 169 65 172 87
Polygon -10899396 true false 85 204 60 233 54 254 72 266 85 252 107 210
Polygon -7500403 true true 119 75 179 75 209 101 224 135 220 225 175 261 128 261 81 224 74 135 88 99

wheel
false
0
Circle -7500403 true true 3 3 294
Circle -16777216 true false 30 30 240
Line -7500403 true 150 285 150 15
Line -7500403 true 15 150 285 150
Circle -7500403 true true 120 120 60
Line -7500403 true 216 40 79 269
Line -7500403 true 40 84 269 221
Line -7500403 true 40 216 269 79
Line -7500403 true 84 40 221 269

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270
@#$#@#$#@
NetLogo 6.0.4
@#$#@#$#@
set population 200
setup
repeat 200 [ go ]
@#$#@#$#@
@#$#@#$#@
<experiments>
  <experiment name="population experiement" repetitions="1" runMetricsEveryStep="true">
    <setup>setup</setup>
    <go>go</go>
    <timeLimit steps="1000"/>
    <metric>count turtles</metric>
    <metric>coverage</metric>
    <enumeratedValueSet variable="max-cohere-turn">
      <value value="4"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="random-search-max-time">
      <value value="42"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="max-separate-turn">
      <value value="10"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="search-algorithm">
      <value value="&quot;default&quot;"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="vision">
      <value value="84"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="max-random-turn">
      <value value="5.5"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="minimum-separation">
      <value value="0.5"/>
    </enumeratedValueSet>
    <steppedValueSet variable="population" first="2" step="1" last="100"/>
    <steppedValueSet variable="plume-spread" first="0.1" step="0.01" last="0.95"/>
    <enumeratedValueSet variable="coverage-data-decay">
      <value value="6"/>
    </enumeratedValueSet>
    <enumeratedValueSet variable="max-align-turn">
      <value value="1"/>
    </enumeratedValueSet>
  </experiment>
</experiments>
@#$#@#$#@
@#$#@#$#@
default
0.0
-0.2 0 0.0 1.0
0.0 1 1.0 0.0
0.2 0 0.0 1.0
link direction
true
0
Line -7500403 true 150 150 90 180
Line -7500403 true 150 150 210 180
@#$#@#$#@
0
@#$#@#$#@
