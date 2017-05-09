package com.example.txtledbluetooth.music.playing.view;

import android.graphics.drawable.Drawable;

/**
 * Created by KomoriWu
 * on 2017-05-09.
 */

public interface PlayingView {

    void startAnim();

    void stopAnim();

    void showGSAlbumCover(Drawable drawable);
}
