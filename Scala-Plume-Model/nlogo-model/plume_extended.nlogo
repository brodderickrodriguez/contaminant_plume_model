; plume_extended.nlogo
; Author: Brodderick Rodriguez
; Created: Dec 18 2018
;
; Simulation of a contaminant plume and UAV behaviors which cause the UAVs to map the contaminant plume
; Extended by implementing searching algorithms outlined in:
; Multi-Agent Control Algorithms for Chemical Cloud Detection and Mapping Using Unmanned Air Vehicles,
; by Michael Kovacinal, Daniel Palmer, Guang Yang, Ravi Vaidyanathan
; with additional environmental modifications
;
; Acknowledgements
; Copyright 1998 Uri Wilensky.
; See Info tab for full copyright and license.
; Approximately 5% of this program is barrowed from Uri Wilensky's Netlogo flocking progam
; The Netlogo flocking program is based on Reynold's 1987 BOIDS program
; Alex Madey March 2013

; model inputs:
;   name:        Population
;   category:    Swarm
;   min value:   2
;   max value:   100
;   increment:   1
;   unit:        UAVs
;   description: The total number of UAVs in an episode and Swarm

;   name:        UAV-vision
;   category:    UAV
;   min value:   0
;   max value:   195
;   increment:   0.5
;   unit:        patches
;   description: The radius around the UAV in which it can detect other UAVs

;   name:        UAV-decontamination-strength
;   category:    UAV
;   min value:   0
;   max value:   0.01
;   increment:   1e-05
;   unit:        patches
;   description: The amount a single UAV can decontaminate (or decrease) the Contaminant Plume size in a single Tick

;   name:        number-plumes
;   category:    Plumes
;   min value:   0
;   max value:   5
;   increment:   1
;   unit:        Plumes
;   description: The number of Plumes in the current episode

;   name:        plume-spread-radius
;   category:    Plumes
;   min value:   0
;   max value:   1
;   increment:   0.01
;   unit:        Environment Width
;   description: A percentage of the Environment width which defines the radius of a Contaminant Plume

;   name:        plume-decay-rate
;   category:    Plumes
;   min value:   0
;   max value:   1E-03
;   increment:   1E-11
;   unit:        Patches / Tick
;   description: The amount that a Contaminant Plume naturally decreases over a single Tick

;   name:        plume-decontamination-threshold
;   category:    Plumes
;   min value:   0.01
;   max value:   1
;   increment:   0.01
;   unit:        plume-spread-radius
;   description: A percentage of a Contaminant Plume’s original size. When all Contaminant Plumes reach this size, the episode terminates
		
;   name:        wind-speed
;   category:    Environment
;   min value:   0
;   max value:   0.1
;   increment:   1E-03
;   unit:        Patches
;   description: The amount the Contaminant Plumes naturally shift due to wind in a single Tick

;   name:        wind-heading
;   category:    Environment
;   min value:   0
;   max value:   360
;   increment:   1
;   unit:        Degrees
;   description: The angle which a Contaminant Plume naturally shifts due to wind in a single Tick

;   name:        world-edge-threshold
;   category:    Environment
;   min value:   0
;   max value:   25
;   increment:   0.5
;   unit:        Patches
;   description: The minimum distance allowed between a UAV and the Environment edge before a UAV must turn

;   name:        max-world-edge-turn
;   category:    Environment
;   min value:   0
;   max value:   20
;   increment:   0.5
;   unit:        Degrees
;   description: The maximum angle a UAV can turn away from the Environment edge when it enters the world-edge-threshold

;   name:        coverage-data-decay
;   category:    Performance Metric
;   min value:   1
;   max value:   60
;   increment:   1
;   unit:        Ticks
;   description: A temporal threshold for calculating the standard deviation of the Swarm’s coverage

;   name:        global-search-strategy
;   category:    Swarm - Search
;   min value:   N/A
;   max value:   N/A
;   increment:   N/A
;   unit:        N/A
;   description: The global search strategy type deployed by the Swarm

;   name:        minimum-separation
;   category:    Swarm - Search - Flock
;   min value:   0
;   max value:   5
;   increment:   0.25
;   unit:        Patches
;   description: The minimum distance allowed between any two UAVs within the Swarm
	
;   name:        max-align-turn	
;   category:    Swarm - Search - Flock
;   min value:   0
;   max value:   20
;   increment:   0.25
;   unit:        borrowed from BOIDs flocking model and is how agents align their heading with their neighbors
;   description: Degrees

;   name:        max-cohere-turn
;   category:    Swarm - Search - Flock
;   min value:   0
;   max value:   10
;   increment:   0.1
;   unit:        Degrees
;   description: borrowed from BOIDs flocking model and is how agents steer towards a group of neighbors

;   name:        max-separate-turn
;   category:    Swarm - Search - Flock
;   min value:   0
;   max value:   20
;   increment:   0.25
;   unit:        Degrees
;   description: Degrees	borrowed from BOIDs flocking model and, on the contrary to alignment, is how agents steer away from their neighbors if they get too close

;   name:        random-search-max-heading-time
;   category:    Swarm - Search - Random
;   min value:   0
;   max value:   100
;   increment:   1
;   unit:        Ticks
;   description: The maximum amount of time a UAV continues on its current trajectory before turning

;   name:        random-search-max-turn
;   category:    Swarm - Search - Random
;   min value:   0
;   max value:   5
;   increment:   0.05
;   unit:        Degrees
;   description: The maximum angle a UAV can turn when the global search strategy is Random Search

;   name:        symmetric-search-max-turn
;   category:    Swarm - Search - Symmetric
;   min value:   0
;   max value:   20
;   increment:   0.1
;   unit:        Degrees
;   description: Degrees	The maximum angle a UAV can turn when the global search strategy is Symmetric Search

;   name:        symmetric-search-region-threshold
;   category:    Swarm - Search - Symmetric
;   min value:   -10
;   max value:   25
;   increment:   0.1
;   unit:        patches
;   description: The distance between a UAV and its region boarder where it is allowed to search

;   name:        symmetric-search-min-region-time
;   category:    Swarm - Search - Symmetric
;   min value:   1
;   max value:   1E+03
;   increment:   1
;   unit:        Ticks
;   description: The minimum amount of time a UAV must spend in its initially assigned region before moving to another region

;   name:        symmetric-search-max-region-time
;   category:    Swarm - Search - Symmetric
;   min value:   100
;   max value:   5E+05
;   increment:   1
;   unit:        Ticks
;   description: The maximum amount of time a UAV must spend in its initially assigned region before moving to another region


; import our custom Plume Model extension
extensions [ plume-scala ]

; globals:
;  - search-strategy-flock - A string value to indicate the current search strategy is flock
;  - search-strategy-random - A string value to indicate the current search strategy is random
;  - search-strategy-symmetric - A string value to indicate the current search strategy is symmetric
;
;   performance metrics:
;    - coverage-all - a list contaning all coverage readings from the UAVs over the coverage-data-decay period
;    - coverage-std - the standard deviation of coverage-all
;    - coverage-mean - the mean of coverage-all
;    - percentage-coverage - the number of patches that contain contaminant and have been visited by a UAV DIVIDED BY the number of patches that contain contaminant
globals [ search-strategy-flock search-strategy-random search-strategy-symmetric coverage-all coverage-std coverage-mean percentage-coverage ]

; set up the breed types we are using
breed [ contaminant-plumes contaminant-plume ]
breed [ UAVs UAV ]
breed [ swarms swarm ]

; define variables to the patches for the contaminant plume and measuring performance
;   plume-density - the density of the contaminant plume at this patch
;   visited - if the plume-density is > 0 and this patch has been visited by a UAV then visited = 1
patches-own [ plume-density visited ]

; define variables for the contaminant plumes
;   plume-spread-radius - the radius of the contaminant plume with respect to the world width
;   plume-spread-patches - the radius of the contaminant plume in terms of patches
contaminant-plumes-own [ plume-spead-radius plume-spread-patches ]

; define no varialbes for the swarm
swarms-own [ ]

; define variables for the UAVs
;   flockmates - the other UAVs in this UAVs vision
;   nearest-neighbor - the nearest other UAV to this UAV
;   best-neighbor - the flockmate of this UAV with the highest plume-reading
;   plume-reading - the density of a contaminant plume at the UAVs current location
;   my-swarm - the swarm which this UAV is a part of
;   detection-time - the time when this UAV first detected a contaminant plume
;   random-search-time - the number of thicks this UAV will continue on its current heading before turning when the search strategy is random
;   UAV-region - the region (defined as a list) which this UAV is resonsible for searching when the search strategy is symmetric
;   desired-heading - the heading this UAV is changing to over more than one tick. This allows a UAV to remember where to turn when a "*-max-turn" input limits the turning radius
;   symmetric-search-max-reading-region - the maximum contaminant plume sensor reading this UAV has seen in its current region
;   symmetric-search-region-time - the amount of time this UAV will stay in its current region before considering switching to another region
UAVs-own [ flockmates nearest-neighbor best-neighbor plume-reading my-swarm detection-time random-search-time
           UAV-region desired-heading symmetric-search-max-reading-region symmetric-search-region-time ]


; called at the begining of an episode
to setup
  ; reset the environment
  clear-all
  reset-ticks

  ; set a background image
  import-drawing "./resources/plume-bg.png"

  ; set up the contaminant plumes, UAVs, then Swarms
  setup-contaminant-plumes
  setup-UAVs
  setup-swarms

  ; initialize strings to indicate which search strategy we are using in this episode
  set search-strategy-flock "search-strategy-flock"
  set search-strategy-random "search-strategy-random"
  set search-strategy-symmetric "search-strategy-symmetric"

  ; initialize the coverage-all list
  set coverage-all []

  ; if the seach strategy is symmetric search, then set up the environment accordingly. See Scala implementation for details.
  if global-search-strategy = search-strategy-symmetric [
    plume-scala:setup-uav-subregions
    plume-scala:paint-subregions
  ]
end


; called at each tick "forever" is set to True, indicating it continues to execute until the termination condition is met
to go
  ; run procedure to update the contaminant plumes. see the procedure below for details
  update-contaminant-plumes

  ; call the procedure to update the UAVs. see the procedure below for details
  update-UAVs

  ; call the procedure to calculate calc-percentage, coverage-all, coverage-mean and coverage-std
  calc-coverage

  ; increments the timestep by 1
  tick

  ; run the procedure to check if the termination condition is met, if it is, then terminate the episode
  if check-termination-condition [ stop ]
end


; a procedure which returns true if the termination condition is met, false otherwise
to-report check-termination-condition
  ; if there are no contaminant plumes, then return false
  if number-plumes < 1 [ report false ]

  ; a variable to check if all contaminant plumes are smaller than the threashold set by input plume-decontamination-threshold
  let all-plumes-decontaminated true

  ; compute the original size of the contaminant plumes when the experiment began
  let original-plume-spread-patches plume-spread-radius * world-width / 2

  ; compute the size of the contaminant plume when the episode will terminate
  let plume-decontamination-threshold-patches original-plume-spread-patches * plume-decontamination-threshold

  ; for each contaminant plume, check if its size is less than the threshold
  ask contaminant-plumes [
    ; if it is not, then set all-plumes-decontaminated = false
    if plume-spread-patches > plume-decontamination-threshold-patches [ set all-plumes-decontaminated false ]
  ]

  ; return true if the termination condition is met, false otherwise
  report all-plumes-decontaminated
end


; a procedure to calcuate the performance metrics calc-percentage, coverage-all, coverage-mean and coverage-std
to calc-coverage
  ; update all the UAV's sensor reading value to the density of the contaminant plyme at its current location
  ask UAVs [ set plume-reading plume-density ]

  ; compute coverage-all, coverage-mean and coverage-std in the scala implementation
  plume-scala:compute-coverage-metrics

  ; initialize a variable to track how many patches have a contaminant plume density
  let total-patches-in-plume 0

  ; initialize a variable to track how many patches have a contaminant plume density and have been visited by a UAV
  let total-patches-in-plume-visited 0

  ; for each contaminant plume
  ask contaminant-plumes [

    ; get the patches which this contaminant plume are currently over
    let patches-in-this-plume patches in-radius plume-spread-patches

    ; sum the total-patches-in-plume with the patches-in-this-plume
    set total-patches-in-plume (count patches-in-this-plume + total-patches-in-plume)

    ; if the patch at the UAVs current location is a part of patches-in-this-plume then set visited = 1
    ask UAVs [ ask patch-here [ if member? self patches-in-this-plume [set visited 1] ] ]

    ; count the number of patches that this contaminant plume cover which have visitied set to 1
    let patches-in-this-plume-visited count patches-in-this-plume with [visited = 1]

    ; sum the total-patches-in-plume-visited and patches-in-this-plume-visited
    set total-patches-in-plume-visited (patches-in-this-plume-visited + total-patches-in-plume-visited)
  ]

  ; if the number of total patches with contaminant plume covering them is greater than 0
  ; this checks for division by zero in the case that there are no contaminant plumes
  ; or that the contaminant plumes have decreased to the point where they cover no patches
  if total-patches-in-plume > 0 [
    ; set the percent-coverage to the number of patches that contain contaminant and have been visited by a UAV
    ; DIVIDED BY the number of patches that contain contaminant
    set percentage-coverage (total-patches-in-plume-visited / total-patches-in-plume)
  ]

end


; a procedure to set up the initial state of the contaminant plumes
to setup-contaminant-plumes
  ; create "number-plumes" number of plumes
  create-contaminant-plumes number-plumes

  ; call the scala implementation to set up all the contaminant plumes
  plume-scala:setup-contaminant-plumes

  ; call the procedure to update the contaminant plumes
  update-contaminant-plumes
end


; a procedure to update the contaminant plumes
to update-contaminant-plumes
  ; for each contaminant plume
  ask contaminant-plumes [
    ; if its plume-spread in patches is greater than zero
    if plume-spread-patches > 0 [
      ; reset previous patch plume-density to 0 before moving
      ask patches in-radius plume-spread-patches [ set plume-density 0 ]

      ; update the plume-spread patches to decrease in size decided by the input parameter plume-decay-rate
      set plume-spread-patches plume-spread-patches * (1 - plume-decay-rate)

      ; set the size to 2 times its radius
      set size plume-spread-patches * 2

      ; set the contaminant plume heading to the heading of the wind
      set heading wind-heading

      ; move the contaminant plume forward by "wind-speed" number of patches
      fd wind-speed

      ; call the procedure to update plume patch density
      set-plume-patch-density

      ; set a temporary variable to track the plume-spread-patches
      ; in order to allow UAVs to decontaminate the plume
      let tmp-plume-spread-patches plume-spread-patches

      ; for each UAV
      ask UAVs [
        ; if the UAV is on the contaminant plume
        if distance myself <= ([plume-spread-patches] of myself) [
          ; then decrease the size of the contaminant plume by "UAV-decontamination-strength" patches
          set tmp-plume-spread-patches tmp-plume-spread-patches - UAV-decontamination-strength
        ]
      ]

      ; update the contaminant plume size
      set plume-spread-patches tmp-plume-spread-patches

    ]
  ]
end


; a procedure to linearly decrease the density of the contaminant plume the closer to the edges
to set-plume-patch-density
  ; set a local variable for the contaminant plume that called this procedure
  let this-plume self

  ; for each patch that this contaminant plume covers
  ask patches in-radius plume-spread-patches [
    ; compute the distance from the center of the contaminant plume to this patch
    let d distancexy [ xcor ] of myself [ ycor ] of myself

    ; set the patch's plume density to 1 - (distance / plume radius)
    set plume-density plume-density + 1 - (d / [ plume-spread-patches ] of myself)

  ]
end


; a procedure to set up the initial state of the UAVs
to setup-UAVs
  ; create "population" number of UAvs
  create-UAVs population [
    ; set their attributes
    set size 3
    set shape "airplane"
    set detection-time 0
    setxy random-xcor random-ycor
  ]
end


; a procedure to update the UAVs based on the global search strategy
to update-UAVs
  ; if the global search strategy is random, then call the scala implementation
  if global-search-strategy = search-strategy-random [ plume-scala:update-random-search ]

  ; if the global search strategy is symmetric, then call the scala implementation
  if global-search-strategy = search-strategy-symmetric [ plume-scala:update-symmetric-search ]

  ; for each UAV
  ask UAVs [
    ; if the global search strategy is random, then call the procedure to update flock
    if global-search-strategy = search-strategy-flock [ update-search-strategy-flock ]

    ; move each UAV forward
    fd 0.5
  ]
end


; a procedure to create the swarms
to setup-swarms
  ; create 1 swarm and hide it (dont show it on the GUI)
  create-swarms 1 [ hide-turtle ]
end


; a procedure to update the flock search strategy
to update-search-strategy-flock
  ; if the UAV is inside of the world bounds decided by the input parameter world-edge-threshold
  ; then flock
  ; otherwise, call the scala implementation to move the UAV back into the world
  ifelse plume-scala:uav-inside-world-bounds
  [ flock ]
  [ plume-scala:move-uav-inside-world-bounds ]
end


; a procedure to
to flock
  plume-scala:find-flockmates
  if any? flockmates [
    find-best-neighbor
    find-nearest-neighbor
    ifelse distance nearest-neighbor < minimum-separation [ separate ] [ align cohere ]
  ]
end


; a procedure to
to find-best-neighbor
  set best-neighbor max-one-of flockmates [ plume-reading ]
end


; a procedure to
to find-nearest-neighbor
  set nearest-neighbor min-one-of flockmates [ distance myself ]
end


; a procedure to
to separate
  turn-away ([ heading ] of nearest-neighbor) max-separate-turn
end


; a procedure to
to align
  if plume-reading < [ plume-reading ] of best-neighbor [ turn-towards average-flockmate-heading max-align-turn ]
end


; a procedure to
to-report average-flockmate-heading
  ; We can't just average the heading variables here. For example, the average of 1 and 359
  ; should be 0, not 180.  So we have to use trigonometry.
  let x-component sum [ dx ] of flockmates
  let y-component sum [ dy ] of flockmates
  ifelse x-component = 0 and y-component = 0 [ report heading ] [ report atan x-component y-component ]
end


; a procedure to
to cohere
  if plume-reading < [ plume-reading ] of best-neighbor [ turn-towards average-heading-towards-flockmates max-cohere-turn ]
end


; a procedure to
to-report average-heading-towards-flockmates
  ; "towards myself" gives us the heading from the other turtle to me,
  ; but we want the heading from me to the other turtle, so we add 180
  let x-component [ sin (towards myself + 180) ] of best-neighbor
  let y-component [ cos (towards myself + 180) ] of best-neighbor
  ifelse x-component = 0 and y-component = 0 [ report heading ] [ report atan x-component y-component ]
end


; a procedure to
to turn-towards [ new-heading max-turn ]
  turn-at-most (subtract-headings new-heading heading) max-turn
;  plume-scala:turn-towards new-heading max-turn
end


; a procedure to
to turn-away [ new-heading max-turn ]
  turn-at-most (subtract-headings heading new-heading) max-turn
end


; a procedure to
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
277
241
310
plume-spread-radius
plume-spread-radius
0
1
0.5
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
39.0
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
237
241
270
number-plumes
number-plumes
0
5
1.0
1
1
plumes
HORIZONTAL

SLIDER
17
403
240
436
wind-speed
wind-speed
0
0.1
0.0135
0.0001
1
patches
HORIZONTAL

SLIDER
16
441
240
474
wind-heading
wind-heading
0
360
270.0
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
84.5
0.5
1
patches
HORIZONTAL

SLIDER
17
316
244
349
plume-decay-rate
plume-decay-rate
0
0.0001
0.0
0.00000000001
1
p/t
HORIZONTAL

SLIDER
17
501
240
534
coverage-data-decay
coverage-data-decay
1
60
11.0
1
1
ticks
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
504
564
745
597
random-search-max-heading-time
random-search-max-heading-time
0
100
11.0
1
1
Ticks
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
0.5
0.05
1
degrees
HORIZONTAL

CHOOSER
18
646
249
691
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
0.0
0.1
1
degrees
HORIZONTAL

TEXTBOX
274
538
441
558
search-strategy-flock
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
0.5
0.25
1
degrees
HORIZONTAL

SLIDER
16
539
240
572
world-edge-threshold
world-edge-threshold
0
25
5.0
0.5
1
degrees
HORIZONTAL

SLIDER
17
576
238
609
max-world-edge-turn
max-world-edge-turn
0
20
8.5
0.5
1
degrees
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
217
170
235
Contaminant Plumes
11
0.0
1

TEXTBOX
18
484
168
502
Misc.
11
0.0
1

TEXTBOX
24
627
211
655
UAV Behavior & Search Strategy
11
0.0
1

SLIDER
761
565
1045
598
symmetric-search-max-turn
symmetric-search-max-turn
0
20
3.9
0.1
1
degrees
HORIZONTAL

SLIDER
761
604
1046
637
symmetric-search-region-threshold
symmetric-search-region-threshold
-10
25
-4.8
0.1
1
patches
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
761
643
1046
676
symmetric-search-min-region-time
symmetric-search-min-region-time
1
1000
28.0
1
1
Ticks
HORIZONTAL

SLIDER
761
679
1047
712
symmetric-search-max-region-time
symmetric-search-max-region-time
100
5000
554.0
1
1
Ticks
HORIZONTAL

PLOT
1252
15
1562
287
percentage-coverage
NIL
NIL
0.0
0.0
0.0
1.0
true
false
"" ""
PENS
"default" 1.0 0 -16777216 true "" "plot percentage-coverage"

SLIDER
19
170
244
203
UAV-decontamination-strength
UAV-decontamination-strength
0
0.01
5.2E-4
0.00001
1
patches
HORIZONTAL

SLIDER
19
359
242
392
plume-decontamination-threshold
plume-decontamination-threshold
0.01
1
0.1
0.01
1
NIL
HORIZONTAL

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
