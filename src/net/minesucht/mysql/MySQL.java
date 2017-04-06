package net.minesucht.mysql;
 
 
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.minesucht.main.MiniGames;
 
 
 
public class MySQL {
 
    private Connection conn;
    private MiniGames plugin;
    private String hostname;
    private String user;
    private String password;
    private String database;
    private int port;
    public MySQL(MiniGames plugin) throws Exception
    {
       
        this.plugin = plugin;
        this.hostname = plugin.getConfig().getString("MySQL.Hostname");
        this.port = plugin.getConfig().getInt("MySQL.Port");
        this.database =  plugin.getConfig().getString("MySQL.Database");
        this.user = plugin.getConfig().getString("MySQL.User");
        this.password = plugin.getConfig().getString("MySQL.Password");
        this.openConnection();
       
    }
    public Connection openConnection()
    {
       
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://"+hostname + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8");
                this.conn = conn;
                queryUpdate("CREATE TABLE IF NOT EXISTS minigames(UUID varchar(64), PUNKTE int, RANK int, WON int, PLAYED int, ABORTED int);");
                queryUpdate("CREATE TABLE IF NOT EXISTS properties(RANK varchar(32), NAME varchar(32), ID int);");
                injectRanks();
                return conn;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Bukkit.shutdown();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Bukkit.shutdown();
            }
            return conn;
           
   
   
    }
    
    
    public void injectRanks(){
    	try {
    		if(rankExists()){
    			return;
    		}else{
    			if(getConnection().isValid(2000)){
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK1', '§eAnfänger', 1);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK2', '§eBeginner', 2);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK3', '§eFortgeschrittener', 3);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK4', '§eGuter Spieler', 4);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK5', '§eProfi', 5);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK6', '§eMeister', 6);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK7', '§eLegendär', 7);");
    			}else{
    				openConnection();
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK1', '§eAnfänger', 1);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK2', '§eBeginner', 2);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK3', '§eFortgeschrittener', 3);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK4', '§eGuter Spieler', 4);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK5', '§eProfi', 5);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK6', '§eMeister', 6);");
    				queryUpdate("INSERT INTO properties(`RANK`, `NAME`, `ID`) VALUES ('RANK7', '§eLegendär', 7);");
    			}
    		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean rankExists(){
    	try {
			if(getConnection().isValid(2000)){
				Connection conn = getConnection();
				ResultSet rs = null;
				PreparedStatement st = conn.prepareStatement("SELECT * FROM properties WHERE ID = 1;");
				rs = st.executeQuery();
				return rs.next();
			}else{
				openConnection();
				Connection conn = getConnection();
				ResultSet rs = null;
				PreparedStatement st = conn.prepareStatement("SELECT * FROM properties WHERE ID = 1;");
				rs = st.executeQuery();
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		    return false;
		}
    }
    public Connection getConnection()
    {
        return this.conn;
    }
    public boolean hasConnection()
    {
        try {
            return this.conn != null || this.conn.isValid(1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
   
    public void closeRessources(ResultSet rs, PreparedStatement st)
    {
        if(rs != null)
        {
            try {
                rs.close();
            } catch (SQLException e) {
               
            }
        }
        if(st != null)
        {
            try {
                st.close();
            } catch (SQLException e) {
               
            }
        }
    }
   
   
    public void closeConnection()
    {
        try {
            this.conn.close();
        } catch (SQLException e) {
           
            e.printStackTrace();
        }finally
        {
            this.conn = null;
        }
       
    }
    public void queryUpdate(String query)
    {
        new BukkitRunnable() {
           
            @Override
            public void run() {
                try {
                    if(getConnection().isValid(2000))
                    {
                        PreparedStatement st = null;
                       
                        Connection conn = getConnection();
                        try {
                             st = conn.prepareStatement(query);
                             st.executeUpdate();
                        } catch (SQLException e) {
                            System.err.println("Failed to send update '" + query + "'.");
                            e.printStackTrace();
                        }finally
                        {
                            closeRessources(null, st);
                        }
                    }
                    else
                    {
                        try {
                            openConnection();
                            PreparedStatement st = null;
                           
                            Connection conn = getConnection();
                            try {
                                 st = conn.prepareStatement(query);
                                 st.executeUpdate();
                            } catch (SQLException e) {
                                System.err.println("Failed to send update '" + query + "'.");
                                e.printStackTrace();
                            }finally
                            {
                                closeRessources(null, st);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
               
               
            }
        }.runTaskAsynchronously(plugin);
       
    }
   
    public boolean playerExists(String uuid){
        try {
            if(getConnection().isValid(2000)){
                Connection conn = getConnection();
                ResultSet rs = null;
                PreparedStatement st = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID = '" + uuid + "';");
                rs = st.executeQuery();
                return rs.next();
               
            }else{
                openConnection();
                Connection conn = getConnection();
                ResultSet rs = null;
                PreparedStatement st = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID = '" + uuid + "';");
                rs = st.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
   
   
    /*
     * Die Letzten / Schlechtesten ¼ (ein viertel) der Spieler bekommen Punktabzu in For
     *
     *
     * 20 + 60 + 100 + 10 + 40 + 15 = 245 / 6 = 40 Durchschnitt aller Spiele nach kompletten Ende
     *
     *
     * Einzel Spielmodi:
     * 10 - für den ersten Platz
     * 6 - für den zweiten Platz
     * 3 - für den dritten Platz
     * 2 - für die Verlierer
     * Team Spielmodi:
     * Durchschnitt = 16
     * Unterdurchscnitt = 7
     * Überdurchschnitt = 16
     * Wenn Unterdurschnitt: Minus Unterpunkte bei durchschnitt
     * Bronze:
     * 0 - 100
     * Rank 2:
     * 100 - 250
     * Rank 3:
     * 250- 500
     * Rank 4:
     * 500 - 1000
     * Rank 5:
     * 1000 - 2000
     * Rank 6:
     * 2000 - 4000
     * Rank 7:
     * 4000-8000
     *
     * Durchschnitt = dt;
     * dt1 + dt2 + dt3 + dt4 + dt5 + dt6 / 6 = Durchschnitt nach allen Spielen.
     */
   
    public void addPlayer(String uuid){
        //UUID varchar(64), PUNKTE int, RANK varchar(32), WON int, PLAYED int
        try {
            if(getConnection().isValid(2000)){
                queryUpdate("INSERT INTO minigames(`UUID`, `PLAYED`, `WON`, `RANK`, `PUNKTE`, `ABORTED`) VALUES('" + uuid + "', 0, 0, 1, 0, 0)");
            }else{
                openConnection();
                queryUpdate("INSERT INTO minigames(`UUID`, `PLAYED`, `WON`, `RANK`, `PUNKTE`, `ABORTED`) VALUES('" + uuid + "', 0, 0, 1, 0, 0)");
               
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updatePlayedGames(String uuid){
        try {
            if(getConnection().isValid(2000)){
                queryUpdate("UPDATE minigames SET PLAYED = PLAYED + 1 WHERE UUID = '" + uuid +"';");
            }else{
                openConnection();
                queryUpdate("UPDATE minigames SET PLAYED = PLAYED + 1 WHERE UUID = '" + uuid +"';");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateWon(String uuid){
        try {
            if(getConnection().isValid(2000)){
                queryUpdate("UPDATE minigames SET PLAYED = WON + 1 WHERE UUID = '" + uuid +"';");
            }else{
                openConnection();
                queryUpdate("UPDATE minigames SET PLAYED = WON + 1 WHERE UUID = '" + uuid +"';");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updatePunkte(String uuid, int punkte){
        try {
            if(getConnection().isValid(2000)){
                queryUpdate("UPDATE minigames SET PUNKTE = PUNKTE + " + punkte + " WHERE UUID = '" + uuid +"';");
            }else{
                openConnection();
                queryUpdate("UPDATE minigames SET PUNKTE = PUNKTE + " + punkte + " WHERE UUID = '" + uuid +"';");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void substractPunkte(String uuid, int punkte){
    	try {
            if(getConnection().isValid(2000)){
                queryUpdate("UPDATE minigames SET PUNKTE = PUNKTE - " + punkte + " WHERE UUID = '" + uuid +"';");
            }else{
                openConnection();
                queryUpdate("UPDATE minigames SET PUNKTE = PUNKTE - " + punkte + " WHERE UUID = '" + uuid +"';");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateRank(String uuid, int Rank){
        try {
            if(getConnection().isValid(2000)){
                queryUpdate("UPDATE minigames SET RANK = " + Rank + " WHERE UUID = '" + uuid +"';");
            }else{
                openConnection();
                queryUpdate("UPDATE minigames SET RANK = " + Rank + " WHERE UUID = '" + uuid +"';");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Integer getRank(String uuid){
       
        try {
            if(getConnection().isValid(2000)){
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("RANK");
                return rank;
            }else
            {
                openConnection();
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("RANK");
                return rank;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
       
    }
    public Integer getPunkte(String uuid){
        try {
            if(getConnection().isValid(2000)){
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("PUNKTE");
                return rank;
            }else
            {
                openConnection();
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("PUNKTE");
                return rank;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    public Integer getWon(String uuid){
        try {
            if(getConnection().isValid(2000)){
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("WON");
                return rank;
            }else
            {
                openConnection();
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("WON");
                return rank;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    public Integer getPlayed(String uuid){
        try {
            if(getConnection().isValid(2000)){
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("PLAYED");
                return rank;
            }else
            {
                openConnection();
                Connection conn = getConnection();
                PreparedStatement st = null;
                ResultSet rs = null;
                st = conn.prepareStatement("SELECT * FROM minigames WHERE UUID ='" + uuid + "';");
                rs = st.executeQuery();
                rs.next();
                int rank = rs.getInt("PLAYED");
                return rank;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    
    public void updateAborted(String uuid){
    	try {
			if(getConnection().isValid(2000)){
				queryUpdate("UPDATE minigames SET ABORTED = ABORTED + 1 WHERE UUID='" + uuid + "';");
			}else{
				openConnection();
				queryUpdate("UPDATE minigames SET ABORTED = ABORTED + 1 WHERE UUID='" + uuid + "';");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<String> getTop(){
    	List<String> list = new ArrayList<>();
    	try {
			if(!getConnection().isValid(2000)){
				openConnection();
			}
			Connection conn = getConnection();
			ResultSet rs = conn.createStatement().executeQuery("SELECT UUID FROM minigames ORDER BY PUNKTE DESC LIMIT 5");
			while(rs.next()){
				list.add(rs.getString("UUID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
   
    
    
    public String getRankName(int Rank){
    	try {
			if(getConnection().isValid(2000)){
				ResultSet rs = null;
				Connection conn = getConnection();
				PreparedStatement st = null;
				st = conn.prepareStatement("SELECT * FROM properties WHERE ID = " + Rank + ";");
				rs = st.executeQuery();
				rs.next();
				String name = rs.getString("NAME");
				return name;
			}else{
				openConnection();
				ResultSet rs = null;
				Connection conn = getConnection();
				PreparedStatement st = null;
				st = conn.prepareStatement("SELECT * FROM properties WHERE ID = " + Rank + ";");
				rs = st.executeQuery();
				rs.next();
				String name = rs.getString("NAME");
				return name;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "§cKontaktiere an Admin";
		}
    }
   
           
       
       
       
}