package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.services.FileService;
import com.hard.playback_player.settings.Constants;

public class TextFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;

    private TextView text_textView;

    public TextFragment() {

    }

    public static TextFragment newInstance(String title, Song song) {
        TextFragment fragment = new TextFragment();

        Bundle args = new Bundle();

        args.putString(TITLE, title);
        args.putSerializable(SONG, song);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            song = (Song) getArguments().getSerializable(SONG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        text_textView = view.findViewById(R.id.text_textView);

        load();

        return view;
    }

    /**
     * load
     */

    private void load() {
        if (song.getText() == null)
            return;

        FileService fileService = new FileService();
        String url = Constants.GOOGLE_DRIVE_FILE + song.getText();

        new Thread(() -> {
            String text = fileService.read(url);

            getActivity().runOnUiThread(() -> {
                text_textView.setText(text);
            });
        }).start();
    }
}