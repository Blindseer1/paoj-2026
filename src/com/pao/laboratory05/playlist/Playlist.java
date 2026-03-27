package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];
    Playlist(String name){
      this.name= name;
    }
  public void addSong(Song song) {
    songs = Arrays.copyOf(songs,songs.length + 1);
    songs[songs.length-1] = song;

  }

  public String getName(){
    return this.name;
  }

  public void printSortedByTitle(){
    Song[] tmp = songs.clone();
    Arrays.sort(tmp);
    System.out.println("Sortate dupa titlu:");
    for(Song song: songs){
      System.out.println(song.title());
    }

  }
  public void printSortedByDuration(){
    Song[] tmp = songs.clone();
    Arrays.sort(tmp,new SongDurationComparator());
    System.out.println("Sortate dupa durata:");
    for(Song song: songs){
      System.out.println(song.title());
    }
  }

  public int getTotalDuration(){
    int ans=0;
    for(Song song: songs){
      ans+=song.durationSeconds();
    }
    return ans;
  }



}
