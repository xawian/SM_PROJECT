package com.example.newsapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.newsapp.Models.Article;
import com.example.newsapp.Models.News;
import com.example.newsapp.AdapterHolder.NewsAdapter;
import com.example.newsapp.Interfaces.NewsClickListener;
import com.example.newsapp.API.NewsService;
import com.example.newsapp.R;
import com.example.newsapp.API.RetrofitManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private ArrayList<Article> articleList = new ArrayList<>() ;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private String searchText;
    private SearchView searchView;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    private final String[] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
    Button button;
    String country;
    FusedLocationProviderClient fusedLocationProviderClient;
    private AutoCompleteTextView autoCompleteTextView;
    private final static int REQUEST_CODE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("tag","ONCREATE");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        autoCompleteTextView = view.findViewById(R.id.auto_Complete_View);
        recyclerView = view.findViewById(R.id.recyclerview);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 5) {
                    Toast.makeText(getActivity(), getString(R.string.shake_message), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (savedInstanceState != null) {
            Log.d("tag","ZAPISANE");
            articleList = savedInstanceState.getParcelableArrayList("array");
            Log.d("tag","ZAPISANA LISTA" + articleList);
            setupArticleListView(articleList);
            searchText = savedInstanceState.getString("query");
            Log.d("tag","searchetxt"+searchText);
            if (searchText != null) {
                Log.d("tag", "TEXT: " + searchText);
                titleSearch(searchText);
            }
        } else {
            Log.d("tag","NieZapisane");
            fetchNewsData(null,"general", null);
        }
        button = view.findViewById(R.id.button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "kliknieto");
                getLocation();

            }

        });
        dmInitialization(country);
        return view;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("array", articleList);
        if(!searchView.isIconified()){
            outState.putString("query",searchView.getQuery().toString());
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.news_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        if(searchText != null && !searchText.isEmpty()){
            searchView.setIconified(false);
            searchItem.expandActionView();
            searchView.setQuery(searchText,true);
            searchView.clearFocus();
        }
        searchView.setQueryHint("Search...");

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                titleSearch(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupArticleListView(ArrayList<Article> list) {
        recyclerView.setHasFixedSize(true);
        articleList = list;
        newsAdapter = new NewsAdapter(list, new NewsClickListener() {
            @Override
            public void onNewsClick(Article article) {
                Fragment fragment = DetailsFragment.newInstance(article.getTitle(),article.getAuthor(),article.getDescription(),article.getPublishedAt(),article.getContent(),article.getUrlToImage());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(getActivity().getSupportFragmentManager().findFragmentByTag("main_fragment"));
                fragmentTransaction.add(R.id.main_fragment_container,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newsAdapter);

    }
   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("tag","ONCREATEDview");




    }*/

    public void fetchNewsData(String country, String category, String query) {
        NewsService newsService = RetrofitManager.getRetrofitManager().create(NewsService.class);
        Call<News> newsApiCall;
        if(country == null)
            newsApiCall = newsService.findNews("us",category,query, getActivity().getString(R.string.api_key));
        else{
            newsApiCall = newsService.findNews(country,category,query,  getActivity().getString(R.string.api_key));
        }
        newsApiCall.enqueue(new Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.body() != null) {
                   setupArticleListView(response.body().getArticle());
                    Log.d("tag", "pobranie listy ");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Log.d("tag", "uzyt");
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            Log.d("tag", "uzyte");
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            country = addresses.get(0).getCountryCode().toLowerCase();
                            fetchNewsData(country, "general", null);
                            dmInitialization(country);
                            Snackbar.make(getActivity().findViewById(R.id.main_view), "Country: "+addresses.get(0).getCountryName(),
                                    BaseTransientBottomBar.LENGTH_LONG).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }
        else{
            Log.d("tag", "uzyteeeeeee");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

    }

    private void dmInitialization(String country){

        ArrayAdapter<String> adapterCategorie = new ArrayAdapter<>(getActivity(), R.layout.category_list_item, categories);
        autoCompleteTextView.setAdapter(adapterCategorie);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                    fetchNewsData(country,category,null);
                Log.d("tag","country"+country);

            }
        });
    }

    public void titleSearch(String text){
        ArrayList<Article> filteredList = new ArrayList<>();
        Log.d("tag","przekazany tekst:" + text);
        Log.d("tag","tablica"+articleList);
        for(Article item: articleList){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase()))
                filteredList.add(item);
        }
        Log.d("tag","list"+filteredList);
        newsAdapter.filteredList(filteredList);

    }

}