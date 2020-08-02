package com.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.R;
import com.project.models.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>  {

    Context context;
    List<ImageModel> items;

    public PhotoAdapter(Context context, ArrayList<ImageModel> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_photo, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Data model for every instance
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView previewPhoto;
        public TextView username, description;

        public ViewHolder(View itemView) {
            super(itemView);
            previewPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
            username = (TextView) itemView.findViewById(R.id.tx_username_gotten);
            description = (TextView) itemView.findViewById(R.id.tx_description_gotten);
        }

        public void bind(ImageModel model){
            byte[] decodedString = Base64.decode(model.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(context).load(decodedByte).into(previewPhoto);

            username.setText("@" + model.getUsername());
            description.setText(model.getDescription());
        }

    }
}
