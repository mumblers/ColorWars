package it.sijmen.colorwars.common;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static it.sijmen.colorwars.common.TeamColor.BLUE;
import static it.sijmen.colorwars.common.TeamColor.RED;

public class ColorWarServer {

    private LatLngBounds bounds = new LatLngBounds(new LatLng(51.2,3), new LatLng(61.2,13));
    private double width = bounds.northeast.longitude - bounds.southwest.longitude;
    private double height = bounds.northeast.latitude - bounds.southwest.latitude;

    private Map<TeamColor,List<ColorPoint>> points = new Hashtable<>();

    private List<ColorPolygon> polygons = new ArrayList<>();

    public ColorWarServer() {
        for(TeamColor color :TeamColor.values()) {
            points.put(color, new ArrayList<ColorPoint>());
        }
    }

    private void addPoint(ColorPoint newPoint) {
        List<ColorPoint> teamPoints = points.get(newPoint.getTeamColor());

        for(ColorPoint teamPoint: teamPoints) {
            if(teamPoint.isPreviousPoint(newPoint) || newPoint.getDistance(teamPoint) < 15) {
                newPoint.addNearby(teamPoint);
                teamPoint.addNearby(newPoint);
            }
        }

        teamPoints.add(newPoint);
        checkForPolygon(newPoint);
    }

    private void checkForPolygon(ColorPoint point) {
        List<ColorPoint.Path> todo = point.getNearbyPoints();
        List<ColorPoint.Path> toAdd = new ArrayList<>();
        System.out.println("Checking for polygon");

        while (!todo.isEmpty()) {
            for(ColorPoint.Path path : todo) {
                if(path.isCycle()) {
                    addPolygon(path.toPolyList());
                    return;
                }
                toAdd.addAll(path.extend());
            }

            todo.clear();
            todo.addAll(toAdd);
            toAdd.clear();
        }
    }


    //this also removes the points from the list
    private void addPolygon(List<ColorPoint> polyPoints) {
        System.out.println("adding polygon");
        TeamColor color = polyPoints.get(0).getTeamColor();
        points.get(color).removeAll(polyPoints);

        for(ColorPoint p :points.get(color)) {
            p.removePoints(polyPoints);
        }

        ColorPolygon polygon = new ColorPolygon(polyPoints);

        removePointUnderPolygon(polygon);

        checkForHoles(polygon);

        polygons.add(polygon);
    }

    private void removePointUnderPolygon(ColorPolygon polygon) {
        System.out.println("Removing points");
        for(Map.Entry<TeamColor, List<ColorPoint>> entry : points.entrySet()) {
            for (Iterator<ColorPoint> iterator = entry.getValue().iterator(); iterator.hasNext(); ) {
                if (polygon.intersects(iterator.next())) {
                    iterator.remove();
                }
            }
        }
    }

    private void checkForHoles(ColorPolygon polygon) {
        for(ColorPolygon p : polygons) {
            polygon.checkForHole(p);
        }
    }


    void addPoint(LatLng position, TeamColor color) {
        double lat = position.latitude - bounds.southwest.latitude;
        double lng = position.longitude - bounds.southwest.longitude;

        lat = Math.max(0.0, Math.min(height, lat));
        lng = Math.max(0.0, Math.min(width, lng));

        addPoint(new ColorPoint(lat, lng, color));
    }

    void addPoint(double lat, double lng, TeamColor color) {
        addPoint(new LatLng(lat/1000.0 + bounds.southwest.latitude, lng/1000.0 + bounds.southwest.longitude), color);
    }

    public void parsePoints() {
        addPoint(50,50, RED);
        addPoint(50,90, RED);
        addPoint(25,90, RED);
        addPoint(25,49.999, RED);
        addPoint(52,50, RED);
        addPoint(55,51, RED);
        addPoint(65,25, BLUE);
        addPoint(63,50, RED);
        addPoint(65,60, BLUE);
        addPoint(70,49, RED);
        addPoint(75,60, BLUE);
        addPoint(80,48, RED);
        addPoint(75,28, BLUE);
        addPoint(90,50, RED);
        addPoint(64,22, BLUE);
        addPoint(90,70, RED);
        addPoint(51,25, BLUE);
        addPoint(90,80, RED);
        addPoint(24,25, BLUE);
        addPoint(74,80, RED);
        addPoint(15,45, BLUE);
        addPoint(68,80, RED);
        addPoint(15,95, BLUE);
        addPoint(60,80, RED);
        addPoint(60,101, BLUE);
        addPoint(60,70, RED);
        addPoint(80,98, BLUE);
        addPoint(60,27, RED);
        addPoint(95,95, BLUE);
        addPoint(60,10, RED);
        addPoint(95,25, BLUE);
        addPoint(70,10, RED);
        addPoint(95,0, BLUE);
        addPoint(81,10, RED);
        addPoint(70,-5, BLUE);
        addPoint(81,25, RED);
        addPoint(70,-5, BLUE);
        addPoint(81,35, RED);
        addPoint(60,-5, BLUE);
        addPoint(81,43, RED);
        addPoint(35,-5, BLUE);
        addPoint(25,24, BLUE);
    }


    public void renderAll(GoogleMap mMap) {
        System.out.println("rendering " + polygons.size() + " polygons");

        for(ColorPolygon polygon: polygons) 
            polygon.render(mMap);
        
        for(Map.Entry<TeamColor, List<ColorPoint>> entry : points.entrySet()) 
            for(ColorPoint point : entry.getValue()) 
                point.render(mMap);
            
        

    }
}
