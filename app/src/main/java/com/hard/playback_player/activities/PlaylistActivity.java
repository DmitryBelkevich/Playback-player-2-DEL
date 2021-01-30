package com.hard.playback_player.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.services.SongService;

import java.util.Collection;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView playlist_listView;

    private SongService songService;

    private ArrayAdapter<Song> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // day/night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Toolbar >
        Toolbar toolbar = findViewById(R.id.playlist_toolbar);
        setSupportActionBar(toolbar);
        // Toolbar <

        // listView >
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        playlist_listView = findViewById(R.id.playlist_listView);

        swipeRefreshLayout.setOnRefreshListener(this);
        playlist_listView.setOnItemClickListener(this);
        // listView <

        songService = new SongService();

        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        new Thread(() -> {
            Collection<Song> songs = songService.getAll();

            runOnUiThread(() -> {
                // setAdapter >
                arrayAdapter = new PlaylistAdapter(this, R.layout.playlist_item, (List<Song>) songs);
                playlist_listView.setAdapter(arrayAdapter);
                // setAdapter <

                swipeRefreshLayout.setRefreshing(false);
            });
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PlaylistActivity.this, SongActivity.class);

        Song song = arrayAdapter.getItem(position);

        intent.putExtra("song", song);

        startActivity(intent);
    }

    private class PlaylistAdapter extends ArrayAdapter<Song> {
        private Context context;
        private int resource;

        public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Song> objects) {
            super(context, resource, objects);

            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(resource, parent, false);

            Song song = getItem(position);

            // title

            TextView songTitle_textView = convertView.findViewById(R.id.songTitle_playlist);
            songTitle_textView.setText(song.getTitle());

            TextView bandTitle_textView = convertView.findViewById(R.id.bandTitle_playlist);
            bandTitle_textView.setText(song.getBand().getTitle());

            // icons

            if (song.getText() != null) {
                ImageView textIcon = convertView.findViewById(R.id.text_icon_playlist);
                textIcon.setVisibility(View.VISIBLE);
            }

            if (song.getScores() != null && !song.getScores().isEmpty()) {
                ImageView scoreIcon = convertView.findViewById(R.id.score_icon_playlist);
                scoreIcon.setVisibility(View.VISIBLE);
            }

            if (song.getPlaybacks() != null && !song.getPlaybacks().isEmpty()) {
                ImageView playbackIcon = convertView.findViewById(R.id.playback_icon_playlist);
                playbackIcon.setVisibility(View.VISIBLE);
            }

            if (song.getOriginal() != null) {
                ImageView originalIcon = convertView.findViewById(R.id.original_icon_playlist);
                originalIcon.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }
}