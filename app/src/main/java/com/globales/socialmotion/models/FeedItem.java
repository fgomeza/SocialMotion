package com.globales.socialmotion.models;

/**
 * Created by Francisco on 23-May-16.
 */
public class FeedItem {

    private String name;
    private String msgTxt;
    private String timestamp;
    private String petName;
    private String imageUrl;
    private MyAddress address;
    private boolean found;
    private String id;

    public MyAddress getAddress() {
        return address;
    }

    public void setAddress(MyAddress address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public FeedItem() {
        this("", "", "", "", "", null, false);
    }

    public FeedItem(String name, String msgTxt, String petName, String timestamp, String imageUrl, MyAddress address, boolean found) {

        this.name = name;
        this.msgTxt = msgTxt;
        this.timestamp = timestamp;
        this.petName = petName;
        this.imageUrl = imageUrl;
        this.found = found;
        this.address = address;

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

    public String getPetName() { return petName; }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
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
