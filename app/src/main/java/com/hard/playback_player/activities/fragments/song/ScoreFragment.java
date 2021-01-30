package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.hard.playback_player.R;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;

import java.io.IOException;
import java.net.URL;

public class ScoreFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;

    private PDFView score_pdfView;

    public ScoreFragment() {

    }

    public static ScoreFragment newInstance(String title, Song song) {
        ScoreFragment fragment = new ScoreFragment();

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
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        score_pdfView = view.findViewById(R.id.score_pdfView);

        load();

        return view;
    }

    /**
     * load
     */

    public void load() {
        if (song.getScores() == null || song.getScores().isEmpty())
            return;

        String url = Constants.GOOGLE_DRIVE_FILE + song.getCurrentScore();

        new Thread(() -> {
            try {
                score_pdfView.fromStream(new URL(url).openStream())
//                .pages(0, 1, 2) // all pages are displayed by default
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(song.getScore().equals(Constants.FULL_SCORE))
                        .enableDoubletap(true)
                        .defaultPage(0)
                        // allows to draw something on the current page, usually visible in the middle of the screen
//                .onDraw(onDrawListener)
                        // allows to draw something on all pages, separately for every page. Called only for visible pages
//                .onDrawAll(onDrawListener)
//                .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                .onPageChange(onPageChangeListener)
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
//                .onPageError(onPageErrorListener)
//                .onRender(onRenderListener) // called after document is rendered for the first time
                        // called on single tap, return true if handled, false to toggle scroll handle visibility
//                .onTap(onTapListener)
//                .onLongPress(onLongPressListener)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        // spacing between pages in dp. To define spacing color, set view background
                        .spacing(1)
//                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//                .linkHandler(DefaultLinkHandler)
//                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
//                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//                .pageSnap(false) // snap pages to screen boundaries
//                .pageFling(false) // make a fling change only a single page like ViewPager
//                .nightMode(false) // toggle night mode
                        .load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}