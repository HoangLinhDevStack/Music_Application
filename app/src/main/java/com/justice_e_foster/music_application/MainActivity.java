//package com.justice_e_foster.music_application;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.SearchView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//    private RecyclerView rvListMusic;
//    private SearchView svLagu;
//    private List<String> songList;
//    private SongAdapter songAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        rvListMusic = findViewById(R.id.rvListMusic);
//        svLagu = findViewById(R.id.svLagu);
//
//        // Initialize the song list
//        songList = new ArrayList<>();
//        songList.add("Nhạc remix cực đỉnh");
//        songList.add("Song 2");
//        songList.add("Song 3");
//        songList.add("Song 4");
//        songList.add("Song 5");
//        songList.add("Song 6");
//        songList.add("Song 7");
//        songList.add("Song 8");
//
//
//
//        // Setup RecyclerView
//        songAdapter = new SongAdapter(songList, this::openPlaySongActivity);
//        rvListMusic.setLayoutManager(new LinearLayoutManager(this));
//        rvListMusic.setAdapter(songAdapter);
//
//        // Search filter logic
//        svLagu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                songAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//    }
//
//    // Open PlaySongActivity and pass the song name
//    private void openPlaySongActivity(String songName) {
//        Intent intent = new Intent(this, PlaySongActivity.class);
//        intent.putExtra("SONG_NAME", songName);
//        startActivity(intent);
//    }
//}


package com.justice_e_foster.music_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvListMusic;
    private SearchView svLagu;
    private List<String> songList;
    private SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListMusic = findViewById(R.id.rvListMusic);
        svLagu = findViewById(R.id.svLagu);

        // Initialize the song list
        songList = new ArrayList<>();
        songList.add("Nhạc remix cực đỉnh");
        songList.add("Song 2");
        songList.add("Song 3");
        songList.add("Song 4");
        songList.add("Song 5");
        songList.add("Song 6");
        songList.add("Song 7");
        songList.add("Song 8");

        // Setup RecyclerView
        songAdapter = new SongAdapter(songList, this::openPlaySongActivity);
        rvListMusic.setLayoutManager(new LinearLayoutManager(this));
        rvListMusic.setAdapter(songAdapter);

        // Search filter logic
        svLagu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    // Open PlaySongActivity and pass the song index
    private void openPlaySongActivity(String songName) {
        // Find the index of the clicked song in the list
        int songIndex = songList.indexOf(songName);

        // Pass the song index instead of the song name
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.putExtra("SONG_INDEX", songIndex);
        startActivity(intent);
    }
}

