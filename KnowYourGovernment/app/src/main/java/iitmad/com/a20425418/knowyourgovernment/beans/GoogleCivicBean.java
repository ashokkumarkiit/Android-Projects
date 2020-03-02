package iitmad.com.a20425418.knowyourgovernment.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 10/29/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class GoogleCivicBean implements Serializable {

    OfficesLocationBean officesLocation;
    List<OfficesBean> offices;
    List<OfficialsBean> officials;

    public OfficesLocationBean getOfficesLocation() {
        return officesLocation;
    }

    public void setOfficesLocation(OfficesLocationBean officesLocation) {
        this.officesLocation = officesLocation;
    }

    public List<OfficesBean> getOffices() {
        return offices;
    }

    public void setOffices(List<OfficesBean> offices) {
        this.offices = offices;
    }

    public List<OfficialsBean> getOfficials() {
        return officials;
    }

    public void setOfficials(List<OfficialsBean> officials) {
        this.officials = officials;
    }
}
