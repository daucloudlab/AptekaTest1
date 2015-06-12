package kz.abcsoft.aptekatest1.models;

public class Apteka {
    private String aid ;
    private String name ;
    private String phone ;
    private String address ;
    private double latitude ;
    private double longitude ;

    public Apteka() {}

    public Apteka(String aid, String name, String phone, String address, double latitude, double longitude){
        this.aid = aid ;
        this.setName(name);
        this.setPhone(phone);
        this.setAddress(address);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public String getAid(){
        return aid ;
    }
    public void setAid(String aid){
        this.aid = aid ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
