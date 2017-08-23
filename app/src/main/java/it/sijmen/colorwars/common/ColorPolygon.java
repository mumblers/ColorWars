package it.sijmen.colorwars.common;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

class ColorPolygon {

    private PolygonOptions options;

    private TeamColor color;

    //null until rendered once
    private Polygon polygon;

    public ColorPolygon(List<ColorPoint> points) {
        if(points.size() < 3) {
            throw new IllegalArgumentException("Must have at least three points to make a polygon");
        }
        color = points.get(0).getTeamColor();

        options = new PolygonOptions().fillColor(color.getColor()).strokeColor(0xFF000000);

        for (ColorPoint point: points) {
            if(point.getTeamColor() != color) {
                throw new IllegalArgumentException("Points must be from the same color");
            }
            options = options.add(point.getPosition());
        }
    }

    public boolean intersects(ColorPoint point) {
        return intersects(point.getPosition());
    }

    private boolean intersects(LatLng position) {
        return PolyUtil.containsLocation(position, options.getPoints(), false);
    }

    public void checkForHole(ColorPolygon polygon) {
        for(LatLng position : polygon.options.getPoints()) {
            if(!intersects(position)) {
                return;
            }
        }
        //all points intersect so add it as hole
        options = options.addHole(polygon.options.getPoints());
    }

    public void render(GoogleMap mMap) {
        polygon = mMap.addPolygon(options);
        System.out.println("rendering polygon length:::::" + options.getPoints().size());
    }
}
