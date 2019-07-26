package com.example.shamim.weather.fragment;


import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shamim.weather.R;
import com.example.shamim.weather.WeatherServiceAPI;
import com.example.shamim.weather.model.Constant;
import com.example.shamim.weather.model.CurrentWeatherResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.SEARCH_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeather extends Fragment{
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private WeatherServiceAPI weatherServiceAPI;

    private double latitude,longitude;
    private boolean isConvertToCelcius=false;

    private String unit="metric";
    private String city="";
    private TextView textViewTemp, textViewDate, textViewDay, textViewCity,
            textViewWeatherMain,textViewMinValue,textViewMaxValue,textViewHumidity,
            textViewPressure,textViewSunrise,textViewSunset;
    private ImageView imageViewIcon;
    private String name;
    private FusedLocationProviderClient client;

    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);


        textViewTemp = view.findViewById(R.id.textViewTemp);
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewDay = view.findViewById(R.id.textViewDay);
        textViewCity = view.findViewById(R.id.textViewCity);
        textViewWeatherMain = view.findViewById(R.id.textViewWeatherMain);
        textViewMinValue = view.findViewById(R.id.textViewMinValue);
        textViewMaxValue = view.findViewById(R.id.textViewMaxValue);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        textViewPressure = view.findViewById(R.id.textViewPressure);
        textViewSunrise = view.findViewById(R.id.textViewSunrise);
        textViewSunset = view.findViewById(R.id.textViewSunset);


        /*Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        weatherServiceAPI=retrofit.create(WeatherServiceAPI.class);*/

        client = LocationServices.getFusedLocationProviderClient(getActivity());
       // getCurrentWeatherData();
        getDeviceCurrentLocation();

        return view;
    }
 /*   @Override
    public void passData(String name) {
        context=getActivity();
        ((MainActivity)context).passVal(fragmentCommunicator);
        Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
        return;
    }*/
        public void getCurrentWeatherData(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        weatherServiceAPI = retrofit.create(WeatherServiceAPI.class);

        final String api = getString(R.string.weather_api_key);
        String customUrl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s", latitude, longitude, unit, api);

        Call<CurrentWeatherResponse> currentWeatherResponseCall = weatherServiceAPI.getCurrentWeather(customUrl);

        currentWeatherResponseCall.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.code() == 200) {
                    CurrentWeatherResponse currentWeatherResponse = response.body();
                    textViewTemp.setText(String.format("%s°", currentWeatherResponse.getMain().getTemp()));
                    textViewCity.setText(currentWeatherResponse.getName());
                    textViewSunrise.setText(Constant.DateTimeFormat.getTimeFromUnix(currentWeatherResponse.getSys().getSunrise()));
                    textViewSunset.setText(Constant.DateTimeFormat.getTimeFromUnix(currentWeatherResponse.getSys().getSunset()));
                    textViewDate.setText(Constant.DateTimeFormat.unixToDate(currentWeatherResponse.getDt()));
                    textViewDay.setText(Constant.DateTimeFormat.unixToDay(currentWeatherResponse.getDt()));
                    textViewMinValue.setText(String.format("%s°", currentWeatherResponse.getMain().getTempMin()));
                    textViewMaxValue.setText(String.format("%s°", currentWeatherResponse.getMain().getTempMax()));
                    textViewWeatherMain.setText(currentWeatherResponse.getWeather().get(0).getMain());
                    textViewHumidity.setText(String.format(Locale.US, "%d%%", currentWeatherResponse.getMain().getHumidity()));
                    textViewPressure.setText(String.format("%s hPa",currentWeatherResponse.getMain().getPressure()));

                    Uri uri = Uri.parse("http://openweathermap.org/img/w/" + currentWeatherResponse.getWeather().get(0).getIcon() + ".png");
                    Picasso.get().load(uri).into(imageViewIcon);

                } else {
                    Toast.makeText(getActivity(), "Did not get value", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("current", "onFailure: " + t.getMessage());
            }
        });
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==11 && grantResults[0]== PackageManager.PERMISSION_GRANTED);

        getDeviceCurrentLocation();

    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},11);

            return false;
        }
        return true;

    }
    public void getDeviceCurrentLocation() {
        if (checkLocationPermission()) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                   // double latitude,longitude;
                    if (location == null) {
                        return;
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(getActivity(), "Current Lat:"+latitude+"  "+"lon:"+longitude, Toast.LENGTH_SHORT).show();

                    getCurrentWeatherData();


                }
            });

        }
        else {
            checkLocationPermission();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager manager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searching).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Car");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchApi(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    /* SearchManager manager= (SearchManager) (getActivity().getSystemService(Context.SEARCH_SERVICE));
        SearchView searchView=(SearchView)menu.findItem(R.id.searching).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
*/



    private void searchApi(String query) {
        city=query;
        if(city.equals(null)){
            getCurrentWeatherData();
        }
        else{
            getSearchWeatherData();
        }
    }

    private void getSearchWeatherData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        weatherServiceAPI = retrofit.create(WeatherServiceAPI.class);

        final String api = getString(R.string.weather_api_key);

        String customUrl=String.format("weather?q=%s&units=%s&appid=%s",city,unit,api);

        Call<CurrentWeatherResponse> currentWeatherResponseCall = weatherServiceAPI.getCurrentWeather(customUrl);

        currentWeatherResponseCall.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                if (response.code() == 200) {
                    CurrentWeatherResponse currentWeatherResponse = response.body();
                    textViewTemp.setText(String.format("%s℃", currentWeatherResponse.getMain().getTemp()));
                    textViewCity.setText(currentWeatherResponse.getName());
                    textViewSunrise.setText(Constant.DateTimeFormat.getTimeFromUnix(currentWeatherResponse.getSys().getSunrise()));
                    textViewSunset.setText(Constant.DateTimeFormat.getTimeFromUnix(currentWeatherResponse.getSys().getSunset()));
                    textViewDate.setText(Constant.DateTimeFormat.unixToDate(currentWeatherResponse.getDt()));
                    textViewDay.setText(Constant.DateTimeFormat.unixToDay(currentWeatherResponse.getDt()));
                    textViewMinValue.setText(String.format("%s℃", currentWeatherResponse.getMain().getTempMin()));
                    textViewMaxValue.setText(String.format("%s℃", currentWeatherResponse.getMain().getTempMax()));
                    textViewWeatherMain.setText(currentWeatherResponse.getWeather().get(0).getMain());
                    textViewHumidity.setText(String.format(Locale.US, "%d%%", currentWeatherResponse.getMain().getHumidity()));
                    textViewPressure.setText(String.format("%s hPa",currentWeatherResponse.getMain().getPressure()));

                    Uri uri = Uri.parse("http://openweathermap.org/img/w/" + currentWeatherResponse.getWeather().get(0).getIcon() + ".png");
                    Picasso.get().load(uri).into(imageViewIcon);

                } else {
                    Toast.makeText(getActivity(), "Did not get value", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                Log.e("current", "onFailure: " + t.getMessage());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.convertFarenhite:
                   unit="imperial";
                    getDeviceCurrentLocation();
                    break;

                    case  R.id.convertCelcius:
                        unit="metric";
                        getDeviceCurrentLocation();
            }
        return super.onOptionsItemSelected(item);
    }

}
