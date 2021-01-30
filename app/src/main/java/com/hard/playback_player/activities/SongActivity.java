package com.hard.playback_player.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hard.playback_player.R;
import com.hard.playback_player.activities.fragments.song.PlayerFragment;
import com.hard.playback_player.activities.fragments.song.ScoreFragment;
import com.hard.playback_player.activities.fragments.song.ScreenFragment;
import com.hard.playback_player.activities.fragments.song.TextFragment;
import com.hard.playback_player.activities.fragments.song.PanelFragment;
import com.hard.playback_player.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {
    private Song song;

    private ScreenFragment screenFragment;
    private PanelFragment panelFragment;
    private TextFragment textFragment;
    private ScoreFragment scoreFragment;
    private PlayerFragment playerFragment;

    private ViewPagerAdapter viewPagerAdapter;

    public ScoreFragment getScoreFragment() {
        return scoreFragment;
    }

    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Toolbar >
        Toolbar toolbar = findViewById(R.id.song_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Toolbar <

        song = (Song) getIntent().getSerializableExtra("song");

        // Fragments >
        screenFragment = ScreenFragment.newInstance(song);
        panelFragment = PanelFragment.newInstance(song);

        if (song.getText() != null)
            textFragment = TextFragment.newInstance("Text", song);

        if (song.getScores() != null && !song.getScores().isEmpty())
            scoreFragment = ScoreFragment.newInstance("Score", song);

        if (song.getOriginal() != null || song.getPlaybacks() != null && !song.getPlaybacks().isEmpty())
            playerFragment = PlayerFragment.newInstance("Player", song);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.panel_linearLayout, screenFragment, null)
                .add(R.id.panel_linearLayout, panelFragment, PanelFragment.TAG)
                .commit();
        // Fragments <

        // View Pager >
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(2);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (textFragment != null)
            viewPagerAdapter.addItem(textFragment);

        if (scoreFragment != null)
            viewPagerAdapter.addItem(scoreFragment);

        if (playerFragment != null)
            viewPagerAdapter.addItem(playerFragment);

        viewPager.setAdapter(viewPagerAdapter);
        // View Pager <
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final String TITLE = "title";

        private List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addItem(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = fragments.get(position);
            Bundle args = fragment.getArguments();
            String title = args.getString(TITLE);
            return title;
        }
    }
}