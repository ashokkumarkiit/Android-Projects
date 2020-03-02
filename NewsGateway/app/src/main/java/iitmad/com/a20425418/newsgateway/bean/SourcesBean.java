package iitmad.com.a20425418.newsgateway.bean;

import java.io.Serializable;

/**
 * Created by Ashok Kumar - A20425418 on 11/21/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class SourcesBean implements Serializable {

    String source_id;
    String source_name;
    String source_category;

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_category() {
        return source_category;
    }

    public void setSource_category(String source_category) {
        this.source_category = source_category;
    }
}
