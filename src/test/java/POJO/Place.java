package POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    private  String placename;
    private String longitude;
    private  String state;
    private  String latitude;
    private  String stateabbreviation;

    public String getPlacename() {
        return placename;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getState() {
        return state;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getStateabbreviation() {
        return stateabbreviation;
    }

    @JsonProperty("place name")
    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public void setState(String state) {
        this.state = state;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    @JsonProperty("state abbreviation")
    public void setStateabbreviation(String stateabbreviation) {
        this.stateabbreviation = stateabbreviation;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placename='" + placename + '\'' +
                ", longitude='" + longitude + '\'' +
                ", state='" + state + '\'' +
                ", latitude='" + latitude + '\'' +
                ", stateabbreviation='" + stateabbreviation + '\'' +
                '}';
    }
}


/*
      Location Class ı

String post code;
String country;
String country abbreviation;
List<Places> places;
List<District> Districts;

Places Class
String place name;
String longitude;
String state;
String latitude;

District Class
String place name;
String longitude;
String state;
String latitude;

{
    "post code": "01000",
    "country": "Turkey",
    "country abbreviation": "TR",
    "places": [
        {
            "place name": "Dervişler Köyü",
            "longitude": "37.4987",
            "state": "Adana",
            "state abbreviation": "1",
            "latitude": "36.9748"
        },
        {
            "place name": "Camuzcu Köyü",
            "longitude": "37.4987",
            "state": "Adana",
            "state abbreviation": "1",
            "latitude": "36.9748"
        }
    ]
    "district": [
        {
            "place name": "Dervişler Köyü",
            "longitude": "37.4987",
            "state": "Adana",
            "state abbreviation": "1",
            "latitude": "36.9748"
        },
        {
            "place name": "Camuzcu Köyü",
            "longitude": "37.4987",
            "state": "Adana",
            "state abbreviation": "1",
            "latitude": "36.9748"
        }
    ]
}

        */
