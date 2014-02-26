package org.mvnsearch.bt;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * BT tracker server
 *
 * @author linux_china
 */
public class TrackerServer {
    private static Logger log = LoggerFactory.getLogger(TrackerServer.class);

    public static void main(String[] args) throws Exception {
        // First, instantiate a Tracker object with the port you want it to listen on.
        // The default tracker port recommended by the BitTorrent protocol is 6969.
        Tracker tracker = new Tracker(new InetSocketAddress(6969));
        publishDirectory(tracker, new File("/tmp/torrent"));
        tracker.start();
    }

    /**
     * publish directory
     *
     * @param tracker BT tracker
     * @param parent  directory to host files
     * @throws Exception exception
     */
    public static void publishDirectory(Tracker tracker, File parent) throws Exception {
        FilenameFilter filter = new SuffixFileFilter(new String[]{".avi", ".txt", ".mp3", ".ogg", ".flv", ".zip"});
        log.info("Analysing: Begin to scan " + parent.getAbsolutePath());
        String announceUrl = "http://" + InetAddress.getLocalHost().getHostAddress() + ":6969/announce";
        for (File commonFile : parent.listFiles(filter)) {
            addBtShared(tracker, announceUrl, commonFile);
        }
    }

    public static void addBtShared(Tracker tracker, String announceUri, File sharedFile) {
        try {
            // Try to generate the .torrent file
            File torrentFile = new File(sharedFile.getParentFile(), sharedFile.getName() + ".torrent");
            Torrent torrent = Torrent.create(new File(sharedFile.getAbsolutePath()), new URI(announceUri), "createdByTtorrent");
            FileOutputStream fos = new FileOutputStream(torrentFile);
            torrent.save(fos);
            fos.close();
            // Announce file to tracker
            TrackedTorrent tt = new TrackedTorrent(torrent);
            tracker.announce(tt);
            log.info("Announce: " + torrentFile.getAbsolutePath() + " torrent announced");
            // seed the shared torrent
            SharedTorrent sharedTorrent = new SharedTorrent(torrent, sharedFile.getParentFile(), true);
            Client seeder = new Client(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()), sharedTorrent);
            seeder.setMaxDownloadRate(10000.0); //10M
            seeder.setMaxUploadRate(20000.0); //20M
            seeder.share();
            log.info("Sharing: " + torrent.getName() + " shared");
        } catch (Exception e) {
            log.error("Unable to announce or share file: " + sharedFile.getAbsolutePath(), e);
        }
    }


}
