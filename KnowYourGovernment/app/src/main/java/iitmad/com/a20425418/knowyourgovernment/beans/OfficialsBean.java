package iitmad.com.a20425418.knowyourgovernment.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class OfficialsBean implements Serializable {

    String officialName;
    //Addresss
    OfficialAddressBean officialAddress;
    String officialParty;
    String[] officialPhones;
    String[] officialUrls;
    String[] officialEmails;
    String officialPhotoUrl;
    //Channels
    List<OfficialChannelsBean> officialChannels;

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public OfficialAddressBean getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(OfficialAddressBean officialAddress) {
        this.officialAddress = officialAddress;
    }

    public String getOfficialParty() {
        return officialParty;
    }

    public void setOfficialParty(String officialParty) {
        this.officialParty = officialParty;
    }

    public String[] getOfficialPhones() {
        return officialPhones;
    }

    public void setOfficialPhones(String[] officialPhones) {
        this.officialPhones = officialPhones;
    }

    public String[] getOfficialUrls() {
        return officialUrls;
    }

    public void setOfficialUrls(String[] officialUrls) {
        this.officialUrls = officialUrls;
    }

    public String[] getOfficialEmails() {
        return officialEmails;
    }

    public void setOfficialEmails(String[] officialEmails) {
        this.officialEmails = officialEmails;
    }

    public String getOfficialPhotoUrl() {
        return officialPhotoUrl;
    }

    public void setOfficialPhotoUrl(String officialPhotoUrl) {
        this.officialPhotoUrl = officialPhotoUrl;
    }

    public List<OfficialChannelsBean> getOfficialChannels() {
        return officialChannels;
    }

    public void setOfficialChannels(List<OfficialChannelsBean> officialChannels) {
        this.officialChannels = officialChannels;
    }
}
