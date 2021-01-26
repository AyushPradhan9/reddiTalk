import java.sql.*;

public class serverDatabase {
	databaseSetup database = new databaseSetup();
	
	private String url = "jdbc:mysql://localhost:3306/redditalk";
	private String username = "root";
	private String password = "toor";
	
	private Connection connection = null;
	private String sql = null;
	private PreparedStatement pstmnt = null;
	private Statement stmnt = null;
	private ResultSet resultSet = null;
	
	public int userExist (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from user_value where username = ?";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}

	    }
		return 0;
	}
	
	public void userSignup (String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO user_value (username, password) VALUES (?, ?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.setString(2,pass);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public int userUpdate (String name, String pass, String newPass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value");
			resultSet = stmnt.executeQuery(sql);
			while(resultSet.next()) { 
				 if(name.equalsIgnoreCase(resultSet.getString("username")) && pass.equalsIgnoreCase(resultSet.getString("password"))) {
					 String s = "update user_value set password=? where username=?";
					 pstmnt = connection.prepareStatement(s);
					 pstmnt.setString(1, newPass);
					 pstmnt.setString(2, name);
					 pstmnt.executeUpdate();
					 return 1;
				 }
			}
		}	
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (stmnt != null) try { stmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public int userLogin (String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value;");
			resultSet = stmnt.executeQuery(sql);
			while(resultSet.next()) { 
				 if(name.equalsIgnoreCase(resultSet.getString("username")) && pass.equalsIgnoreCase(resultSet.getString("password"))) {
					 return 1;
				 }
			}
		}	
		catch (SQLException ignore) {}
		finally { 
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (stmnt != null) try { stmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public void userOnline (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "UPDATE user_value SET online=1 WHERE username=?;";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public void userOffline (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "UPDATE user_value SET online=0 WHERE username=?;";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public int checkOnline (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from user_value where username=? AND online=1;";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}	
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public int topicExist(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from topic_value WHERE topicname = ?";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public void topicSignup(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_value (topicname) VALUES (?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public void setTopicUser(String name, String topic) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_users (user_id,topic_id)"
					+ "SELECT user_value.user_id, topic_value.topic_id"
					+ " FROM user_value,topic_value"
					+ " WHERE user_value.username = (?)"
					+ " AND topic_value.topicname = (?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {
			System.out.println(ignore);
		}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public int checkTopicUser(String name, String topic) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT user_id, topic_id"
					+ " FROM topic_user"
					+ " WHERE user_id = ?"
					+ " AND topic_id = ?";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public void sendDirectMess(String sender, String reciever, String mess) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO direct_mess (sender_id,reciever_id,message) VALUES"
				+ 	"("
				+		"(SELECT a.user_id FROM user_value AS a WHERE username=?)"
				+		",(SELECT b.user_id FROM user_value AS b WHERE username=?)"
				+		",?"
				+	");";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, sender);
			pstmnt.setString(2, reciever);
			pstmnt.setString(3, mess);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}

//	public int checkDirectUser(String sender, String reciever) {
//		try {
//			connection = DriverManager.getConnection(url,username,password);
//			sql = "SELECT sender_id, reciever_id"
//					+ "FROM direct_users"
//					+ "WHERE sender_id = (SELECT user_id FROM user_value WHERE username=?)"
//					+ "AND reciever_id=(SELECT user_id FROM user_value WHERE username=?)";
//			pstmnt = connection.prepareStatement(sql);
//			pstmnt.setString(1,sender);
//			pstmnt.setString(2, reciever);
//			resultSet = pstmnt.executeQuery();
//			if(resultSet.next()) {
//				return 1;
//			}
//		}
//		catch (SQLException ignore) {}
//		finally {
//		    if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
//		    if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
//		    if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
//		}
//		return 0;
//	}
	
}
