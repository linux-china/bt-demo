package org.mvnsearch.bt;

import org.junit.Test;

import java.net.InetAddress;

/**
 * spike test case
 *
 * @author linux_china
 */
public class SpikeTest {

    /**
     * spike test
     */
    @Test
    public void testSpike() throws Exception {
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println(localhost.getHostAddress());
    }
}
