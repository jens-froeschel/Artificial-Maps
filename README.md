# Artificial-Maps
The goal of this bachelorthesis is, to artificially generate realistic worldmaps. The results of this thesis could be used for further studies of noise algorithms or more likely for the generation of maps (for simulators or games).
The paper consists of four important parts:
1. The idea of noise functions
2. A bit of earth's climatology
3. Fundamentals of visiualising worldmaps
4. A brief explanation of the code

The thesis and the comments are kept in german (Keeping it german was easier for me, and I don't exspect anyone to ever use this program =/   Still programming it was a lot of fun and I learned some interesting technics =D  )

If you are interested in noise functions maybe the program can give you an idea of the "looks" of different noise functions. =)

Information:
The program calculates noise functions for every visible pixel inside the window. Therefor increasing the size of the window forces the program to reaload and a bigger windowsize needs more time to load.

Controls:
In the top left you can select different noise functions to generate a height-, temperature- and rainfall map. 
Beneath you can set the sea level (height of the water).
Beneath that you can select which map should be drawn (for more information on the koeppen maps you can search "köppen climate classification" and should find information on the topic).
Below the Koeppen Maps you can find the standard information (Height, Rainfall, Temperature) of the last clicked point on the map.
At the bottom left you can change the generation seed and create/calculate a new map.
At the top right you can switch between different month's of one simulated year (the temperature and rainfall change)
