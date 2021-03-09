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
	
	public boolean userExist (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * FROM user_value WHERE username = (?);";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return true;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}

	    }
		return false;
	}
	
	public void userSignup (String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO user_value (username, password) VALUES (?, ?);";
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
	
	public boolean userUpdate (String name, String pass, String newPass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value;");
			resultSet = stmnt.executeQuery(sql);
			while(resultSet.next()) { 
				 if(name.equals(resultSet.getString("username")) && pass.equals(resultSet.getString("password"))) {
					 String s = "UPDATE user_value SET password=(?) WHERE username=(?);";
					 pstmnt = connection.prepareStatement(s);
					 pstmnt.setString(1, newPass);
					 pstmnt.setString(2, name);
					 pstmnt.executeUpdate();
					 return true;
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
		return false;
	}
	
	public boolean userLogin(String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value WHERE username=(?) AND password=(?);");
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, pass);
			resultSet = pstmnt.executeQuery();
			while(resultSet.next()) { 
				return true;
			}
		}	
		catch (SQLException ignore) {}
		finally { 
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (stmnt != null) try { stmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return false;
	}
	
	public boolean topicExist(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from topic_value WHERE topicname = (?);";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return true;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return false;
	}
	
	public void topicSignup(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_value (topicname) VALUES (?);";
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
			sql = "INSERT INTO topic_users (username,topicname) VALUES"
					+ 	"("
					+		"(?),"
					+		"(?)"
					+	");";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public boolean checkTopicUser(String name, String topic) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT username, topicname"
					+ " FROM topic_users"
					+ " WHERE username = (?)"
					+ " AND topicname = (?)"
					+ " AND left_group=0;";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return true;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return false;
	}
	
	public void sendDirectMess(String sender, String reciever, String mess) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO direct_mess (sender_name,reciever_name,message) VALUES"
				+ 	"("
				+		"(?),"
				+		"(?),"
				+		"(?)"
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
	
	public void leaveTopicUser(String name, String topic) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "UPDATE topic_users SET left_group=1 WHERE username=(?) AND topicname=(?);";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}

	public void sendTopicMess(String name, String topic, String mess) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_mess (username, topicname, message) VALUES "
					+ "("
					+ " (?), "
					+ " (?), "
					+ " (?)"
					+ ");";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1, name);
			pstmnt.setString(2, topic);
			pstmnt.setString(3, mess);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {ignore.printStackTrace();}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
}
