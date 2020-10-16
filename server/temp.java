
public class temp {
	private static String url;
	private static String username;
	private static String password;
	
	public void getInfo(String databaseName,String port, String username, String password) {
		this.username=username;
		this.password=password;
		url="jdbc:mysql://localhost:"+port+"/"+databaseName;
	}
	
	public static void main(String[] args) {
		
		databaseSetup database = new databaseSetup();
		System.out.println(url+" "+username+" "+password);

	}

}
