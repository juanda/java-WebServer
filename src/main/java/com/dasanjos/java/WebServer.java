package com.dasanjos.java;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * Class <code>WebServer</code> - Main class that starts the Web Server Thread Pool in port 8080 (default)
 */
public class WebServer extends Thread {

    private static Logger log = Logger.getLogger(WebServer.class);
    private static Config config;
    //8080
    private static final int DEFAULT_PORT = 8080;
    private static final int N_THREADS = 3;

    public static void main(String args[]) {
        try {
            config = loadConfiguration(args);
            new WebServer().start(getValidPortParam(args));
        } catch (Exception e) {
            log.error("Startup Error", e);
        }
    }

    private static Config loadConfiguration(String[] args) {
        
        if (args.length > 0) {    
            
            File f = new File(args[0]);
            if (f.exists()){
                Config c = new Config(args[0]);
                return c;
            }  
        }     
        return null;
    }

    public void start(int port) throws IOException {
        ServerSocket s = new ServerSocket(port);
        System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)");
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        while (true) {
            executor.submit(new RequestHandler(s.accept()));
        }
    }

    /**
     * Parse command line arguments (string[] args) for valid port number
     *
     * @return int valid port number or default value (8080)
     */
    static int getValidPortParam(String args[]) throws NumberFormatException {
        if(config != null && config.isConfigOK()){
            return config.getPuerto();
        }
        
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            if (port > 0 && port < 65535) {
                return port;
            } else {
                throw new NumberFormatException("Invalid port! Port value is a number between 0 and 65535");
            }
        }
        
        
        
        return DEFAULT_PORT;
    }
}
