package com.dust.app.map;

import com.dust.app.BuildConfig;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

public class CustomTileSource extends TileSourceFactory {

    public static final OnlineTileSourceBase GoogleSatWGS84 = new XYTileSource("Google-Hybrid",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=s&scale=2&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌卫星混合
    public static final OnlineTileSourceBase GoogleHybrid = new XYTileSource("Google-Hybrid",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=y&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌卫星
    public static final OnlineTileSourceBase GoogleSatGCJ02 = new XYTileSource("Google-Sat",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=s&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌地图
    public static final OnlineTileSourceBase GoogleRoads = new XYTileSource("Google-Roads",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=m&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌地形
    public static final OnlineTileSourceBase GoogleTerrain = new XYTileSource("Google-Terrain",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=t&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //谷歌地形带标注
    public static final OnlineTileSourceBase GoogleTerrainHybrid = new XYTileSource("Google-Terrain-Hybrid",
            0, 20, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=p&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //高德地图
    public static final OnlineTileSourceBase AutoNaviVector = new XYTileSource("AutoNavi-Vector",
            0, 20, 256, ".png", new String[]{
            "https://wprd01.is.autonavi.com/appmaptile?",
            "https://wprd02.is.autonavi.com/appmaptile?",
            "https://wprd03.is.autonavi.com/appmaptile?",
            "https://wprd04.is.autonavi.com/appmaptile?",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z="
                    + MapTileIndex.getZoom(pMapTileIndex) + "&lang=zh_cn&size=1&scl=1&style=7&ltype=7";
        }
    };

    //影像地图 _W是墨卡托投影  _c是国家2000的坐标系
    public static final OnlineTileSourceBase tianDiTuImgTileSource = new XYTileSource("TianDiTuImg", 1, 18, 768, ".png",
            new String[]{"http://t1.tianditu.com/DataServer?T=img_w",
                    "http://t2.tianditu.com/DataServer?T=img_w",
                    "http://t3.tianditu.com/DataServer?T=img_w",
                    "http://t4.tianditu.com/DataServer?T=img_w",
                    "http://t5.tianditu.com/DataServer?T=img_w",
                    "http://t6.tianditu.com/DataServer?T=img_w"}) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "&X=" + MapTileIndex.getX(pMapTileIndex) + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    //影像标注 _W是墨卡托投影  _c是国家2000的坐标系
    public static final OnlineTileSourceBase tianDiTuCiaTileSource = new XYTileSource("TianDiTuCia", 1, 18, 768, ".png",
            new String[]{"http://t1.tianditu.com/DataServer?T=cia_w",
                    "http://t2.tianditu.com/DataServer?T=cia_w",
                    "http://t3.tianditu.com/DataServer?T=cia_w",
                    "http://t4.tianditu.com/DataServer?T=cia_w",
                    "http://t5.tianditu.com/DataServer?T=cia_w",
                    "http://t6.tianditu.com/DataServer?T=cia_w"}) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "&X=" + MapTileIndex.getX(pMapTileIndex) + "&Y=" + MapTileIndex.getY(pMapTileIndex) + "&L=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    public static final OnlineTileSourceBase tianDiTuRoadTileSource = new OnlineTileSourceBase("MGRS", 0, 18, 256, "PNG", new String[0]) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int zoom = MapTileIndex.getZoom(pMapTileIndex);
            // 高德道路png
            // return "https://wprd02.is.autonavi.com/appmaptile?x=" + x + "&y=" + y + "&z=" + zoom + "&style=8";
            // 天地图标注
            // return "https://t3.tianditu.gov.cn/DataServer?T=cva_w&x=" + x + "&y=" + y + "&l=" + zoom + "&tk=2ce94f67e58faa24beb7cb8a09780552";
            // 天地图道路png
            return "https://t6.tianditu.gov.cn/DataServer?T=cia_w&x=" + x + "&y=" + y + "&l=" + zoom + "&tk=" + BuildConfig.tianDiTuToken;
        }
    };

    public static final OnlineTileSourceBase tianDiTuTerrainTileSource = new OnlineTileSourceBase("MGRS", 0, 20, 256, "PNG", new String[0]) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            int x = MapTileIndex.getX(pMapTileIndex);
            int y = MapTileIndex.getY(pMapTileIndex);
            int zoom = MapTileIndex.getZoom(pMapTileIndex);
            return "https://t1.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + x + "&TILEROW=" + y + "&TILEMATRIX=" + zoom + "&tk=" + BuildConfig.tianDiTuToken;
        }
    };

}