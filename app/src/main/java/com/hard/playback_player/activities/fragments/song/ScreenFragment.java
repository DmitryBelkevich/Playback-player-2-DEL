package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hard.playback_player.R;
import com.hard.playback_player.activities.SongActivity;
import com.hard.playback_player.models.Song;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ScreenFragment extends Fragment {
    private static final String SONG = "song";

    private static final int LENGTH = 30;

    private Song song;

    private ScoreFragment scoreFragment;

    private LinearLayout screen_layout;

    private TextView score_textView;
    private TextView keySignature_textView;
    private TextView playback_textView;

    public ScreenFragment() {

    }

    public static ScreenFragment newInstance(Song song) {
        ScreenFragment fragment = new ScreenFragment();

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
        scoreFragment = songActivity.getScoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen, container, false);

        screen_layout = view.findViewById(R.id.screen_layout);
        score_textView = view.findViewById(R.id.score_textView);
        keySignature_textView = view.findViewById(R.id.keySignature_textView);
        playback_textView = view.findViewById(R.id.playback_textView);

        screen_layout.setOnClickListener((v) -> {
            registerForContextMenu(v);
            getActivity().openContextMenu(v);
        });

        load();

        return view;
    }

    private void load() {
        clear();

        if (!song.getScores().isEmpty())
            setScore(song.getScore());

        if (song.getKeySignature() != null)
            setKeySignature(song.getKeySignature());

        if (song.getOriginal() != null && song.isOriginalPlaying())
            setPlayback("Original");
        else if (!song.isOriginalPlaying())
            setPlayback((song.getTransposition() > 0 ? "+" : "") + song.getTransposition());
    }

    private void setScore(String message) {
        score_textView.setText(message);
        score_textView.setEnabled(true);
    }

    private void setKeySignature(String message) {
        keySignature_textView.setText(message);
        keySignature_textView.setVisibility(View.VISIBLE);
    }

    private void setPlayback(String message) {
        playback_textView.setText(message);
        playback_textView.setEnabled(true);
    }

    private void clearScore() {
        score_textView.setText("No Scores");
        score_textView.setEnabled(false);
    }

    private void clearKeySignature() {
        keySignature_textView.setText("");
        keySignature_textView.setVisibility(View.GONE);
    }

    private void clearPlayback() {
        playback_textView.setText("No Playbacks");
        playback_textView.setEnabled(false);
    }

    private void clear() {
        clearScore();
        clearKeySignature();
        clearPlayback();
    }

    /**
     * Menu
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        int id = 0;

        // separator

        Map<String, String> scores = song.getScores();
        if (!scores.isEmpty()) {
            menu.add(0, id, id, "----- Scores -----");
            menu.getItem(id).setEnabled(false);

            id++;
        }

        // scores
        Set<String> scoresTitles = scores.keySet();

        Iterator<String> scoreIterator = scoresTitles.iterator();

        while (scoreIterator.hasNext()) {
            String scoreTitle = scoreIterator.next();

            menu.add(1, id, id, scoreTitle);

            MenuItem item = menu.getItem(id);

            if (scoreTitle.equals(song.getScore())) {
                item.setChecked(true);

                String message = scoreTitle.length() > LENGTH ? scoreTitle.substring(0, LENGTH) : scoreTitle;
                setScore(message);
            }

            id++;
        }

        menu.setGroupCheckable(1, true, true);

        // separator

        if (song.getOriginal() != null || !song.getPlaybacks().isEmpty()) {
            menu.add(0, id, id, "----- Playbacks -----");
            menu.getItem(id).setEnabled(false);

            id++;
        }

        // original

        int groupId = 2;

        if (song.getOriginal() != null) {
            String title = "Original" + (song.getKeySignature() != null ? " (" + song.getKeySignature() + ")" : "");
            menu.add(groupId, id, id, title);

            if (song.isOriginalPlaying()) {
                MenuItem item = menu.getItem(id);

                item.setChecked(true);
                setPlayback("Original");
            }

            id++;
        }

        // playbacks

        Map<Byte, String> playbacks = song.getPlaybacks();

        Set<Byte> transpositions = playbacks.keySet();

        Iterator<Byte> playbackIterator = transpositions.iterator();

        while (playbackIterator.hasNext()) {
            byte transposition = playbackIterator.next();

            String title = (transposition > 0 ? "+" : "") + transposition;
            menu.add(groupId, id, id, title);

            MenuItem item = menu.getItem(id);

            if (!song.isOriginalPlaying() && transposition == song.getTransposition()) {
                item.setChecked(true);

                String message = (song.getTransposition() > 0 ? "+" : "") + song.getTransposition();
                setPlayback(message);
            }

            id++;
        }

        menu.setGroupCheckable(groupId, true, true);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.isChecked())
            return super.onOptionsItemSelected(item);

        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                break;
            default:
                int groupId = item.getGroupId();

                int offset = 1;
                switch (groupId) {
                    case 1:
                        String scoreTitle = new ArrayList<>(song.getScores().keySet()).get(itemId - offset);
                        song.setScore(scoreTitle);

                        item.setChecked(true);

                        scoreFragment.load();

                        String message = scoreTitle.length() > LENGTH ? scoreTitle.substring(0, LENGTH) : scoreTitle;
                        setScore(message);
                        break;
                    case 2:
                        Map playbacks = new LinkedHashMap();

                        // original

                        if (song.getOriginal() != null)
                            playbacks.put("Original", song.getOriginal());

                        // playbacks

                        playbacks.putAll(song.getPlaybacks());

                        offset = song.getScores().size() + 1;

                        if (!song.getScores().isEmpty())
                            offset += 1;

                        Object playback = new ArrayList<>(playbacks.keySet()).get(itemId - offset);

                        message = null;
                        if (playback instanceof String) {
                            song.setOriginalPlaying(true);
                            message = (String) playback;
                        } else if (playback instanceof Byte) {
                            song.setOriginalPlaying(false);
                            song.setTransposition((byte) playback);
                            message = ((byte) playback > 0 ? "+" : "") + playback;
                        }

                        item.setChecked(true);

                        setPlayback(message);

                        break;
                }
        }

        return super.onContextItemSelected(item);
    }
}