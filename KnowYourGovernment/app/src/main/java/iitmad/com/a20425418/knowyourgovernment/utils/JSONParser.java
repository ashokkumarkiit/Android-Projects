package iitmad.com.a20425418.knowyourgovernment.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.knowyourgovernment.beans.GoogleCivicBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficesBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficesLocationBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialAddressBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialChannelsBean;
import iitmad.com.a20425418.knowyourgovernment.beans.OfficialsBean;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class JSONParser {

    public GoogleCivicBean parser(String jsonString){
        GoogleCivicBean googleCivicBean;
        JSONObject jsonGoogleCivic = new JSONObject();
        JSONObject jsonOfficesLocation = new JSONObject();
        JSONArray jsonOffices = new JSONArray();
        JSONArray jsonOfficials = new JSONArray();
        OfficesLocationBean officeLocatinBean;
        List<OfficesBean> listOfficesBean;
        List<OfficialsBean> listOfficialsBean;
        OfficesBean officesBean;
        OfficialsBean officialsBean;
        try {
            googleCivicBean = new GoogleCivicBean();
            listOfficesBean = new ArrayList<OfficesBean>();
            listOfficialsBean = new ArrayList<OfficialsBean>();
            jsonGoogleCivic = new JSONObject(jsonString);

            if(jsonGoogleCivic.has("normalizedInput"))
                jsonOfficesLocation = jsonGoogleCivic.getJSONObject("normalizedInput");

            if(jsonGoogleCivic.has("offices"))
                jsonOffices = jsonGoogleCivic.getJSONArray("offices");

            if(jsonGoogleCivic.has("officials"))
                jsonOfficials = jsonGoogleCivic.getJSONArray("officials");

            //Fetching the offices location data and putting it into OfficesLocationBean
            officeLocatinBean = new OfficesLocationBean();
            if(jsonOfficesLocation.has("city"))
                officeLocatinBean.setCity(jsonOfficesLocation.getString("city"));
            if(jsonOfficesLocation.has("state"))
                officeLocatinBean.setState(jsonOfficesLocation.getString("state"));
            if(jsonOfficesLocation.has("zip"))
                officeLocatinBean.setZip(jsonOfficesLocation.getString("zip"));
            googleCivicBean.setOfficesLocation(officeLocatinBean);

            //Fetching the list of offices and placing in the list object of OfficesBean
            for(int i = 0; i< jsonOffices.length(); i++){
                JSONObject tempJsonOfficesObject = (JSONObject) jsonOffices.get(i);
                officesBean = new OfficesBean();
                if(tempJsonOfficesObject.has("name"))
                    officesBean.setOffice_name(tempJsonOfficesObject.getString("name"));
                if(tempJsonOfficesObject.has("officialIndices")){
                    JSONArray tempofficialIndicesArray = tempJsonOfficesObject.getJSONArray("officialIndices");
                    List<String> tempString = new ArrayList<String>();
                    for(int j = 0; j < tempofficialIndicesArray.length(); j++){
                        tempString.add(tempofficialIndicesArray.getString(j));
                    }
                    officesBean.setOfficialIndices(tempString);
                }
                listOfficesBean.add(officesBean);
            }
            googleCivicBean.setOffices(listOfficesBean);

            //Fetching the list of officials and plasing in the list object of OfficialsBean
            for(int i = 0; i< jsonOfficials.length();i++ ){
                JSONObject tempJsonOfficialObject = (JSONObject) jsonOfficials.get(i);
                officialsBean = new OfficialsBean();
                if(tempJsonOfficialObject.has("name"))
                    officialsBean.setOfficialName(tempJsonOfficialObject.getString("name"));

                //Adding Address to the officials bean object
                if(tempJsonOfficialObject.has("address")){
                    JSONArray tempJsonOfficialsAddressArray = (JSONArray) tempJsonOfficialObject.getJSONArray("address");
                    if(tempJsonOfficialObject.length() > 0 ){
                        JSONObject tempAddressObject = (JSONObject) tempJsonOfficialsAddressArray.get(0);
                        OfficialAddressBean addressBean = new OfficialAddressBean();
                        if(tempAddressObject.has("line1"))
                            addressBean.setAddrLine1(tempAddressObject.getString("line1"));
                        if(tempAddressObject.has("line2"))
                            addressBean.setAddrLine2(tempAddressObject.getString("line2"));
                        if(tempAddressObject.has("line3"))
                            addressBean.setAddrLine3(tempAddressObject.getString("line3"));
                        if(tempAddressObject.has("city"))
                            addressBean.setAddrCity(tempAddressObject.getString("city"));
                        if(tempAddressObject.has("state"))
                            addressBean.setAddrState(tempAddressObject.getString("state"));
                        if(tempAddressObject.has("zip"))
                            addressBean.setAddrZip(tempAddressObject.getString("zip"));
                        officialsBean.setOfficialAddress(addressBean);
                    }
                }

                if(tempJsonOfficialObject.has("party"))
                    officialsBean.setOfficialParty(tempJsonOfficialObject.getString("party"));
                else
                    officialsBean.setOfficialParty("Unknown");

                //Fetching phone nos from json array of phones
                if(tempJsonOfficialObject.has("phones")){
                    JSONArray tempJsonOfficialsPhonesArray = (JSONArray) tempJsonOfficialObject.getJSONArray("phones");
                    String[] tempofficialsPhonesArray = new String[tempJsonOfficialsPhonesArray.length()];
                    for(int j=0; j< tempJsonOfficialsPhonesArray.length(); j++){
                        tempofficialsPhonesArray[j] = tempJsonOfficialsPhonesArray.getString(0);
                    }
                    officialsBean.setOfficialPhones(tempofficialsPhonesArray);
                }

                //Fetching Officials URLs
                if(tempJsonOfficialObject.has("urls")){
                    JSONArray tempJsonOfficialsURLsArray = (JSONArray) tempJsonOfficialObject.getJSONArray("urls");
                    String[] tempOfficialsURLsArray = new String[tempJsonOfficialsURLsArray.length()];
                    for(int j = 0; j < tempJsonOfficialsURLsArray.length(); j++){
                        tempOfficialsURLsArray[j] = tempJsonOfficialsURLsArray.getString(0);
                    }
                    officialsBean.setOfficialUrls(tempOfficialsURLsArray);
                }

                //Fetching Officials Emails
                if(tempJsonOfficialObject.has("emails")){
                    JSONArray tempJsonOfficialsEmailsArray = (JSONArray) tempJsonOfficialObject.getJSONArray("emails");
                    String[] tempOfficialsEmailsArray = new String[tempJsonOfficialsEmailsArray.length()];
                    for(int j = 0; j < tempJsonOfficialsEmailsArray.length(); j++){
                        tempOfficialsEmailsArray[j] = tempJsonOfficialsEmailsArray.getString(0);
                    }
                    officialsBean.setOfficialEmails(tempOfficialsEmailsArray);
                }

                if(tempJsonOfficialObject.has("photoUrl"))
                    officialsBean.setOfficialPhotoUrl(tempJsonOfficialObject.getString("photoUrl"));

                //Fetching Officials channels
                if(tempJsonOfficialObject.has("channels")){
                    JSONArray tempJsonOfficialsChannelsArray = (JSONArray) tempJsonOfficialObject.getJSONArray("channels");
                    if(tempJsonOfficialsChannelsArray.length() > 0 ) {
                        List<OfficialChannelsBean> listChannelsBean = new ArrayList<OfficialChannelsBean>();
                        for (int j = 0; j < tempJsonOfficialsChannelsArray.length(); j++) {
                            JSONObject tempJsonOfficialsChannelsObject = (JSONObject) tempJsonOfficialsChannelsArray.getJSONObject(j);
                            OfficialChannelsBean channelsBean = new OfficialChannelsBean();
                            if (tempJsonOfficialsChannelsObject.has("id"))
                                channelsBean.setChannelId(tempJsonOfficialsChannelsObject.getString("id"));
                            if (tempJsonOfficialsChannelsObject.has("type"))
                                channelsBean.setChannelType(tempJsonOfficialsChannelsObject.getString("type"));
                            listChannelsBean.add(channelsBean);
                        }
                        officialsBean.setOfficialChannels(listChannelsBean);
                    }
                }
                listOfficialsBean.add(officialsBean);
            }
            googleCivicBean.setOfficials(listOfficialsBean);
            return googleCivicBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
