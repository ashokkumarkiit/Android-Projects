package iitmad.com.a20425418.knowyourgovernment.beans;

import java.io.Serializable;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class OfficesLocationBean implements Serializable {

    String city;
    String state;
    String zip;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
