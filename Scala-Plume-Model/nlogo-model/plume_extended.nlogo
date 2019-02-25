; extended_plume.nlogo
; Author: Brodderick Rodriguez
; Created: Dec 18 2018
;
; Simulation of a contaminant plume and UAV behaviors which
; cause the UAVs to map the contaminant plume
; Extended by implementing searching algorithms outlined in:
; Multi-Agent Control Algorithms for Chemical Cloud Detection and Mapping Using Unmanned Air Vehicles,
; by Michael Kovacinal, Daniel Palmer, Guang Yang, Ravi Vaidyanathan
; with additional environmental modifications
;
; Acknowledgements
; Copyright 1998 Uri Wilensky.
; See Info tab for full copyright and license.
; Approximately X% of this program is barrowed from Uri Wilensky's Netlogo flocking progam
; The Netlogo flocking program is based on Reynold's 1987 BOIDS program
; Alex Madey March 2013

extensions [ plume-scala ]

globals [ search-strategy-flock search-strategy-random search-strategy-symmetric
          coverage-all coverage-std coverage-mean coverage-per-plume-density ]

breed [ contaminant-plumes contaminant-plume ]
breed [ UAVs UAV ]
breed [ swarms swarm ]

patches-own [ plume-density ]

contaminant-plumes-own [ plume-spead-radius plume-spread-patches ]

swarms-own [ ]

UAVs-own [ flockmates nearest-neighbor best-neighbor plume-reading my-swarm detection-time
           random-search-time UAV-region desired-heading
           symmetric-search-max-reading-region symmetric-search-region-time ]

to setup
  clear-all
  reset-ticks
;  import-drawing "./resources/plume-bg.png"
  set search-strategy-flock "search-strategy-flock"
  set search-strategy-random "search-strategy-random"
  set search-strategy-symmetric "search-strategy-symmetric"
  set coverage-all []

  setup-contaminant-plumes
  setup-UAVs
  setup-swarms

  if global-search-strategy = search-strategy-symmetric [
    plume-scala:setup-uav-subregions
    plume-scala:paint-subregions
  ]
end

to go
  update-contaminant-plumes
  update-UAVs
  update-swarms
  calc-coverage
  tick
end

to calc-coverage
  ask UAVs [ set plume-reading plume-density ]
  plume-scala:compute-coverage-metrics
end

to setup-contaminant-plumes
  create-contaminant-plumes number-plumes
  plume-scala:setup-contaminant-plumes
  update-contaminant-plumes
end

to-report pythagorean [ a b ]
  report sqrt (a ^ 2 + b ^ 2)
end

to update-contaminant-plumes
  ask contaminant-plumes [
    ; reset previous patch plume-density to 0 before moving
    ask patches in-radius plume-spread-patches [
      set plume-density 0
     ; set pcolor black
    ]
    set plume-spread-patches plume-spread-patches * (1 - plume-decay-rate)
    set size plume-spread-patches * 2
    set heading wind-heading
    fd wind-speed

    set-plume-patch-density
  ]
end

to set-plume-patch-density
  let this-plume self
  ask patches in-radius plume-spread-patches [
    let d distancexy [ xcor ] of myself [ ycor ] of myself
    set plume-density plume-density + 1 - (d / [ plume-spread-patches ] of myself)

  ]
end

to setup-UAVs
  create-UAVs population [
    set size 3
    set shape "airplane"
    set detection-time 0
    setxy random-xcor random-ycor
  ]
end

to update-UAVs
  if global-search-strategy = search-strategy-random [ plume-scala:update-random-search ]
  if global-search-strategy = search-strategy-symmetric [ plume-scala:update-symmetric-search ]

  ask UAVs [
    if global-search-strategy = search-strategy-flock [ update-search-strategy-flock ]
    fd 0.5
  ]
end

to setup-swarms
  create-swarms 1 [ hide-turtle ]
end

to update-swarms
end

to update-search-strategy-flock
  find-flockmates
  if any? flockmates [
    find-best-neighbor
    find-nearest-neighbor
    ifelse distance nearest-neighbor < minimum-separation [ separate ] [ align cohere ]
  ]
end

to find-flockmates
  set flockmates other UAVs in-radius UAV-vision
end

to find-best-neighbor
  set best-neighbor max-one-of flockmates [ plume-reading ]
end

to find-nearest-neighbor
  set nearest-neighbor min-one-of flockmates [ distance myself ]
end

to separate
  turn-away ([ heading ] of nearest-neighbor) max-separate-turn
end

to align
  if plume-reading < [ plume-reading ] of best-neighbor [ turn-towards average-flockmate-heading max-align-turn ]
end

to-report average-flockmate-heading
  ; We can't just average the heading variables here. For example, the average of 1 and 359
  ; should be 0, not 180.  So we have to use trigonometry.
  let x-component sum [ dx ] of flockmates
  let y-component sum [ dy ] of flockmates
  ifelse x-component = 0 and y-component = 0 [ report heading ] [ report atan x-component y-component ]
end

to cohere
  if plume-reading < [ plume-reading ] of best-neighbor [ turn-towards average-heading-towards-flockmates max-cohere-turn ]
end

to-report average-heading-towards-flockmates
  ; "towards myself" gives us the heading from the other turtle to me,
  ; but we want the heading from me to the other turtle, so we add 180
  let x-component [ sin (towards myself + 180) ] of best-neighbor
  let y-component [ cos (towards myself + 180) ] of best-neighbor
  ifelse x-component = 0 and y-component = 0 [ report heading ] [ report atan x-component y-component ]
end

to turn-towards [ new-heading max-turn ]
  turn-at-most (subtract-headings new-heading heading) max-turn
;  plume-scala:turn-towards new-heading max-turn
end

to turn-away [ new-heading max-turn ]
  turn-at-most (subtract-headings heading new-heading) max-turn
end

to turn-at-most [ turn max-turn ]
  plume-scala:turn-at-most turn max-turn
end
@#$#@#$#@
GRAPHICS-WINDOW
253
13
1241
527
-1
-1
5.0
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
195
0
100
1
1
1
ticks
30.0

BUTTON
35
20
125
53
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

SLIDER
17
231
241
264
plume-spread-radius
plume-spread-radius
0
1
0.25
0.01
1
percent
HORIZONTAL

SLIDER
19
89
244
122
population
population
0
100
100.0
1
1
UAVs per swarm
HORIZONTAL

BUTTON
139
20
224
53
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
17
191
241
224
number-plumes
number-plumes
0
5
5.0
1
1
NIL
HORIZONTAL

SLIDER
18
307
241
340
wind-speed
wind-speed
0
0.1
0.0
0.0001
1
NIL
HORIZONTAL

SLIDER
17
345
241
378
wind-heading
wind-heading
0
360
0.0
1
1
degrees
HORIZONTAL

PLOT
1570
91
1869
283
plume detection map
NIL
NIL
0.0
195.0
0.0
100.0
false
false
"" "  ask UAVs [\n    if plume-reading > 0 [\n      plotxy xcor ycor\n      plot-pen-down\n      set-plot-pen-color black\n      plotxy xcor ycor\n      plot-pen-up\n    ]  \n  ]"
PENS
"default" 1.0 0 -16777216 true "" ""

SLIDER
19
127
243
160
UAV-vision
UAV-vision
0
world-width
47.0
0.5
1
patches
HORIZONTAL

SLIDER
17
270
244
303
plume-decay-rate
plume-decay-rate
0
0.0001
1.0E-11
0.00000000001
1
p/t
HORIZONTAL

SLIDER
17
410
240
443
coverage-data-decay
coverage-data-decay
1
60
12.0
1
1
NIL
HORIZONTAL

PLOT
1254
297
1565
571
coverage-std
Ticks
standard deviation
0.0
0.3
0.0
0.3
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot coverage-std"

PLOT
1573
298
1873
569
coverage-mean
ticks
UAV coverage
0.0
1.0
0.0
0.3
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot coverage-mean"

SLIDER
506
565
745
598
random-search-max-heading-time
random-search-max-heading-time
0
100
38.0
1
1
NIL
HORIZONTAL

SLIDER
505
604
746
637
random-search-max-turn
random-search-max-turn
0
5
1.05
0.05
1
degrees
HORIZONTAL

CHOOSER
18
555
249
600
global-search-strategy
global-search-strategy
"search-strategy-flock" "search-strategy-random" "search-strategy-symmetric"
2

SLIDER
266
562
492
595
minimum-separation
minimum-separation
0
5
0.0
0.25
1
patches
HORIZONTAL

SLIDER
266
600
492
633
max-align-turn
max-align-turn
0
20
0.0
0.25
1
degrees
HORIZONTAL

SLIDER
268
641
492
674
max-cohere-turn
max-cohere-turn
0
10
9.0
0.1
1
degrees
HORIZONTAL

TEXTBOX
274
538
441
558
search-strategy-default
11
0.0
1

TEXTBOX
513
540
680
560
search-strategy-random
11
0.0
1

SLIDER
267
680
493
713
max-separate-turn
max-separate-turn
0
20
0.0
0.25
1
degrees
HORIZONTAL

SLIDER
17
448
240
481
world-edge-threshold
world-edge-threshold
0
25
4.0
0.5
1
NIL
HORIZONTAL

SLIDER
18
485
239
518
max-world-edge-turn
max-world-edge-turn
0
20
20.0
0.5
1
NIL
HORIZONTAL

TEXTBOX
20
68
170
86
UAVs & Swarms
11
0.0
1

TEXTBOX
20
171
170
189
Contaminant Plumes
11
0.0
1

TEXTBOX
18
393
168
411
Misc.
11
0.0
1

TEXTBOX
24
536
211
564
UAV Behavior & Search Strategy
11
0.0
1

SLIDER
761
565
1011
598
symmetric-search-max-turn
symmetric-search-max-turn
0
20
7.8
0.1
1
degrees
HORIZONTAL

SLIDER
761
604
1012
637
symmetric-search-region-threshold
symmetric-search-region-threshold
0
25
2.8
0.1
1
NIL
HORIZONTAL

TEXTBOX
764
541
914
559
search-strategy-symmetric
11
0.0
1

SLIDER
759
643
1011
676
symmetric-search-min-region-time
symmetric-search-min-region-time
1
1000
414.0
1
1
NIL
HORIZONTAL

SLIDER
758
680
1012
713
symmetric-search-max-region-time
symmetric-search-max-region-time
100
5000
1248.0
1
1
NIL
HORIZONTAL

PLOT
1252
15
1562
287
plot 1
NIL
NIL
0.0
0.0
0.0
0.3
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot coverage-per-plume-density"

@#$#@#$#@
## WHAT IS IT?

(a general understanding of what the model is trying to show or explain)

## HOW IT WORKS

(what rules the agents use to create the overall behavior of the model)

## HOW TO USE IT

(how to use the model, including a description of each of the items in the Interface tab)

## THINGS TO NOTICE

(suggested things for the user to notice while running the model)

## THINGS TO TRY

(suggested things for the user to try to do (move sliders, switches, etc.) with the model)

## EXTENDING THE MODEL

(suggested things to add or change in the Code tab to make the model more complicated, detailed, accurate, etc.)

## NETLOGO FEATURES

(interesting or unusual features of NetLogo that the model uses, particularly in the Code tab; or where workarounds were needed for missing features)

## RELATED MODELS

(models in the NetLogo Models Library and elsewhere which are of related interest)

## CREDITS AND REFERENCES

(a reference to the model's URL on the web if it has one, as well as any other necessary credits, citations, and links)
@#$#@#$#@
default
true
0
Polygon -7500403 true true 150 5 40 250 150 205 260 250

airplane
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 15 165 15 195 120 180 135 240 105 270 120 285 150 270 180 285 210 270 165 240 180 180 285 195 285 165 180 105 180 60 165 15

airplane 2
true
0
Polygon -7500403 true true 150 26 135 30 120 60 120 90 18 105 15 135 120 150 120 165 135 210 135 225 150 285 165 225 165 210 180 165 180 150 285 135 282 105 180 90 180 60 165 30
Line -7500403 false 120 30 180 30
Polygon -7500403 true true 105 255 120 240 180 240 195 255 180 270 120 270

airplane1
true
0
Polygon -7500403 true true 150 0 120 30 105 60 90 105 15 165 15 195 120 165 105 240 90 270 105 285 150 255 195 285 210 270 195 240 180 165 285 195 285 165 210 105 195 60 180 30

airplane2
true
0
Polygon -7500403 true true 150 0 135 15 120 60 120 105 60 210 75 240 120 210 135 240 105 270 120 285 150 270 180 285 195 270 165 240 180 210 225 240 240 210 180 105 180 60 165 15

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

sheep
false
15
Circle -1 true true 203 65 88
Circle -1 true true 70 65 162
Circle -1 true true 150 105 120
Polygon -7500403 true false 218 120 240 165 255 165 278 120
Circle -7500403 true false 214 72 67
Rectangle -1 true true 164 223 179 298
Polygon -1 true true 45 285 30 285 30 240 15 195 45 210
Circle -1 true true 3 83 150
Rectangle -1 true true 65 221 80 296
Polygon -1 true true 195 285 210 285 210 240 240 210 195 210
Polygon -7500403 true false 276 85 285 105 302 99 294 83
Polygon -7500403 true false 219 85 210 105 193 99 201 83

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

wolf
false
0
Polygon -16777216 true false 253 133 245 131 245 133
Polygon -7500403 true true 2 194 13 197 30 191 38 193 38 205 20 226 20 257 27 265 38 266 40 260 31 253 31 230 60 206 68 198 75 209 66 228 65 243 82 261 84 268 100 267 103 261 77 239 79 231 100 207 98 196 119 201 143 202 160 195 166 210 172 213 173 238 167 251 160 248 154 265 169 264 178 247 186 240 198 260 200 271 217 271 219 262 207 258 195 230 192 198 210 184 227 164 242 144 259 145 284 151 277 141 293 140 299 134 297 127 273 119 270 105
Polygon -7500403 true true -1 195 14 180 36 166 40 153 53 140 82 131 134 133 159 126 188 115 227 108 236 102 238 98 268 86 269 92 281 87 269 103 269 113

x
false
0
Polygon -7500403 true true 270 75 225 30 30 225 75 270
Polygon -7500403 true true 30 75 75 30 270 225 225 270
@#$#@#$#@
NetLogo 6.0.4
@#$#@#$#@
@#$#@#$#@
@#$#@#$#@
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
