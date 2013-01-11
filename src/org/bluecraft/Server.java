package org.bluecraft;

/*
 * Insert REALLY LONG Opencraft License here.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.bluecraft.model.MapController;
import org.bluecraft.model.Store;
import org.bluecraft.model.World;
import org.bluecraft.net.SessionHandler;
import org.bluecraft.task.TaskQueue;
import org.bluecraft.task.impl.CTFProcessTask;
import org.bluecraft.task.impl.ConsoleTask;
import org.bluecraft.task.impl.HeartbeatTask;
import org.bluecraft.task.impl.ItemDropTask;
import org.bluecraft.task.impl.MessageTask;
import org.bluecraft.task.impl.UpdateTask;
import org.bluecraft.util.SetManager;

/**
 * The core class of the OpenCraft server.
 * @author Graham Edgecombe
 * @author Quinton Marchi
 */
public final class Server {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

        public static Random r = new Random();

        private static Store store;

        public static String failMsg;

        public static boolean configExists = false;

        public static String log = "";
        
        private static ArrayList<String> ipBans = new ArrayList<String>(128);
        
        public static ArrayList<String> rulesText = new ArrayList<String>(20);
        
        public static ArrayList<String> newsText = new ArrayList<String>(20);
                
        //set the strings for the file checkers!
        public static boolean lgexist;
        public static boolean ipexist;
        public static boolean bnexist;
        public static boolean rlexist;
        public static boolean nwexist;
        public static String pathTo;
    public static String password = "test56";

        //private static IRC irc;
	
	/**
	 * The entry point of the server application.
	 * @param args
	 */
	public static void main(String[] args) {
                //System.setErr(new PrintStream(new NullOutputStream()));
		try {
                    new Server().start();
		} catch (Throwable t) {
                    logger.log(Level.SEVERE, "\u001b[0;31mAn error occurred while starting the server.", t);
		}
	}

        public static String [] wrapText (String text, int len)
        {
          // return empty array for null text
          if (text == null) {
                return new String [] {};
            }

          // return text if len is zero or less
          if (len <= 0) {
                return new String [] {text};
            }

          // return text if less than length
          if (text.length() <= len) {
                return new String [] {text};
            }

          char [] chars = text.toCharArray();
          Vector lines = new Vector();
          StringBuilder line = new StringBuilder();
          StringBuffer word = new StringBuffer();

          for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
              if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
              }

              line.append(word);
              word.delete(0, word.length());
            }
          }

          // handle any extra chars in current word
          if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
              lines.add(line.toString());
              line.delete(0, line.length());
            }
            line.append(word);
          }

          // handle extra line
          if (line.length() > 0) {
            lines.add(line.toString());
          }

          String [] ret = new String[lines.size()];
          int c = 0; // counter
          for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
          }

          return ret;
        }

        public static void log(String line)
        {
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String date =  dfm.format(new Date());
            
            String text = "[\u001b[0;31m"+date+"\u001b[0;37m] "+line;
            System.out.println(text);
            log += text+"\n";
        }

        public static void saveLog()
        {
            try {
                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datelg = df2.format(new Date());
                lgexist = new File("./logs/server"+datelg+".log").exists();
                if (!lgexist) {
                    logger.info("Loading logs file...");
                
                    new File("./logs/server"+datelg+".log").createNewFile();
                    logger.info("\u001b[1;33mThe logs file was loaded.\u001b[1;37m");
                }
                PrintWriter w = new PrintWriter(new FileWriter(new File("./logs/server"+datelg+".log"), true));
                w.append(log);
                w.flush();
                w.close();
                log = "";
            } catch (IOException ex) {
                System.err.println("There was an error saving the logs.");
            }
        }

        public static Store getStore()
        {
            return store;
        }

	/**
	 * The socket acceptor.
	 */
	public final IoAcceptor acceptor = new NioSocketAcceptor();
	
	/**
	 * Creates the server.
	 * @throws IOException if an I/O error occurs.
	 * @throws FileNotFoundException if the configuration file is not found.
	 */
	public Server() throws FileNotFoundException, IOException {
		logger.info("Starting BlueCraft CTF server...");
                pathTo = new File("./").getAbsolutePath();
		Configuration.readConfiguration();
                int min = Configuration.getConfiguration().getMinRank();
                
                ipexist = new File("./data/ipbans.txt").exists();
                if (!lgexist) {
                logger.info("\u001b[1;31mLoading IPBans file...\u001b[1;37m");
            }
                    new File("./ipbans.txt").createNewFile();
                    logger.info("\u001b[1;33mThe IPBans file loaded.\u001b[1;37m");
                FileInputStream ipFile = new FileInputStream("./ipbans.txt");
                BufferedReader eader = new BufferedReader(new InputStreamReader(ipFile));
                String l = null;
                while((l = eader.readLine()) != null)
                {
                    ipBans.add(l);
                }
                
                rlexist = new File("./rules.txt").exists();
                if (!lgexist) {
                logger.info("\u001b[1;31mLoading Rules file...\u001b[1;37m");
            }
                    new File("./rules.txt").createNewFile();
                    logger.info("\u001b[1;33mThe Rules file was loaded.\u001b[1;37m");
                FileInputStream rulesFile = new FileInputStream("./rules.txt");
                eader = new BufferedReader(new InputStreamReader(rulesFile));
                l = null;
                while((l = eader.readLine()) != null)
                {
                    rulesText.add(l);
                }
                
                //The news command is outdated, and will be removed in the next release.
                
                /*nwexist = new File("./props/news.txt").exists();
                if (!lgexist) {
                logger.info("\u001b[1;31mHad to create the news file :S");
            }
                    new File("./props/news.txt").createNewFile();
                    logger.info("\u001b[1;33mIf you see this without errors than it was created!\u001b[1;37m");
                FileInputStream newsFile = new FileInputStream("./props/news.txt");
                eader = new BufferedReader(new InputStreamReader(newsFile));
                l = null;
                while((l = eader.readLine()) != null)
                {
                    newsText.add(l);
                }*/
                
                MapController.create();
                logger.info("\u001b[1;33mLoading maps...\u001b[1;34m(MapController.create())\u001b[1;37m ");
		World.getWorld() ;
		SetManager.getSetManager().reloadSets();
		acceptor.setHandler(new SessionHandler());
		TaskQueue.getTaskQueue().schedule(new UpdateTask());
		TaskQueue.getTaskQueue().schedule(new CTFProcessTask());
                TaskQueue.getTaskQueue().schedule(new HeartbeatTask());
		TaskQueue.getTaskQueue().schedule(new MessageTask());
		new Thread(new ConsoleTask()).start();
                if(!Constants.classic) {
                new Thread(new ItemDropTask()).start();
            }
		logger.info("\u001b[1;33mPreparing server...\u001b[1;34m(ItemDropTask())\u001b[1;37m");
	}
        
        public static boolean isAllowed(String playerName)
        {
            return true;
        }
	
	/**
	 * Starts the server.
	 * @throws IOException if an I/O error occurs.
	 */
	public void start() throws IOException {
                if(Constants.classic) {
                Constants.PORT = Configuration.getConfiguration().getPort();
            }
		logger.log(Level.INFO, "\u001b[1;33mThe server is now running on port {0}...", Configuration.getConfiguration().getPort());
		acceptor.bind(new InetSocketAddress(Configuration.getConfiguration().getPort()));
                if(!Constants.classic) {
                store = new Store();
                logger.info("\u001b[1;33mServer sucessfully started.\u001b[1;37m");
            }     
	}
        /**
     *
     * @throws IOException
     */
    public void stop() throws IOException{
            
        }
        public static String httpGet(String address)
        {
            try {
                URL url = new URL(address);
                URLConnection con = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String result = "";
                String line;
                while((line = in.readLine()) != null) {
                    result += line + "\n";
                }
                return result;
            } catch (Exception ex) {
                log("httpGet failed");
                return null;
            }
        }
        public static int getUnsigned(int b)
        {
            if(b < 0) {
                return b + 256;
            }
            else {
                return b;
            }
        }
        public static boolean isIPBanned(String ip)
        {
            return ipBans.contains(ip);
        }
        public static void banIP(String ip)
        {
            ipBans.add(ip);
            saveIPBans();
        }
        public static void unbanIP(String ip)
        {
            ipBans.remove(ip);
            saveIPBans();
        }
        private static void saveIPBans()
        {
        try {
            new File("./ipbans.txt").delete();
            FileOutputStream out = new FileOutputStream("./ipbans.txt");
            for(String ip : ipBans)
            {
                out.write((ip+"\n").getBytes());
            }
        } catch (IOException ex) {
        }
        }
    public static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
        
    }
    public void shutdown(){
        
    }
}

