package com.pawel.weatherapp.enums;

public enum FieldsEnum {
    NAME("name"),
    REGION("region"),
    COUNTRY("country"),
    LAT("latitude"),
    LON("longitude"),
    TZ_ID("timezone"),
    LOCALTIME("localtime"),

    //current:
    LAST_UPDATED("updated"),
    TEMP_C("temperature [c]"),
    IS_DAY("is_day"),
    WIND_KPH("wind"),
    WIND_DEGREE("wind degree"),
    WIND_DIR("wind directon"),
    PRESSURE_MB("pressure [mb]"),
    PRECIP_MM("precip [mm]"),
    HUMIDITY("humidity"),
    CLOUD("cloud"),
    FEELSLIKE_C("feelslike"),
    VIS_KM("vis"),
    UV("uv"),
//    GUST_KPH("gust")
    CONDITION("condition"),

    //forecast:
    FORECASTDAY("forecastday");

    String name;

    FieldsEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
