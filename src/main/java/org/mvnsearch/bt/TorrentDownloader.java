package org.mvnsearch.bt;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

import java.io.File;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * torrent downloader & seeder
 *
 * @author linux_china
 */
public class TorrentDownloader {

    public static void main(String[] args) throws Exception {
        SharedTorrent torrent = SharedTorrent.fromFile(new File("/tmp/torrent/wf8.final.zip.torrent"), new File("/tmp"));
        Client client = new Client(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()), torrent);
        try {
            client.setMaxDownloadRate(1000.0);
            client.setMaxUploadRate(2000.0);
            client.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Client.ClientState state = (Client.ClientState) arg;
                    if (state == Client.ClientState.DONE) {
                        System.out.println("Done");
                    }
                }
            });
            client.share(10);
            // client.download(); only for download
        } finally {
            client.stop(true);
        }
    }
}
