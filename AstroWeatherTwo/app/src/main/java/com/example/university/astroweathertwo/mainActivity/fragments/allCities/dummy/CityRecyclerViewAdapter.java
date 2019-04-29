package com.example.university.astroweathertwo.mainActivity.fragments.allCities.dummy;

import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.mainActivity.fragments.allCities.dummy.CitiesListFragment.OnListFragmentInteractionListener;
import com.example.university.astroweathertwo.utilities.database.SQLiteDatabaseHelper;
import com.example.university.astroweathertwo.utilities.database.entities.City;

import java.util.List;

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder> {

    private List<City> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CityRecyclerViewAdapter(List<City> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        for(int i = 0 ; i < 50 ; i ++) {
            Log.e("witam", mValues.get(position).toString());
        }

        holder.mItem = mValues.get(position);
        holder.cityNameTextView.setText(mValues.get(position).getName());
        holder.cityCountryCodeTextView.setText(mValues.get(position).getCountryCode());
        holder.cityWoeidCodeTextView.setText(mValues.get(position).getWoeid());

        //TODO: figure this out
        //holder.cityDatabaseIdTextView.setText(mValues.get(position).getId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView cityNameTextView;
        public final TextView cityCountryCodeTextView;
        public final TextView cityDatabaseIdTextView;
        public final TextView cityWoeidCodeTextView;
        public final Button btnDeleteFavouriteCity;
        public City mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cityNameTextView = view.findViewById(R.id.city_name_text_view);
            cityCountryCodeTextView = view.findViewById(R.id.city_country_code_text_view);
            cityDatabaseIdTextView = view.findViewById(R.id.city_database_id_text_view);
            cityWoeidCodeTextView = view.findViewById(R.id.city_woeid_code_text_view);
            btnDeleteFavouriteCity = view.findViewById(R.id.btn_delete_city);

            btnDeleteFavouriteCity.setOnClickListener(v -> {
                SQLiteDatabaseHelper sqLiteDatabaseHelper = SQLiteDatabaseHelper.getInstance(view.getContext());

                sqLiteDatabaseHelper.deleteOne(mItem);
                mValues.remove(mItem);

                CityRecyclerViewAdapter.this.notifyDataSetChanged();
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + cityCountryCodeTextView.getText() + "'";
        }
    }
}
