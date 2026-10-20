package fbs.lg1.Services.Categorys;

import fbs.lg1.Enums.MusicGenre;

public class MusicCategory extends AbstractCategory {

    private String ArtistName;
    private String AlbumName;
    private MusicGenre Genre;

    public MusicCategory(String title, long plannedTimeSec, long startTimeSec,String artistName, String albumName, MusicGenre musicGenre)
    {
        super(title, plannedTimeSec, startTimeSec);
        initialize(artistName, albumName, musicGenre);
    }

    private void initialize(String artistName, String albumName, MusicGenre musicGenre)
    {
        this.ArtistName = artistName;
        this.AlbumName = albumName;
        this.Genre = musicGenre;
    }

}
