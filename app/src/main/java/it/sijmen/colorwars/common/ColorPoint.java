package it.sijmen.colorwars.common;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

class ColorPoint {

    //y
    private final LatLng position;

    private final TeamColor color;

    private int num;
    private List<ColorPoint> nearby = new ArrayList<>(2);
    private ColorPoint parent;


    public ColorPoint(double lat, double lon, TeamColor color) {
        position = new LatLng(lat, lon);
        this.color = color;
        num = color.getNum();
    }

    public int getColor() {
        return color.getColor();
    }

    public LatLng getPosition() {
        return position;
    }

    public TeamColor getTeamColor() {
        return color;
    }


    private static float[] results = new float[1];

    public float getDistance(ColorPoint p) {
        Location.distanceBetween(position.latitude, position.longitude, p.getPosition().latitude, p.getPosition().longitude, results);
        return results[0];
    }

    public int getNum() {
        return num;
    }

    public void addNearby(ColorPoint p) {
        if(p.isPreviousPoint(this)) {
            parent = p;
        } else {
            System.out.println(getDistance(p));
            System.out.println("ng parent");
        }
        nearby.add(p);
    }

    public List<ColorPoint.Path> getNearbyPoints() {
        Path mainPath = new Path(null, this);
        List<ColorPoint.Path> paths = new ArrayList<>(nearby.size());
        for(ColorPoint point : nearby) {
            paths.add(new Path(mainPath, point));
        }

        return paths;
    }

    public void removePoints(List<ColorPoint> points) {
        if(points.contains(parent)) {
            parent = null;
        }
        nearby.removeAll(points);
    }

    public void render(GoogleMap mMap) {
//        if(parent != null) {
//            PolylineOptions options = new PolylineOptions().add(getPosition()).add(parent.getPosition()).color(color.getColor());
//            mMap.addPolyline(options);
//        }
        PolylineOptions options = new PolylineOptions().add(getPosition()).color(color.getColor());
        for(ColorPoint p: nearby) {
            options = options.add(p.getPosition());
        }
        mMap.addPolyline(options);
    }

    public boolean isPreviousPoint(ColorPoint newPoint) {
        return this.getNum() == newPoint.getNum() - 1;
    }

    public static class Path {

        private ColorPoint.Path previous;
        private ColorPoint me;


        public Path(Path path, ColorPoint point) {
            previous = path;
            me = point;
        }

        public Path addChild(ColorPoint point) {
            return new Path(this, point);
        }

        public List<ColorPoint> toPolyList() {
            List<ColorPoint> points;
            if(previous == null) {
                points = new ArrayList<>();
            } else {
                points = previous.toPolyList();
            }
            points.add(me);
            return points;
        }

        public ColorPoint getChild() {
            return me;
        }

        public boolean isCycle() {
            return (previous != null && previous.containsMe(me));
        }

        private boolean containsMe(ColorPoint point) {
            return me == point || (previous != null && previous.containsMe(point));
        }

        public List<ColorPoint.Path> extend() {
            ColorPoint parent = null;
            if(previous != null) {
                parent = previous.me;
            }
            List<ColorPoint.Path> paths = new ArrayList<>();
            for(ColorPoint p: me.nearby) {
                if(p != parent) {
                    paths.add(addChild(p));
                }
            }
            return paths;
        }
    }

}
