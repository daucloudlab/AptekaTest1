package kz.abcsoft.aptekatest1.models;

public class Medikament {

    private String mid ;
    private String cid ;
    private String aid ;
    private String title ;
    private String description ;
    private double price ;
    private byte[] information ;
    private String image ;

    public Medikament() {}

    public Medikament(String mid, String aid, String cid, String title, String description,
                      double price, byte [] information, String image){
        this.mid = mid ;
        this.aid = aid ;
        this.cid = cid ;
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setInformation(information);
        this.setImage(image);
    }


    public String getMid() {
        return mid;
    }


    public String getCid() {
        return cid;
    }



    public String getAid() {
        return aid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getInformation() {
        return information;
    }

    public void setInformation(byte[] information) {
        this.information = information;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
