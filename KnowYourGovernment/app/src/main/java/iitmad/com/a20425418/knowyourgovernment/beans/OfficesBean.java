package iitmad.com.a20425418.knowyourgovernment.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class OfficesBean implements Serializable {

    String office_name;
    List<String> officialIndices;

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public List<String> getOfficialIndices() {
        return officialIndices;
    }

    public void setOfficialIndices(List<String> officialIndices) {
        this.officialIndices = officialIndices;
    }
}
