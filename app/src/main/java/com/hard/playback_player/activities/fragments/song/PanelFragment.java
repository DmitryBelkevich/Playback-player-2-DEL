package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.hard.playback_player.R;
import com.hard.playback_player.activities.SongActivity;
import com.hard.playback_player.models.Song;

public class PanelFragment extends Fragment {
    public static final String TAG = "PanelFragment";
    private static final String SONG = "song";

    private Song song;

    private PlayerFragment playerFragment;

    private Button play_button;
    private Button stop_button;

    public PanelFragment() {

    }

    public static PanelFragment newInstance(Song song) {
        PanelFragment fragment = new PanelFragment();

        Bundle args = new Bundle();

        args.putSerializable(SONG, song);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            song = (Song) getArguments().getSerializable(SONG);
        }

        SongActivity songActivity = (SongActivity) getActivity();
        playerFragment = songActivity.getPlayerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel, container, false);

        play_button = view.findViewById(R.id.play_button);
        stop_button = view.findViewById(R.id.stop_button);

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerFragment.getPlayer() == null) {
                    playerFragment.load();
                    playerFragment.start();
                } else {
                    if (playerFragment.getPlayer().isPlaying())
                        playerFragment.pause();
                    else
                        playerFragment.start();
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerFragment.stop();
            }
        });

        load();

        return view;
    }

    private void load() {
        if (song.getOriginal() != null || song.getPlaybacks() != null && !song.getPlaybacks().isEmpty())
            setEnabled(true);
    }

    public void setPlaying() {
        play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_24, 0, 0, 0);
    }

    public void setPaused() {
        play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_circle_outline_24, 0, 0, 0);
    }

    public void setStopped() {
        play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_arrow_24, 0, 0, 0);
    }

    public void setEnabled(boolean enabled) {
        play_button.setEnabled(enabled);
        stop_button.setEnabled(enabled);
    }
}