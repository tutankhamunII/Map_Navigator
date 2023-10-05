# Map_Navigator
Program that reads a map from a text file and save it as a graph to run shortest path algorithms between any two points on the map.

In this project I built a java program that reads geographical data of roads and intersections from a text file and generate a graphical map of these data and displays it. It can also 
calculate the shortest path betIen any two destinations on the map using Dijkstra's Algorithm. I attempted to represent the graph as adjancey matrix and list, but both of them resulted in 
its own problems. The adjancy matrix consumed a huge amount of memory specially with the bigger maps of Monroe and nys, which resulted in the program running out of memory. It was also very slow when I 
attemped to add new vertices to the graph. As a result, I tried to implement the adjancy list which eliminated the space problem, but excarbated the runtime problem becuase traversing the linkedlist was
also very slow. After researching these problems in the context of this project, I came up with a solution to use a hash-map of strings to linkedlists. The strings are the intersections IDs and each linkedlist
head node stores an intersection object and pointer to an edge. Each edge stores a road that the intersections object is part of it. All edges also stores pointers to next edge to from the linkedlist. I used
2D graphics to paint lines that represent roads in the map. I added all the roads objects to an Arraylist dynamically in the program to print them all using paintcomponent method. I also spent sometime trying 
different methods to rescale the map with respect to the windows size and figured out a very straightforward way the depends on the maximum and minimum longitudes and latitudes of the map.

I implemented Dijkstra Algorithm to calculate shortest path betIen two destinations using another hash map that maps intersections IDs to the intersections themselves, which allows for significantly faster
intersection lookup compared with an array representation. I also added all intersections to a priority queue as they are inserted into the graph per dijkstra sudocode in lectures. I them added a formpath method to find the desired 
path betIen two destinations using the intersection IDs and saves them in an array. The array is then traversed to print the path to console output.

Time Analysis:

Displaying maps: All roads are stored in arraylist that is traversed once to print them, so the time complexity for this operation shall be big-O(E) where E is the number of edges in the graph.
operations like finding minimum and maximum latitude and longitude have constant time with no respect to the input size.

Shortest Path Calculation: Operations like finding the smallest vertex (due to used priorityqueue), selecting appropriate linked list from the hashmap, and all other basic calculations have constant time
and therefore are omitted from the time complexity relation. The findcost() method that traveerses a linked list until it discovers that needed road would be the limiting factor in our implementation and shall have
worst case complexity of big-O(E) where E is number of nodes inside the linked list when the needed road is at the very last node. This method is called on every instersection of the graph so the total runtime of 
our Dijkstra implementation shall be big-O(V * E) where V is number of vertices and E is average number of roads per linked list. 

Displaying Shortest Path: The runtime for this operation is simple and is big-O(V) where V is the number of intersections that makes up the path (as I traverse an array to print the path)


Run instructions:

- Compile using javac *.java to compile all files.
- Execute using java project3 [filename] [-request] [request distinations if request is directions]
EX: java project3 ur.txt -show -directions HOYT MOREY. 
- you can use both -show and -directions of either. 
