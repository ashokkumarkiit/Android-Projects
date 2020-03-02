package iitmad.com.a20425418.newsgateway.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import iitmad.com.a20425418.newsgateway.bean.ArticleBean;
import iitmad.com.a20425418.newsgateway.bean.NewsSourcesBean;
import iitmad.com.a20425418.newsgateway.bean.SourcesBean;


/**
 * Created by Ashok Kumar - A20425418 on 11/21/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class JSONParser {

    private static final String TAG = "JSONParser";

    public NewsSourcesBean parseNewsSource(String newsSourceJSON){

        NewsSourcesBean newsSourcesBean = new NewsSourcesBean();
        List<SourcesBean> listSourceBean = new ArrayList<SourcesBean>();
        Set<String> listCategory = new HashSet<String>();
        JSONObject jsonNewsSource = new JSONObject();
        JSONArray jsonNewsSourceArray = new JSONArray();

        try{
            jsonNewsSource = new JSONObject(newsSourceJSON);
            jsonNewsSourceArray = jsonNewsSource.getJSONArray("sources");
            listCategory.add("all");
            for(int i = 0 ; i < jsonNewsSourceArray.length(); i++){
                JSONObject tempJsonNewsSourceObject = (JSONObject) jsonNewsSourceArray.get(i);
                SourcesBean bean = new SourcesBean();
                if(tempJsonNewsSourceObject.has("id"))
                    bean.setSource_id(tempJsonNewsSourceObject.getString("id"));
                if(tempJsonNewsSourceObject.has("name"))
                    bean.setSource_name(tempJsonNewsSourceObject.getString("name"));
                if(tempJsonNewsSourceObject.has("category")) {
                    bean.setSource_category(tempJsonNewsSourceObject.getString("category"));
                    listCategory.add(tempJsonNewsSourceObject.getString("category"));
                }
                listSourceBean.add(bean);
            }
            newsSourcesBean.setListSourceBean(listSourceBean);
            newsSourcesBean.setListCategory(listCategory);
            //Log.d(TAG, "parseNewsSource: " + listSourceBean.toString());

            return newsSourcesBean;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<ArticleBean> parseNewsArticles(String articlesJSON){

        List<ArticleBean> listArticleBean = new ArrayList<ArticleBean>();
        JSONObject jsonNewsArticles = new JSONObject();
        JSONArray jsonNewsArticlesArray = new JSONArray();

        try{
            jsonNewsArticles = new JSONObject(articlesJSON);
            jsonNewsArticlesArray = jsonNewsArticles.getJSONArray("articles");
            for(int i = 0 ; i < jsonNewsArticlesArray.length(); i++){
                JSONObject tempJsonArticlesObject = (JSONObject) jsonNewsArticlesArray.get(i);
                ArticleBean bean = new ArticleBean();
                if(tempJsonArticlesObject.has("author"))
                    bean.setArticle_author(tempJsonArticlesObject.getString("author"));
                if(tempJsonArticlesObject.has("title"))
                    bean.setArticle_title(tempJsonArticlesObject.getString("title"));
                if(tempJsonArticlesObject.has("description"))
                    bean.setArticle_description(tempJsonArticlesObject.getString("description"));
                if(tempJsonArticlesObject.has("url"))
                    bean.setArticle_url(tempJsonArticlesObject.getString("url"));
                if(tempJsonArticlesObject.has("urlToImage"))
                    bean.setArticle_urlToImage(tempJsonArticlesObject.getString("urlToImage"));
                if(tempJsonArticlesObject.has("publishedAt"))
                    bean.setArticle_publishedAt(tempJsonArticlesObject.getString("publishedAt"));

                listArticleBean.add(bean);
            }
            //Log.d(TAG, "parseNewsSource: " + listArticleBean.toString());

            return listArticleBean;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
