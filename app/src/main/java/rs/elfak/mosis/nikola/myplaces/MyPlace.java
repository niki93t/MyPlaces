package rs.elfak.mosis.nikola.myplaces;

/**
 * Created by Nikola on 4/6/2016.
 */
public class MyPlace {
    String name;
    String description;
    String longitude;
    String latitude;
    long ID;

    public MyPlace(String nme, String desc)
    {
        this.name = nme;
        this.description = desc;
    }

    public MyPlace(String nme)
    {
        this(nme, "");
    }

    public String getName()
    {
        return name;
    }

    public  String getDescription()
    {
        return description;
    }

    public void setName(String nme)
    {
        this.name = nme;
    }

    public void setDescription(String desc)
    {
        this.description = desc;
    }

    public String getLongitude(){
        return longitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public long getID(){
        return ID;
    }

    public void setID(long ID){
        this.ID = ID;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
