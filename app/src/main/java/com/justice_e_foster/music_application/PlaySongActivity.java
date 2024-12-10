

package com.justice_e_foster.music_application;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PlaySongActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private int[] songResources = {
            R.raw.music_1, R.raw.music_2, R.raw.music_3,
            R.raw.music_4, R.raw.music_5, R.raw.music_6,
            R.raw.music_7, R.raw.music_8
    };
    private int currentSongIndex = 0;
    private boolean isRepeat = false;
    private boolean isShuffle = false;
    private SeekBar seekBar;
    private TextView tvCurrentDuration, tvTotalDuration, tvSongTitle;
    private ImageView imagePlay, imageRepeat, imageShuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        // Initialize UI components
        tvSongTitle = findViewById(R.id.songTitle);
        tvCurrentDuration = findViewById(R.id.tvCurrentDuration);
        tvTotalDuration = findViewById(R.id.tvTotalDuration);
        seekBar = findViewById(R.id.seekBar);
        imagePlay = findViewById(R.id.imagePlay);
        ImageView imagePrev = findViewById(R.id.imagePrev);
        ImageView imageNext = findViewById(R.id.imageNext);
        ImageView imageForward = findViewById(R.id.imageForward);
        ImageView imageRewind = findViewById(R.id.imageRewind);
        imageRepeat = findViewById(R.id.imageRepeat);
        imageShuffle = findViewById(R.id.imageShuffle);

        // Get the passed song index
        currentSongIndex = getIntent().getIntExtra("SONG_INDEX", 0); // Default to 0 if no index is passed
        playSong(currentSongIndex);

        // Play/Pause button
        imagePlay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                imagePlay.setImageResource(R.drawable.ic_play);
            } else {
                mediaPlayer.start();
                imagePlay.setImageResource(R.drawable.ic_pause);
            }
        });

        // Previous button
        imagePrev.setOnClickListener(v -> {
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else {
                currentSongIndex = songResources.length - 1; // Loop to last song
            }
            playSong(currentSongIndex);
        });

        // Next button
        imageNext.setOnClickListener(v -> {
            if (isShuffle) {
                // Random song selection
                currentSongIndex = new Random().nextInt(songResources.length); // Random song
            } else {
                // Sequential song selection
                currentSongIndex = (currentSongIndex + 1) % songResources.length; // Loop to first song
            }
            playSong(currentSongIndex);
        });

        // Fast forward button
        imageForward.setOnClickListener(v -> {
            int forwardPosition = mediaPlayer.getCurrentPosition() + 10000;
            mediaPlayer.seekTo(Math.min(forwardPosition, mediaPlayer.getDuration()));
        });

        // Rewind button
        imageRewind.setOnClickListener(v -> {
            int rewindPosition = mediaPlayer.getCurrentPosition() - 10000;
            mediaPlayer.seekTo(Math.max(rewindPosition, 0));
        });

        // Repeat button
        imageRepeat.setOnClickListener(v -> {
            isRepeat = !isRepeat;
            imageRepeat.setImageResource(isRepeat ? R.drawable.ic_repeat_pressed : R.drawable.ic_repeat);
        });

        // Shuffle button
        imageShuffle.setOnClickListener(v -> {
            isShuffle = !isShuffle;
            imageShuffle.setImageResource(isShuffle ? R.drawable.ic_shuffle_pressed : R.drawable.ic_shuffle);
        });

        // Handle music completion
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeat) {
                // Repeat the current song
                playSong(currentSongIndex);
            } else if (isShuffle) {
                // Play a random song, ensuring it's not the same as the current one
                int newSongIndex;
                do {
                    newSongIndex = new Random().nextInt(songResources.length);
                } while (newSongIndex == currentSongIndex); // Avoid repeating the same song
                currentSongIndex = newSongIndex;
                playSong(currentSongIndex);
            } else {
                // Play the next song in sequence
                currentSongIndex = (currentSongIndex + 1) % songResources.length; // Loop to first song
                playSong(currentSongIndex);
            }
        });
    }

    private void playSong(int index) {
        // Stop and release the current MediaPlayer if exists
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Create a new MediaPlayer for the selected song
        mediaPlayer = MediaPlayer.create(this, songResources[index]);
        mediaPlayer.start();
        imagePlay.setImageResource(R.drawable.ic_pause);

        // Set up the SeekBar and song details
        tvSongTitle.setText("Now Playing: Song " + (index + 1));
        seekBar.setMax(mediaPlayer.getDuration());
        tvTotalDuration.setText(formatTime(mediaPlayer.getDuration()));
        updateSeekBar();
    }

    private void updateSeekBar() {
        // Update SeekBar and TextView periodically
        new Thread(() -> {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                runOnUiThread(() -> {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tvCurrentDuration.setText(formatTime(mediaPlayer.getCurrentPosition()));
                });
                try {
                    Thread.sleep(1000); // Update every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // SeekBar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // Move to the selected position in the song
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private String formatTime(int millis) {
        int minutes = millis / 1000 / 60; // Convert milliseconds to minutes
        int seconds = (millis / 1000) % 60; // Get the remaining seconds
        return String.format("%02d:%02d", minutes, seconds); // Format as mm:ss
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}

