package com.example.shamim.weather.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shamim.weather.R;
import com.example.shamim.weather.model.Constant;
import com.example.shamim.weather.model.ForecastWeatherResponse;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import static com.squareup.picasso.Picasso.get;

public class ForecastWeatherAdapter extends RecyclerView.Adapter<ForecastWeatherAdapter.ForecastViewHolder> {
    private Context context;
    private List<ForecastWeatherResponse.ListFor>forecastWeatherResponseList;

    public ForecastWeatherAdapter(Context context, List<ForecastWeatherResponse.ListFor> forecastWeatherResponseList) {
        this.context = context;
        this.forecastWeatherResponseList = forecastWeatherResponseList;
    }


    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater=LayoutInflater.from(parent.getContext());
     View view= inflater.inflate(R.layout.forecast_row,parent,false);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        /*long datetime=forecastWeatherResponseList.get(position).getDt();
        Date date=new Date(datetime*1000);*/
        try{
            holder.maxTemTV.setText(String.valueOf(forecastWeatherResponseList.get(position).getMain().getTempMax())+"°");
            holder.minTempTV.setText(String.valueOf(forecastWeatherResponseList.get(position).getMain().getTempMin())+"°");
           /* holder.dateTV.setText(String.valueOf(date));
            holder.dayTV.setText(String.valueOf(forecastWeatherResponseList.get(position).getDtTxt()));*/

            /*textViewDate.setText(Constant.DateTimeFormat.unixToDate(currentWeatherResponse.getDt()));
            textViewDay.setText(Constant.DateTimeFormat.unixToDay(currentWeatherResponse.getDt()));*/

            holder.dayTV.setText(String.valueOf(Constant.DateTimeFormat.unixToDay(forecastWeatherResponseList.get(position).getDt())));
            holder.dateTV.setText(String.valueOf(Constant.DateTimeFormat.unixToDate(forecastWeatherResponseList.get(position).getDt())));


            Uri uri = Uri.parse("http://openweathermap.org/img/w/" + (forecastWeatherResponseList.get(position).getWeather().get(0).getIcon() + ".png"));
            Picasso.get().load(uri).into(holder.image);

        }
        catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return forecastWeatherResponseList.size();
    }


    public class ForecastViewHolder extends RecyclerView.ViewHolder{
        TextView maxTemTV,minTempTV,dateTV,dayTV;
        ImageView image;
        public ForecastViewHolder(View itemView) {
            super(itemView);
            maxTemTV=itemView.findViewById(R.id.forecastTempMax);
            minTempTV=itemView.findViewById(R.id.forecastTempMin);
            dateTV=itemView.findViewById(R.id.forecastDate);
            dayTV=itemView.findViewById(R.id.forecasteDay);
            image=itemView.findViewById(R.id.forestImageIcon);

        }
    }
}
