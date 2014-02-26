package org.mvnsearch.bt;

import com.turn.ttorrent.common.Torrent;

import java.io.File;

/**
 * torrent info
 *
 * @author linux_china
 */
public class TorrentInfo {

    public static void main(String[] args) throws Exception {
        Torrent torrent = Torrent.load(new File("/tmp/torrent/wf8.final.zip.torrent"));
        System.out.println(torrent);
    }
}
