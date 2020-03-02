package iitmad.com.a20425418.newsgateway.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ashok Kumar - A20425418 on 11/21/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class NewsSourcesBean implements Serializable {

    List<SourcesBean> listSourceBean;
    Set<String> listCategory;

    public List<SourcesBean> getListSourceBean() {
        return listSourceBean;
    }

    public void setListSourceBean(List<SourcesBean> listSourceBean) {
        this.listSourceBean = listSourceBean;
    }

    public Set<String> getListCategory() {
        return listCategory;
    }

    public void setListCategory(Set<String> listCategory) {
        this.listCategory = listCategory;
    }
}
