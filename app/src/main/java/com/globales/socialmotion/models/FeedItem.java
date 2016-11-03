package com.globales.socialmotion.models;

import java.io.Serializable;

/**
 * Created by Francisco on 23-May-16.
 */
public class FeedItem implements Serializable {

    private String name;
    private String msgTxt;
    private String timestamp;
    private String id;

    public String getName() {
        return name;
    }

    public FeedItem() {
        this("", "", "");
    }

    public FeedItem(String name, String msgTxt, String timestamp) {
        this.name = name;
        this.msgTxt = msgTxt;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class MyAddress {

        private String address;
        private double lat;
        private double lng;

        public MyAddress(){

        }

        public MyAddress(String address, double lat, double lng) {
            this.address = address;
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

         public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }




    }

}
