import java.sql.*;
import java.lang.Math;

public class SalaryStdDev {
	private Connection con = null;

	public void setDBConnection(String url, String user, String password) {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printQueryResult(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		
		if (con == null) {
			System.out.println("There was a problem while connecting database");
			return;
		}
		
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			double avgPow = 0, powAvg = 0, val = 0, stdDev = 0;
			int count = 0;

			while (rs.next()) {
				String salary = rs.getString("sal");
				try {
					val = Double.parseDouble(salary);
					count++;
					avgPow += (val * val);
					powAvg += val;
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}

			if (count != 0) {
				avgPow /= (double) count;
				powAvg /= (double) count;
				stdDev = Math.sqrt(avgPow - Math.pow(powAvg, 2));
			}
			
			System.out.println("Standard Deviation:\n" + stdDev);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 4 || args[0] == null || args[1] == null || args[2] == null || args[3] == null) {
			System.out.println("Make sure you are giving all arguments. Eg.:\n"
			+ "java -cp <path_jdbc_driver.jar>:. SalaryStdDev databaseName tableName login password");
			return;
		}

		String dbURL = "jdbc:db2://localhost:50000/" + args[0];
		String user = args[2];
		String password = args[3];
		String sql = "select e.salary as sal from " + args[2] + "." + args[1] + " e";
		SalaryStdDev lab2 = new SalaryStdDev();

		lab2.setDBConnection(dbURL, user, password);
		lab2.printQueryResult(sql);
	}
}