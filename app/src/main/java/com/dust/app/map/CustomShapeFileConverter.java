package com.dust.app.map;

import android.util.Log;

import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointPlainShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PointShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;
import org.osmdroid.api.IMapView;
import org.osmdroid.shape.ShapeMetaSetter;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class CustomShapeFileConverter {

    public static List<Overlay> convert(MapView map, File file, ValidationPreferences prefs, ShapeMetaSetter shapeMetaSetter) throws Exception {
        List<Overlay> folder = new ArrayList<>();
        File dbf = new File(file.getParentFile(), file.getName().replace(".shp", ".dbf"));
        try (FileInputStream is = new FileInputStream(file);
             FileInputStream dbfInputStream = new FileInputStream(dbf);
             DbfReader dbfReader = new DbfReader(dbfInputStream)) {

            ShapeFileReader r = new ShapeFileReader(is, prefs);
            AbstractShape s;
            while ((s = r.next()) != null) {
                DbfRecord metadata = dbfReader.read();
                switch (s.getShapeType()) {
                    case POINT: {
                        PointShape aPoint = (PointShape) s;
                        Marker m = new Marker(map);
                        m.setTextIcon("123");
                        m.setVisible(false);
                        m.setPosition(fixOutOfRange(new GeoPoint(aPoint.getY(), aPoint.getX())));
                        shapeMetaSetter.set(metadata, m);
                        folder.add(m);
                    }
                    break;
                    case MULTIPOINT: {
                        MultiPointPlainShape aPoint = (MultiPointPlainShape) s;
                        PointData[] points = aPoint.getPoints();
                        for (PointData p : points) {
                            Marker m = new Marker(map);
                            m.setPosition(fixOutOfRange(new GeoPoint(p.getY(), p.getX())));
                            shapeMetaSetter.set(metadata, m);
                            folder.add(m);
                        }
                    }
                    break;
                    case POLYLINE: {
                        PolylineShape polylineShape = (PolylineShape) s;
                        for (int i = 0; i < polylineShape.getNumberOfParts(); i++) {
                            Polyline line = new Polyline(map);
                            PointData[] points = polylineShape.getPointsOfPart(i);
                            List<GeoPoint> pts = new ArrayList<>();
                            for (PointData p : points) {
                                GeoPoint pt = fixOutOfRange(new GeoPoint(p.getY(), p.getX()));
                                pts.add(pt);
                            }
                            line.setPoints(pts);
                            line.setVisible(false);
                            shapeMetaSetter.set(metadata, line);
                            folder.add(line);
                        }
                    }
                    break;
                    case POLYGON: {
                        PolygonShape aPolygon = (PolygonShape) s;
                        for (int i = 0; i < aPolygon.getNumberOfParts(); i++) {
                            Polygon polygon = new Polygon(map);
                            PointData[] points = aPolygon.getPointsOfPart(i);
                            List<GeoPoint> pts = new ArrayList<>();
                            for (PointData p : points) {
                                GeoPoint pt = fixOutOfRange(new GeoPoint(p.getY(), p.getX()));
                                pts.add(pt);
                            }
                            pts.add(pts.get(0));// force the polygon to close
                            polygon.setPoints(pts);
                            polygon.getOutlinePaint().setColor(0);
                            shapeMetaSetter.set(metadata, polygon);
                            folder.add(polygon);
                        }
                    }
                    break;
                    default:
                        Log.w(IMapView.LOGTAG, s.getShapeType() + " was unhandled! " + s.getClass().getCanonicalName());
                }
            }
        }
        return folder;
    }

    public static ValidationPreferences getDefaultValidationPreferences() {
        final ValidationPreferences pref = new ValidationPreferences();
        pref.setMaxNumberOfPointsPerShape(200000);
        return pref;
    }

    public static List<Overlay> convert(MapView map, File file) throws Exception {
        return convert(map, file, getDefaultValidationPreferences());
    }

    public static List<Overlay> convert(MapView map, File file, ValidationPreferences pref) throws Exception {
        return convert(map, file, pref, new CustomShapeFileMetaSetter());
    }

    private static GeoPoint fixOutOfRange(GeoPoint point) {
        if (point.getLatitude() > 90.00)
            point.setLatitude(90.00);
        else if (point.getLatitude() < -90.00)
            point.setLatitude(-90.00);

        if (abs(point.getLongitude()) > 180.00) {
            double longitude = point.getLongitude();
            double diff = longitude > 0 ? -360 : 360;
            while (abs(longitude) > 180)
                longitude += diff;
            point.setLongitude(longitude);
        }
        return point;
    }

}
