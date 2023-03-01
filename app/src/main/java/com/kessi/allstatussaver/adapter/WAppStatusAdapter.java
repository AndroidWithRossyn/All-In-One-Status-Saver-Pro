package com.kessi.allstatussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kessi.allstatussaver.PreviewActivity;
import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.model.DataModel;
import com.kessi.allstatussaver.utils.Utils;

import java.util.ArrayList;


public class WAppStatusAdapter extends RecyclerView.Adapter<WAppStatusAdapter.ViewHolder> {
    private Context activity;
    ArrayList<DataModel> mData;
    String folderPath;
    boolean isWApp;

    public WAppStatusAdapter(Context activity, ArrayList<DataModel> jData, boolean isWApp) {
        this.mData = jData;
        this.activity = activity;
        this.isWApp = isWApp;
        folderPath = Utils.downloadWhatsAppDir.getAbsolutePath();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataModel jpast = this.mData.get(position);


        if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
            holder.imagePlayer.setVisibility(View.VISIBLE);
        } else {
            holder.imagePlayer.setVisibility(View.GONE);
        }


        Glide.with(this.activity).load(jpast.getFilePath()).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imagevi);


        holder.downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.copyFileInSavedDir(activity, jpast.getFilePath(), isWApp);
                Toast.makeText(activity, "Saved successfully!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private ImageView imagePlayer;
        private ImageView imagevi, downloadIV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imagevi = ((ImageView) itemView.findViewById(R.id.imageView));
            this.imagePlayer = ((ImageView) itemView.findViewById(R.id.iconplayer));
            this.cardView = ((CardView) itemView.findViewById(R.id.card_view));
            this.cardView.setOnClickListener(this);
            this.downloadIV = itemView.findViewById(R.id.downloadIV);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, PreviewActivity.class);
            intent.putParcelableArrayListExtra("images", mData);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("statusdownload", "status");
            intent.putExtra("isWApp", isWApp);
            intent.putExtra("folderpath", folderPath);
            activity.startActivity(intent);
        }
    }


}
