package com.example.android.sunshine;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by S_efr on 1/15/2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final ForecastAdapterOnClickHnadler mClickHandler;
    private static final String TAG = ForecastAdapter.class.getSimpleName();
    private String[] mWeatherData;

    public interface ForecastAdapterOnClickHnadler {
        void onClick(String dayWeather);
    }

    public ForecastAdapter(ForecastAdapterOnClickHnadler clickHandler) {
        mClickHandler = clickHandler;
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mWeatherTextView;
        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param dayWeather of this day
         */
        void bind(String  dayWeather) {
            mWeatherTextView.setText(dayWeather);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String dayWeather = mWeatherData[clickedPosition];
            mClickHandler.onClick(dayWeather);
        }
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ForecastAdapterViewHolder viewHolder = new ForecastAdapterViewHolder(view);
        return viewHolder;
    }

    /**
     * Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null.
     * @return
     */
    @Override
    public int getItemCount() {
        if(mWeatherData == null)
            return 0;
        else
            return mWeatherData.length;
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        Log.d(TAG, "Position #: " + position + " / " + "Data: " + mWeatherData[position]);
        holder.bind(mWeatherData[position]);
    }

    public void setmWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
