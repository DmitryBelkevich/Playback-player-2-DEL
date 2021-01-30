package com.hard.playback_player.activities.fragments.song;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;
import com.hard.playback_player.settings.Settings;
import com.hard.playback_player.utils.TimeFormat;

import java.io.IOException;

public class PlayerFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;
    private PanelFragment panelFragment;

    private SeekBar track_seekBar;
    private TextView totalTime_textView;
    private TextView elapsedTime_textView;
    private TextView remainingTime_textView;

    private SeekBar volume_seekBar;
    private TextView volume_textView;

    private MediaPlayer player;

    public PlayerFragment() {

    }

    public static PlayerFragment newInstance(String title, Song song) {
        PlayerFragment fragment = new PlayerFragment();

        Bundle args = new Bundle();

        args.putString(TITLE, title);
        args.putSerializable(SONG, song);

        fragment.setArguments(args);

        return fragment;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            song = (Song) getArguments().getSerializable(SONG);
        }

        panelFragment = (PanelFragment) getActivity().getSupportFragmentManager().findFragmentByTag(PanelFragment.TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        track_seekBar = view.findViewById(R.id.track_seekBar);
        totalTime_textView = view.findViewById(R.id.totalTime_textView);
        elapsedTime_textView = view.findViewById(R.id.elapsedTime_textView);
        remainingTime_textView = view.findViewById(R.id.remainingTime_textView);

        volume_seekBar = view.findViewById(R.id.volume_seekBar);
        volume_textView = view.findViewById(R.id.volume_textView);

        track_seekBar.setEnabled(false);

        // init volume >
        volume_seekBar.setProgress(Settings.getVolume());
        volume_textView.setText(String.valueOf(Settings.getVolume()));
        // init volume <

        track_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player == null)
                    return;

                if (fromUser) {
                    int totalTime = player.getDuration();

                    int mills = (int) (totalTime * (progress / 100f));

                    player.seekTo(mills);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volume_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.setVolume(progress);

                volume_textView.setText(String.valueOf(progress));

                if (player == null)
                    return;

                if (fromUser) {
                    float vol = progress / 100f;

                    player.setVolume(vol, vol);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        load();

        return view;
    }

    /**
     * load
     */

    public void load() {
        String url = Constants.GOOGLE_DRIVE_FILE + song.getCurrentPlayback();

        player = new MediaPlayer();

        try {
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // volume

        int volume = Settings.getVolume();

        float vol = volume / 100f;

        player.setVolume(vol, vol);

        volume_textView.setText(String.valueOf(Settings.getVolume()));
        volume_seekBar.setProgress(Settings.getVolume());

        // track

        player.setOnCompletionListener((player) -> {
            stop();
        });

        // run

        if (player.getDuration() <= 0)
            return;

        setEnabled(true);

        new Thread(() -> {
            int totalTime = player.getDuration();
            updateTotalTime(totalTime);

            int currentTime = player.getCurrentPosition();
            updateElapsedTimer(currentTime);
            updateRemainingTime(totalTime - currentTime);

            while (true) {
                if (player == null)
                    break;

                currentTime = player.getCurrentPosition();
                int progress = (int) (currentTime * 100f / totalTime);

                if (0 > progress || progress > 100)
                    break;

                updateProgress(progress);
                updateElapsedTimer(currentTime);
                updateRemainingTime(totalTime - currentTime);

//                while (!player.isPlaying()) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateProgress(int progress) {
        track_seekBar.setProgress(progress);
    }

    private void updateTotalTime(long mills) {
        String time = TimeFormat.format(mills);

        getActivity().runOnUiThread(() -> {
            totalTime_textView.setText(time);
        });
    }

    private void updateElapsedTimer(long mills) {
        String time = TimeFormat.format(mills);

        getActivity().runOnUiThread(() -> {
            elapsedTime_textView.setText(time);
        });
    }

    private void updateRemainingTime(long mills) {
        String time = TimeFormat.format(mills);

        getActivity().runOnUiThread(() -> {
            remainingTime_textView.setText("-" + time);
        });
    }

    private void setEnabled(boolean enabled) {
        track_seekBar.setEnabled(enabled);
        totalTime_textView.setEnabled(enabled);
        elapsedTime_textView.setEnabled(enabled);
        remainingTime_textView.setEnabled(enabled);
    }

    /**
     * Player
     */

    public void start() {
        player.start();
        panelFragment.setPlaying();
    }

    public void pause() {
        player.pause();
        panelFragment.setPaused();
    }

    public void stop() {
        if (player == null)
            return;

//        player.reset();
//        player.prepare();
        player.stop();
        player.release();
        player = null;

        panelFragment.setStopped();

        // reset
        updateProgress(0);
        updateTotalTime(0);
        updateElapsedTimer(0);
        updateRemainingTime(0);

        setEnabled(false);

        // message

        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}