package com.pradeep.app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserListAdaptor extends RecyclerView.Adapter<UserListAdaptor.ViewHolder>{
    private List <User> listdata;
    private List <AUser> listRemoteData;
    private static int mType;
    private ClickListioner mListioner;
    public UserListAdaptor(int type, List listdata,ClickListioner listioner) {
        if(type == 0) {
            this.listdata = listdata;
        } else{
            this.listRemoteData = listdata;
        }
        this.mType = type;
        this.mListioner = listioner;
    }

    public void changeList(int type,List list){
        if(type == 0) {
            this.listdata = list;
        } else{
            this.listRemoteData = list;
        }
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(mType == 0) {
            final View listItem = layoutInflater.inflate(R.layout.user_local_list, parent, false);
            final ViewHolder viewHolder = new ViewHolder(listItem);
            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListioner.onItemClick(listItem,listdata,viewHolder.getAdapterPosition(),0);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListioner.onItemClick(listItem,listdata,viewHolder.getAdapterPosition(),1);
                }
            });
            return viewHolder;
        } else {
            View listItem = layoutInflater.inflate(R.layout.user_remote_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mType == 1) {
            AUser data = listRemoteData.get(position);
            holder.firstName.setText(data.getFirst_name());
            holder.lastName.setText(data.getLast_name());
            holder.email.setText(data.getEmail());
            new ImageLoadTask(data.getAvatar(),holder.imageView).execute();
        } else {
            User data = listdata.get(position);
            holder.firstName.setText(data.getFirstName());
            holder.lastName.setText(data.getLastName());
            holder.email.setText(data.getEmailId());
        }
    }


    @Override
    public int getItemCount() {
        if(mType == 0) {
            return listdata.size();
        } else {
            return listRemoteData.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView firstName;
        public TextView lastName;
        public TextView email;
        public Button edit,delete;
        public ViewHolder(View itemView) {
            super(itemView);
            if(mType == 0) {
                edit = (Button)itemView.findViewById(R.id.edit);
                delete = (Button)itemView.findViewById(R.id.delete);
            } else {
                this.imageView = (ImageView) itemView.findViewById(R.id.image_view);
            }
            this.firstName = (TextView) itemView.findViewById(R.id.firstName);
            this.lastName = (TextView) itemView.findViewById(R.id.lastName);
            this.email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
