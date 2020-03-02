package iitmad.com.a20425418.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import iitmad.com.a20425418.newsgateway.bean.ArticleBean;
import iitmad.com.a20425418.newsgateway.bean.NewsSourcesBean;
import iitmad.com.a20425418.newsgateway.utils.AsyncTaskNewsSourceDownloader;
import iitmad.com.a20425418.newsgateway.utils.NewsService;
import iitmad.com.a20425418.newsgateway.bean.SourcesBean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String BROADCAST_FROM_SERVICE = "BROADCAST FROM SERVICE";
    public static final String SERVICE_DATA = "SERVICE_DATA";
    public static final String SERVICE_DATA_MSG = "SERVICE_DATA_MSG";
    public static final String NEWS_SOURCE = "NEWS_SOURCE";
    NewsServiceReceiver newsServiceReceiver;
    AsyncTaskNewsSourceDownloader asyncTaskNewsSourceDownloader;
    private Menu opt_menu;
    ArrayList<String> listNewsSourcesByCategory;
    ArrayList<String> listNewsSourcesByCategoryId;
    NewsSourcesBean newsSourcesBean;
    int drawerItemClickedPosition = 0;
    List<ArticleBean> listArticleBean;
    String selectedNewsSource = "";
    String selectedNewsCategory = "";
    int currentFragmentPosition = 0;
    boolean isOrientationChanged = false;

    private MyPageAdapter pageAdapter; // Adapter to do the paging
    private List<Fragment> fragments = new ArrayList<>(); // A list of fragments to page through
    private ViewPager pager;  // View pager object to display the fragments

    // The below are for the drawer layout
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    Set<String> option_menu_item_set;
    Boolean isFragmentDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            listArticleBean = new ArrayList<ArticleBean>();
            newsServiceReceiver = new NewsServiceReceiver();

            //Executing the News Source Downloader on every launch
            executeNewsSourceAsync();

            // Below sets up the drawer layout
            mDrawerLayout = findViewById(R.id.drawer_layout);
            mDrawerList = findViewById(R.id.left_drawer);

            mDrawerList.setOnItemClickListener(
                    new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            drawerItemClickedPosition = position;
                            currentFragmentPosition = 0;
                            isFragmentDisplayed = true;
                            selectItem(position);
                        }
                    }
            );
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    mDrawerLayout,         /* DrawerLayout object */
                    R.string.strDrawerOpen,  /* "open drawer" description for accessibility */
                    R.string.strDrawerClose  /* "close drawer" description for accessibility */
            );


            // Set up the support action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            // Set up the view pager & fragments
            //fragments = new ArrayList<>(); // Create arraylist to hold fragments
            fragments.clear();
            pageAdapter = new MyPageAdapter(getSupportFragmentManager()); // Create fragment adapter
            pager = findViewById(R.id.viewpager); // Get reference to view pager
            pager.setAdapter(pageAdapter); // Set the view pager's adapter
            pageAdapter.notifyDataSetChanged();
            Log.d(TAG, "onCreate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method is used for fetching the list of news when drawer item i.e. news source is clicked
     *
     * @param position - Position of the drawer Item
     */
    private void selectItem(int position) {
        try {
            pager.setBackground(null);
            Toast.makeText(this, listNewsSourcesByCategory.get(position), Toast.LENGTH_SHORT).show();
            //Log.d(TAG, "selectItem: " + listNewsSourcesByCategoryId.get(position));
            setTitle(listNewsSourcesByCategory.get(position));
            selectedNewsSource = listNewsSourcesByCategory.get(position);
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            intent.putExtra("NEWS_SOURCE", listNewsSourcesByCategoryId.get(position));
            startService(intent);
            mDrawerLayout.closeDrawer(mDrawerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method builds the creating Array List of fragments based on the no of articles fetched for the selected news
     *
     * @param idx
     */
    private void reDoFragments(int idx) {
        // This re-builds a set of fragments once a drawer item is selected
        try {
            pager.setBackground(null);
            // Tell the adapter that all pages (fragments) have changed
            for (int i = 0; i < pageAdapter.getCount(); i++)
                pageAdapter.notifyChangeInPosition(i);


            // Clear the list of fragments (remove the old set of fragments)
            fragments.clear();

            //String src = listNewsSourcesByCategory.get(idx); // Get the selected drawer item

            for (int i = 0; i < listArticleBean.size(); i++) {
                // Build a new fragment passing a counter string to be used in the fragment display
                NewsDetailFragment mf = NewsDetailFragment.newInstance((i + 1) + " of " + listArticleBean.size(), listArticleBean.get(i));
                //Log.d(TAG, "reDoFragments: listArticleBean title" + listArticleBean.get(i).getArticle_title());
                // Add the new fragment to the list of fragments
                fragments.add(mf);
            }

            // Tell the page adapter that the fragment list changed
            pageAdapter.notifyDataSetChanged();

            //Check if orientation is changes, than display what fragment was lastly
            // displayed. else display the 0th position of loaded news source
            if (!(isOrientationChanged)) {
                // set the first fragment in the list to display
                pager.setCurrentItem(0);
            } else {
                pager.setCurrentItem(currentFragmentPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        try {
            mDrawerToggle.syncState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For the drawer
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        try {
            mDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void executeNewsSourceAsync() {
        try {
            asyncTaskNewsSourceDownloader = new AsyncTaskNewsSourceDownloader();
            asyncTaskNewsSourceDownloader.setListener(asyncTaskNewsSourceDownloaderListener);
            asyncTaskNewsSourceDownloader.execute("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopService(View v) {
        try {
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This part is just creating a menu object
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        opt_menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        try {
            // If a drawer item was selected, return true
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            selectedNewsCategory = item.toString();
            Log.d(TAG, "onOptionsItemSelected: " + selectedNewsCategory);

            currentFragmentPosition = 0;
            //Fetching New Sources of selection of category
            fetchNewsSourceOnCategory(item.toString(), newsSourcesBean);
            //Set the default selected item of drawer as 0
            ////////selectItem(0);
            ((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This part is adding menu items based on the list of items(categories) received from the News Sources
     *
     * @param categoryList
     */
    public void setupCategory(Set<String> categoryList) {
        try {

            option_menu_item_set = new TreeSet<String>(categoryList);
            for (String s : option_menu_item_set)
                opt_menu.add(s);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (newsServiceReceiver != null)
                unregisterReceiver(newsServiceReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = null;
        if (option_menu_item_set != null && option_menu_item_set.size() > 0) {
            setupCategory(option_menu_item_set);
        }
        try {
            if (pageAdapter != null)
                pageAdapter.notifyDataSetChanged();
            if (newsServiceReceiver != null)
                filter = new IntentFilter(BROADCAST_FROM_SERVICE);
            registerReceiver(newsServiceReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (newsServiceReceiver != null)
                unregisterReceiver(newsServiceReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (newsServiceReceiver != null)
                unregisterReceiver(newsServiceReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outState.clear();
            //NewsSourcesBean newsSourcesBean;
            outState.putSerializable("listArticleBean", (Serializable) listArticleBean);
            ArrayList<String> option_menu_item_list = new ArrayList<String>(option_menu_item_set);
            outState.putSerializable("option_menu_item_set", option_menu_item_list);
            outState.putInt("drawerItemClickedPosition", drawerItemClickedPosition);
            outState.putSerializable("listNewsSourcesByCategory", listNewsSourcesByCategory);
            outState.putSerializable("listNewsSourcesByCategoryId", listNewsSourcesByCategoryId);
            outState.putSerializable("newsSourcesBean", newsSourcesBean);
            outState.putString("selectedNewsSource", selectedNewsSource);
            outState.putString("selectedNewsCategory", selectedNewsCategory);
            isOrientationChanged = true;
            outState.putBoolean("isOrientationChanged", isOrientationChanged);
            outState.putBoolean("isFragmentDisplayed", isFragmentDisplayed);
            if (pager != null) {
                currentFragmentPosition = pager.getCurrentItem();
                outState.putInt("currentFragmentPosition", currentFragmentPosition);
            }
            Log.d(TAG, "onSaveInstanceState: ");
            invalidateOptionsMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            listArticleBean = (List<ArticleBean>) savedInstanceState.getSerializable("listArticleBean");
            ArrayList<String> option_menu_item_list = new ArrayList<String>();
            option_menu_item_list = (ArrayList<String>) savedInstanceState.getSerializable("option_menu_item_set");
            if (option_menu_item_list != null && option_menu_item_list.size() > 0) {
                option_menu_item_set = new HashSet<String>(option_menu_item_list);
                setupCategory(option_menu_item_set);
            }
            drawerItemClickedPosition = savedInstanceState.getInt("drawerItemClickedPosition");
            listNewsSourcesByCategory = (ArrayList<String>) savedInstanceState.getSerializable("listNewsSourcesByCategory");
            listNewsSourcesByCategoryId = (ArrayList<String>) savedInstanceState.getSerializable("listNewsSourcesByCategoryId");
            newsSourcesBean = (NewsSourcesBean) savedInstanceState.getSerializable("newsSourcesBean");
            selectedNewsSource = savedInstanceState.getString("selectedNewsSource");
            selectedNewsCategory = savedInstanceState.getString("selectedNewsCategory");
            isOrientationChanged = savedInstanceState.getBoolean("isOrientationChanged");
            isFragmentDisplayed = savedInstanceState.getBoolean("isFragmentDisplayed");
            if (selectedNewsCategory != "" && newsSourcesBean != null)
                fetchNewsSourceOnCategory(selectedNewsCategory, newsSourcesBean);
            if (savedInstanceState.containsKey("currentFragmentPosition")) {
                currentFragmentPosition = savedInstanceState.getInt("currentFragmentPosition");
            }
            //if (selectedNewsCategory != "")
            //selectItem(drawerItemClickedPosition);

            if (isFragmentDisplayed)
                reDoFragments(drawerItemClickedPosition);
            //selectItem(drawerItemClickedPosition);

            if ((!selectedNewsSource.equals("")))
                setTitle(selectedNewsSource);


            //((ArrayAdapter) mDrawerList.getAdapter()).notifyDataSetChanged();
            //Log.d(TAG, "onRestoreInstanceState: ");
            //Log.d(TAG, "onRestoreInstanceState: selectedNewsCategory " + selectedNewsCategory);
            //Log.d(TAG, "onRestoreInstanceState: selectedNewsSource " + selectedNewsSource);
            //Log.d(TAG, "onRestoreInstanceState: drawerItemClickedPosition " + drawerItemClickedPosition);
            //Log.d(TAG, "onRestoreInstanceState: currentFragmentPosition " + currentFragmentPosition);
            savedInstanceState.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class NewsServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action == null)
                    return;
                switch (action) {
                    case BROADCAST_FROM_SERVICE:
                        List<ArticleBean> data = new ArrayList<ArticleBean>();
                        if (intent.hasExtra(SERVICE_DATA)) {
                            data = (List<ArticleBean>) intent.getSerializableExtra(SERVICE_DATA);
                            Log.d(TAG, "onReceive: " + data);
                            listArticleBean.clear();
                            listArticleBean = data;
                            reDoFragments(drawerItemClickedPosition); // Re-create fragments when a drawer item is selected
                        }
                        if (intent.hasExtra(SERVICE_DATA_MSG))
                            Log.d(TAG, "onReceive: " + intent.getStringExtra(SERVICE_DATA_MSG));
                        //((TextView) findViewById(R.id.tvSample)).setText(data);`
                        break;
                    default:
                        Log.d(TAG, "onReceive: Unkown broadcast received");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    AsyncTaskNewsSourceDownloader.AsyncTaskNewsSourceDownloaderListener asyncTaskNewsSourceDownloaderListener = new AsyncTaskNewsSourceDownloader.AsyncTaskNewsSourceDownloaderListener() {
        @Override
        public Context onPreDownload() {
            return MainActivity.this;
        }

        @Override
        public void onPostDownload(NewsSourcesBean result) {
            try {
                if (result != null) {
                    newsSourcesBean = result;
                    //Log.d(TAG, "onPostDownload: " + result.getListCategory().toString());
                    setupCategory(result.getListCategory());
                    if (isOrientationChanged) {
                        if (selectedNewsCategory != "")
                            fetchNewsSourceOnCategory(selectedNewsCategory, result);
                        else
                            fetchNewsSourceOnCategory(result.getListCategory().toArray()[0].toString(), result);
                    } else
                        fetchNewsSourceOnCategory(result.getListCategory().toArray()[0].toString(), result);
                    //selectItem(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Check For internet connection. If No internet connection, it will show a dialog
     *
     * @return
     */
    private Boolean doNetCheck() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                //Cannot Access Connectivity Manager
                return false;
            }

            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                //Connected
                return true;
            } else {
                //Not Connected
                //Show Dialog for Not available network
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method id used for fetching the list of news sources based on category
     *
     * @param category
     * @param tempNewsSourcesBean
     * @return
     */
    public void fetchNewsSourceOnCategory(String category, NewsSourcesBean tempNewsSourcesBean) {
        try {
            listNewsSourcesByCategory = new ArrayList<>();
            listNewsSourcesByCategoryId = new ArrayList<>();
            //Log.d(TAG, "fetchNewsSourceOnCategory: " + category);
            List<SourcesBean> tempSourceBeanList = tempNewsSourcesBean.getListSourceBean();
            for (int i = 0; i < tempSourceBeanList.size(); i++) {
                if (category.trim().equals("all")) {
                    listNewsSourcesByCategoryId.add(tempSourceBeanList.get(i).getSource_id().trim());
                    listNewsSourcesByCategory.add(tempSourceBeanList.get(i).getSource_name().trim());
                } else {
                    if (tempSourceBeanList.get(i).getSource_category().trim().equals(category.trim())) {
                        listNewsSourcesByCategoryId.add(tempSourceBeanList.get(i).getSource_id().trim());
                        listNewsSourcesByCategory.add(tempSourceBeanList.get(i).getSource_name().trim());
                    }
                }
            }
            Log.d(TAG, "fetchNewsSourceOnCategory: " + listNewsSourcesByCategory);
            mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, listNewsSourcesByCategory));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //********************************************************************************//

    // Adapter that accesses the fragments based upon view pager activity
    private class MyPageAdapter extends FragmentPagerAdapter {

        private long baseId = 0;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            fm.popBackStack();
            fm.popBackStackImmediate();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            // Return the fragment at the specified position
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // REturn the current number of fragments in the list
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         *
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }
}
