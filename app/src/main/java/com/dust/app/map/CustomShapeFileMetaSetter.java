package com.dust.app.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.iryndin.jdbf.core.DbfRecord;

import org.osmdroid.shape.ShapeMetaSetter;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.nio.charset.Charset;
import java.text.ParseException;

public class CustomShapeFileMetaSetter implements ShapeMetaSetter {

    private static String getSensibleTitle(String string) {
        if (string.length() > 20) {
            return string.substring(0, 16) + "...";
        }
        return string;
    }

    @Override
    public void set(DbfRecord metadata, Marker marker) throws ParseException {
        if (metadata != null) {
            metadata.setStringCharset(Charset.defaultCharset());
            JSONObject metaData = JSON.parseObject(JSONObject.toJSONString(metadata.toMap()));
            marker.setTitle(metaData.getString("object"));
            marker.setSnippet(getSensibleTitle(metaData.getString("name")));
            marker.setSubDescription("类型：" + metaData.getString("type_desc"));
        }
    }

    @Override
    public void set(DbfRecord metadata, Polyline polyline) throws ParseException {
        if (metadata != null) {
            metadata.setStringCharset(Charset.defaultCharset());
            JSONObject metaData = JSON.parseObject(JSONObject.toJSONString(metadata.toMap()));
            polyline.setTitle(metaData.getString("object"));
            polyline.setSnippet(getSensibleTitle(metaData.getString("name")));
            polyline.setSubDescription("类型：" + metaData.getString("type_desc") + "<br>长度：" + metaData.getString("length") + "m");
        }
    }

    @Override
    public void set(DbfRecord metadata, Polygon polygon) throws ParseException {
        if (metadata != null) {
            metadata.setStringCharset(Charset.defaultCharset());
            JSONObject metaData = JSON.parseObject(JSONObject.toJSONString(metadata.toMap()));
            polygon.setTitle(metaData.getString("object"));
            polygon.setSnippet(getSensibleTitle(metaData.getString("name")));
            polygon.setSubDescription("类型：" + metaData.getString("type_desc") + "<br>面积:" + metaData.getString("area") + "m²");
        }
    }

}
