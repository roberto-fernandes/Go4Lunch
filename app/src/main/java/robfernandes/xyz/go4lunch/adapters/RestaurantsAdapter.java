package robfernandes.xyz.go4lunch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import robfernandes.xyz.go4lunch.R;
import robfernandes.xyz.go4lunch.model.RestaurantInfo;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;
import static robfernandes.xyz.go4lunch.utils.Utils.putImageIntoImageView;

public class RestaurantsAdapter extends
        RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private List<RestaurantInfo> restaurantInfoList;
    private LatLng userLatLng ;
    private Context context;

    public RestaurantsAdapter(List<RestaurantInfo> restaurantInfoList, LatLng userLatLng, Context context) {
        this.restaurantInfoList = restaurantInfoList;
        this.userLatLng = userLatLng;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_item, viewGroup, false);
        return new RestaurantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RestaurantInfo restaurantInfo = restaurantInfoList.get(i);
        viewHolder.title.setText(restaurantInfo.getName());
        viewHolder.description.setText(restaurantInfo.getAdress());
        try {
            if (restaurantInfo.getOpeningHours().getOpenNow()) {
                viewHolder.openHours.setText("Open now");
                viewHolder.openHours.setTextColor(Color.GREEN);
            } else {
                viewHolder.openHours.setText("Closed");
                viewHolder.openHours.setTextColor(Color.RED);
            }
        } catch (NullPointerException e) {
        }

        LatLng restaurantLatLng = new LatLng(restaurantInfo.getLat(), restaurantInfo.getLon());

        String distanceString = ((int) computeDistanceBetween(userLatLng, restaurantLatLng)) + "m";

        viewHolder.distance.setText(distanceString);
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
            sb.append("?maxwidth=100");
            sb.append("&maxheight=100");
            sb.append("&photoreference=");
            sb.append(restaurantInfo.getPhotos().get(0).getPhotoReference());
            sb.append("&key=");
            sb.append(context.getString(R.string.google_maps_api_key));

            String photoUrl = sb.toString();
            putImageIntoImageView(viewHolder.imageView, photoUrl);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return restaurantInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, openHours, distance;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.restaurant_item_title);
            description = itemView.findViewById(R.id.restaurant_item_description);
            openHours = itemView.findViewById(R.id.restaurant_item_open_hours);
            distance = itemView.findViewById(R.id.restaurant_item_distance);
            imageView = itemView.findViewById(R.id.restaurant_item_image);
        }
    }
}
