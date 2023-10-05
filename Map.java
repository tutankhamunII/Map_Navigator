//Developers:
//Hesham Elshafey
//Ahmed Nassar
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
public class Map {
	
	public HashMap<String, LinkedList> graph;
	public static int Intersections_Number;
	public static ArrayList<Road> roads;
	public static HashMap<String, Intersection> intersectionMap;
	public static PriorityQueue<Intersection> unknownIntersectionsHeap;
	public static Intersection [] dijkstraPath;
	public static double minLat, maxLat, minLong, maxLong;
	
	public Map(int numVertices) {
		graph = new HashMap<String, LinkedList>();
		Intersections_Number = numVertices;
		roads = new ArrayList<Road>();
		intersectionMap = new HashMap<String, Intersection>();
		Comparator<Intersection> comparator = new Comparator<Intersection>() {

        	@Override
        	public int compare(Intersection i1, Intersection i2) {
            	
            	if(i1.distance < i2.distance) {
            		return -1;
            	}
            	else {
            		return 1;
            	}
            }
		};
		unknownIntersectionsHeap = new PriorityQueue<Intersection>(numVertices, comparator);
		minLat = minLong = Integer.MAX_VALUE;
		maxLat = maxLong = Integer.MIN_VALUE;	
	}

	public int size() {
		return graph.size();
	}
	
	public static String formPath(String endID) {
		
		Intersection temp = intersectionMap.get(endID);
		String [] path = new String[intersectionMap.size()];
		int counter = 0;
		while(temp.path != null) {
			path[counter] = temp.IntersectionID;
			temp = temp.path;
			counter++;
		}
		path[counter] = temp.IntersectionID;
		int totalPath = 0;
		for(int i = 0; i < path.length; i++) {
			if(path[i] == null) {
				totalPath = i;
				break;
			}
		}
		dijkstraPath = new Intersection [totalPath];
		for(int i = 0; i < totalPath; i++) {
			dijkstraPath[i] = intersectionMap.get(path[i]);
		}
		String finalPath = "";
		for(int i = counter ; i > -1; i--) {
			finalPath = finalPath + path[i] + "\n";
		}
		return finalPath;
	}
	public static double dijkstraPathLength() {

		return dijkstraPath[0].distance * 0.000621371;
	}
	
	public static Intersection smallestUnknownVertex() {
		
		Intersection temp = unknownIntersectionsHeap.remove();
		return intersectionMap.get(temp.IntersectionID);
		
	}
	public void dijkstra(String intersectionID) {
		
		Intersection source = intersectionMap.get(intersectionID);
		unknownIntersectionsHeap.remove(source);
		source.distance = 0;
		unknownIntersectionsHeap.add(source);
		double currentcost;
		int numUnknownVertices = intersectionMap.size();
		while(numUnknownVertices > 0) {
	
			Intersection temp = smallestUnknownVertex();
			temp.known = true;
			numUnknownVertices--;
			LinkedList currentVertex = graph.get(temp.IntersectionID);
			Edge currentRoad = currentVertex.head.edge;
			Intersection currentIntersection;
			while(currentRoad != null) {
				if(currentRoad.road.intersect1.equals(temp.IntersectionID)) {
					currentIntersection = intersectionMap.get(currentRoad.road.intersect2);
				}
				else {
					currentIntersection = intersectionMap.get(currentRoad.road.intersect1);
				}
				if(currentIntersection.known == false) {
				
					currentcost = findKey(temp, currentIntersection);
					if(temp.distance + currentcost < currentIntersection.distance) {
						
						unknownIntersectionsHeap.remove(currentIntersection);
						currentIntersection.distance = temp.distance + currentcost;
						currentIntersection.path = temp;
						unknownIntersectionsHeap.add(currentIntersection);
					}
				}
				currentRoad = currentRoad.next;
			}
		}
	}
	
	public double findKey(Intersection int1, Intersection int2) {
		
		LinkedList temp = graph.get(int1.IntersectionID);
		return temp.pathlength(int2);
	}
	
	public void insertIntersection(Intersection i) {
		
		
		if(i.latitude < minLat) {
			minLat = i.latitude;
		}
		if(i.latitude > maxLat) {
			maxLat = i.latitude;
		}
		if(i.longitude < minLong) {
			minLong = i.longitude;
		}
		if(i.longitude > maxLong) {
			maxLong = i.longitude;
		}
		intersectionMap.put(i.IntersectionID, i);
		unknownIntersectionsHeap.add(i);
		LinkedList temp = new LinkedList();
		temp.insert(i);
		graph.put(i.IntersectionID, temp);
	}
	
	public void insertRoad(Road e) {

		LinkedList int1 = graph.get(e.intersect1);
		LinkedList int2 = graph.get(e.intersect2);
		int1.insert(e);
		int2.insert(e);
		roads.add(e);
	}
	
	public static Intersection intersectLookup(String intersectID) {
		
		return intersectionMap.get(intersectID);
		
	}
	
	public static double RoadDistance(Intersection int1, Intersection int2) {
		
		return CalculateDistance(int1.latitude, int1.longitude, int2.latitude, int2.longitude);
	}

	public static double CalculateDistance(double lat1, double long1, double lat2, double long2) {
		
		int EarthRadius = 6371000;
		lat1 = Math.toRadians(lat1);
		long1 = Math.toRadians(long1);
		lat2 = Math.toRadians(lat2);
		long2 = Math.toRadians(long2);
		double changeLat = lat2-lat1;
		double changeLong = long2-long1;
		double temp = (Math.sin(changeLat/2) * Math.sin(changeLat/2)) + (Math.cos(lat1) * Math.cos(lat2) * Math.sin(changeLong/2) * Math.sin(changeLong/2));
		double temp2 = 2 * Math.atan2(Math.sqrt(temp), Math.sqrt(1-temp));
		return EarthRadius * temp2;
	}

}
