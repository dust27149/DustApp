package com.dust.app.bean;

public class Geocode {

    public AddressComponent addressComponent;
    public String formatted_address;
    public Location location;

    /*addressComponent: {
        "address": "西什库大街31号院20号楼",
        "city": "北京市",
        "county_code": "156110102",
        "nation": "中国",
        "poi_position": "西北",
        "county": "西城区",
        "city_code": "156110000",
        "address_position": "西北",
        "poi": "西什库大街31号院20号楼",
        "province_code": "156110000",
        "province": "北京市",
        "road": "大红罗厂街",
        "road_distance": 49,
        "poi_distance": 24,
        "address_distance": 24
    }*/
    public static class AddressComponent {
        public String address;// 此点最近地点信息 string 必返回
        public int address_distance;// 此点距离最近地点信息距离 int 必返回
        public String address_position;// 此点在最近地点信息方向 string 必返回
        public String city;// 此点所在国家或城市或区县 string 必返回
        public String poi;// 距离此点最近poi点 string 必返回
        public int poi_distance;// 距离此点最近poi点的距离 int 必返回
        public String poi_position;// 此点在最近poi点的方向 string 必返回
        public String road;// 距离此点最近的路 string 必返回
        public int road_distance;// 此点距离此路的距离 int 必返回
        public String county_code;
        public String nation;
        public String county;
        public String city_code;
        public String province_code;
        public String province;
    }

    public static class Location {
        public double lon;
        public double lat;
    }

}
