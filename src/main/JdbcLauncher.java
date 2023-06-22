package main;

import java.sql.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class JdbcLauncher {
	private static final String SHOW_TABLES = "show tables";
	
	private static final String CREATE_TABLE_STUDENT = "create table student(id int not null  auto_increment,"
            + "promotion varchar(255) not null," + "l_name varchar(255) not null," + "promotion varchar(255) not null,"
            + "email varchar(255) not null," + "phoneNbr varchar(255)," + "abscenceNbr int,"
            + "isRep boolean, primary key(id))";

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException  {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try (Connection con = DriverManager.getConnection("jdbc:mySQL://localhost:3306/testjava", "root", "")) {
			try (Statement stmt = con.createStatement()) {
				
                boolean patientTableToRemove = false;
                try (ResultSet resultSet = stmt.executeQuery(SHOW_TABLES)) {
                    while (resultSet.next()) {
                        String tableName = resultSet.getString(1);
                        System.out.println("table name: " + tableName);

                        //vérifie que la table student existe bien
                        if ("student".equals(tableName)) {
                            patientTableToRemove = true;
                            break;
                        } 
                        // sinon on la crée automatiquement
                        else {
                            patientTableToRemove = false;
                        }
                    }
                }

                if (!patientTableToRemove) {
                    System.out.println("student table creation");
                    stmt.execute(CREATE_TABLE_STUDENT);
                }
            }
			//Fait un premier console Log pour récupérer la commande que l'utilisateur veut exectuter
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the command you want to do (getAll, getById, create) : ");
            String input = reader.readLine();
           
			try (PreparedStatement stmt = con.prepareStatement("insert into patient(name, level) values(?, ?)")) {
				// String insertPatient2 = "insert into patient(name, level) values('grincheux',
				// 5)";
				stmt.setString(1, "grincheux");
				stmt.setInt(2, 5);
				stmt.executeUpdate();

				// String insertPatient3 = "insert into patient(name, level) values('prof', 0)";
				stmt.setString(1, "prof");
				stmt.setInt(2, 0);
				stmt.executeUpdate();
			}
		 if(input.equals("getAll")) {
			 try (Statement stmt = con.createStatement()) {
					try (ResultSet resultSet = stmt.executeQuery("select * from student")) {
						while (resultSet.next()) {
//								Integer id = resultSet.getInt(1);
//								String name = resultSet.getString(2);
//								Integer level = resultSet.getInt(3);
							//String name = resultSet.getString("name");
							//Integer level = resultSet.getInt("level");
							Integer id = resultSet.getInt("id");
							String promotion = resultSet.getString("promotion");
							Boolean isdelegate = resultSet.getBoolean("isdelegate");
							String firstname = resultSet.getString("firstname");
							String lastname = resultSet.getString("lastname");
							String mail = resultSet.getString("mail");
							String phonenumber = resultSet.getString("phonenumber");
							Integer nbmiss = resultSet.getInt("nbmiss");
							
							System.out.println("id: " + id + ", promotion: " + promotion + ", isdelegate: " + isdelegate + "firstname: " + firstname + "lastname: " + lastname + "mail: " + mail + "phoneumber: " + phonenumber + "nbmiss: " + nbmiss );
						}
					}
				}	
            }
			
		}
	}
}
