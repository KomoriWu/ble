package com.example.txtledbluetooth.music.playing.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.txtledbluetooth.music.playing.PlayingActivity;
import com.example.txtledbluetooth.utils.GaussianBlurUtil;
import com.example.txtledbluetooth.utils.MusicUtils;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public class PlayingModelImpl implements PlayingModel {

    @Override
    public void loadGSAlbumCover(final String albumUri, final Context context,
                                 final OnLoadListener onLoadListener) {
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... voids) {
                Drawable drawable = GaussianBlurUtil.BoxBlurFilter(MusicUtils.createThumbFromUir(
                        context, Uri.parse(albumUri)));
                return drawable;
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                onLoadListener.success(drawable);
            }
        }.execute();
    }

    public interface OnLoadListener {
        void success(Drawable drawable);
    }
}
