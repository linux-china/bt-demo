package org.mvnsearch.bt;

import com.turn.ttorrent.common.Torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

/**
 * torrent generator
 *
 * @author linux_china
 */
public class TorrentGenerator {
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("/tmp/torrent/wf8.final.zip.torrent");
        URI announceURI = new URI("http://127.0.0.1:6969/announce");
        File source = new File("/tmp/torrent/wf8.final.zip");
        Torrent torrent = Torrent.create(source, announceURI, "linux_china");
        torrent.save(fos);
        fos.close();
    }
}
