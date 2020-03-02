package iitmad.com.a20425418.knowyourgovernment.beans;

import java.io.Serializable;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class OfficialChannelsBean implements Serializable {

    String channelType;
    String channelId;

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
