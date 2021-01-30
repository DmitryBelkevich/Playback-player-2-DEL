package com.hard.playback_player.services;

import com.hard.playback_player.models.Band;
import com.hard.playback_player.repositories.BandRepository;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class BandService {
    private FileService fileService;
    private BandRepository bandRepository;

    public BandService() {
        fileService = new FileService();
        bandRepository = new BandRepository();
    }

    public Collection<Band> getAll() {
//        File file = new File("/storage/emulated/0/download/" + "bands.json");
//
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            String url = "https://drive.google.com/uc?export=download&id=" + "1eZ-3iykbW8dfLmg9ZGjg0uDt_3cG6bq9";
//
//            fileService.download(url, file);
//        }

        return bandRepository.getAll();
    }
}
