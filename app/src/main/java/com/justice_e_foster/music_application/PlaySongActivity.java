package com.justice_e_foster.music_application;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlaySongActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private int[] songResources = {
            R.raw.music_1,
            R.raw.music_2,
            R.raw.music_3,
            R.raw.music_4,
            R.raw.music_5,
            R.raw.music_6,
            R.raw.music_7,
            R.raw.music_8,
    };

    private int currentSongIndex = -1; // Track current song index to avoid reloading the same song

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        TextView tvSongTitle = findViewById(R.id.tvSongTitle);
        ImageView imagePlay = findViewById(R.id.imagePlay);

        // Get the song position passed from MainActivity
        int songPosition = getIntent().getIntExtra("SONG_POSITION", 0);
        String songName = getIntent().getStringExtra("SONG_NAME");

        if (songName == null) {
            // If no song name is passed, show an error or default text
            songName = "No song selected!";
        }

        // Check if we need to load a new song
        if (songPosition != currentSongIndex) {
            // Release the previous MediaPlayer instance if it exists
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            // Update the current song index
            currentSongIndex = songPosition;

            // Create a new MediaPlayer instance for the selected song
            mediaPlayer = MediaPlayer.create(this, songResources[songPosition]);
            mediaPlayer.setOnCompletionListener(mp -> resetMusic());

            // Set the song title
            tvSongTitle.setText("Song " + (songPosition + 1));
            TextView songTitleTextView = findViewById(R.id.songTitle);
            songTitleTextView.setText("Now Playing: " + songName);
        }

        // Play/Pause button logic
        imagePlay.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imagePlay.setImageResource(R.drawable.ic_play); // Set to play icon when paused
                } else {
                    mediaPlayer.start();
                    imagePlay.setImageResource(R.drawable.ic_pause); // Set to pause icon when playing
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure that we release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void resetMusic() {
        // Reset the MediaPlayer when the song completes
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0); // Seek to the beginning
            mediaPlayer.pause();  // Pause the song
            ImageView imagePlay = findViewById(R.id.imagePlay);
            imagePlay.setImageResource(R.drawable.ic_play); // Reset to play icon
        }
    }
}