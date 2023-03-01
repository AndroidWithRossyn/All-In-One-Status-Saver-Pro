package com.kessi.allstatussaver.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kessi.allstatussaver.PreviewActivity;
import com.kessi.allstatussaver.R;
import com.kessi.allstatussaver.model.DataModel;
import com.kessi.allstatussaver.utils.Utils;
import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;


public class WABusStatusAdapter extends RecyclerView.Adapter<WABusStatusAdapter.ViewHolder> {
    private Context activity;
    private File file;
    ArrayList<DataModel> mData;
    String folderPath;
    public WABusStatusAdapter(Context activity, ArrayList<DataModel> jData) {
        this.mData = jData;
        this.activity = activity;
        folderPath = Utils.downloadWABusiDir.getAbsolutePath();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DataModel jpast = this.mData.get(position);
        this.file = new File(jpast.getFilePath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack(jpast.getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imagevi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.imagePlayer.setVisibility(View.VISIBLE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.3ga|\\.aac|\\.aif|\\.aifc|\\.aiff|\\.amr|\\.au|\\.aup|\\.caf|\\.flac|\\.gsm|\\.kar|\\.m4a|\\.m4p|\\.m4r|\\.mid|\\.midi|\\.mmf|\\.mp2|\\.mp3|\\.mpga|\\.ogg|\\.oma|\\.opus|\\.qcp|\\.ra|\\.ram|\\.wav|\\.wma|\\.xspf)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
            } else if (!Utils.getBack(jpast.getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                holder.imagePlayer.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.file).apply(new RequestOptions().placeholder(R.color.black).error(android.R.color.black).optionalTransform(new RoundedCorners(5))).into(holder.imagevi);
            }

            holder.downloadIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savemedia(jpast.getFilePath(),activity);
                }
            });
        }
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
            intent.putExtra("folderpath", folderPath);
            activity.startActivity(intent);
        }
    }

    void savemedia(String mPath, Context activity) {
        final Storage storage = new Storage(activity);
        String path;
        try {
            path = Utils.downloadWABusiDir.getAbsolutePath();
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            storage.copy(mPath, path + File.separator + new File(mPath).getName());
            Toast.makeText(activity, "Saved successfully!", Toast.LENGTH_LONG).show();
            String fileType = "image/*";
            if (new File(mPath).getPath().endsWith(".mp4")){
                fileType = "video/*";
            }else {
                fileType = "image/*";
            }
            MediaScannerConnection.scanFile(activity, new String[]{path + File.separator + new File(mPath).getName()}, new String[]{fileType},
                    new MediaScannerConnection.MediaScannerConnectionClient() {
                        public void onMediaScannerConnected() {
                        }

                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(activity, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
        }
    }
}
