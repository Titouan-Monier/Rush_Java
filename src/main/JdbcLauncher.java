package main;

import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class JdbcLauncher {
	private static final String SHOW_TABLES = "show tables";
	
	private static final String CREATE_TABLE_STUDENT = "create table student(id int not null  auto_increment primary key,"
            + "promotion varchar(255) not null," + "is_delegate boolean not null," + "firstname varchar(255) not null,"
            + "lastname varchar(255) not null," + "mail varchar(255) not null," + "phonenumber varchar(255) not null," + "nbmiss int not null)";
            
	

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException  {
		Class.forName("com.mysql.cj.jdbc.Driver");
		try (Connection con = DriverManager.getConnection("jdbc:mySQL://localhost:3306/testjava", "root", "")) {
			try (Statement stmt = con.createStatement()) {
				
                boolean studentTableToRemove = false;
                try (ResultSet resultSet = stmt.executeQuery(SHOW_TABLES)) {
                    while (resultSet.next()) {
                        String tableName = resultSet.getString(1);
                        System.out.println("table name: " + tableName);

                        //vérifie que la table student existe bien
                        if ("student".equals(tableName)) {
                            studentTableToRemove = true;
                            break;
                        } 
                        // sinon on la crée automatiquement
                        else {
                            studentTableToRemove = false;
                        }
                    }
                }

                if (!studentTableToRemove) {
                    System.out.println("student table creation");
                    stmt.execute(CREATE_TABLE_STUDENT);
                }
            }
			//Fait un premier console Log pour récupérer la commande que l'utilisateur veut executer
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the command you want to do (getAll, getById, create) : ");
            String input = reader.readLine();
           
		
			
		 if(input.equals("create")) {
			 //create the input array of student and add them in
			 ArrayList studentInput = new ArrayList<>();
			 
			 System.out.print("Enter the promotion: ");
	         String promotion = reader.readLine();
	         studentInput.add(promotion);
	         
	         System.out.print("Enter the is_delegate (true/false): ");
	         String isdelegate = reader.readLine();
	         if (isdelegate.equals("false")){
	        	 studentInput.add(false);
	         }else {
	         studentInput.add(true);
	         }
	         
	         System.out.print("Enter the firstname: ");
	         String firstname = reader.readLine();
	         studentInput.add(firstname);
	         
	         System.out.print("Enter the lastname: ");
	         String lastname = reader.readLine();
	         studentInput.add(lastname);
	         
	         System.out.print("Enter the mail: ");
	         String mail = reader.readLine();
	         studentInput.add(mail);
	         
	         System.out.print("Enter the phonenumber: ");
	         String phonenumber = reader.readLine();
	         studentInput.add(phonenumber);
	         
	         System.out.print("Enter the nbmiss: ");
	         String nbmiss = reader.readLine();
	         studentInput.add(nbmiss);
	         
	       //using for loop

             
    for (int i = 0; i < studentInput.size();i++) 
    { 		      
        System.out.println(studentInput.get(i)); 		
    }   
	         
			 Statement stmt = con.createStatement();
			 String query = "SELECT * FROM " + "student";
	         ResultSet resultSet = stmt.executeQuery(query);
			 boolean alreadyExist = false;
	         while(resultSet.next()) {
	        	//to create a student we verify all student if the  firstname and (&&) lastname or (||) mail are equals or not with the boolean alreadyexist
				if(resultSet.getString("mail").equals(studentInput.get(4))|| resultSet.getString("firstname").equals(studentInput.get(2))&& resultSet.getString("lastname").equals(studentInput.get(3))){
					 System.out.println("the student has already been register");
					 alreadyExist=true;
				 }
			
	            

	         }
	         if (alreadyExist==false) {
	        	 
                 try (PreparedStatement stmt2 = con.prepareStatement("INSERT INTO student"
                         + "(promotion, is_delegate, firstname, lastname, mail, phonenumber, nbmiss)"
                         +" VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                	 stmt2.setString(1,(String) studentInput.get(0));
                     stmt2.setBoolean(2, (boolean) studentInput.get(1));
                     stmt2.setString(3, (String) studentInput.get(2));
                     stmt2.setString(4, (String) studentInput.get(3));
                     stmt2.setString(5, (String) studentInput.get(4));
                     stmt2.setString(6, (String) studentInput.get(5));
                     stmt2.setInt(7,Integer.parseInt((String) studentInput.get(6)));
                     stmt2.executeUpdate();

                     System.out.println("You have create: " + studentInput.get(2) + " " + studentInput.get(3) + " to the database.");
                 }

                 
         
         }
	        	 
	         
			 
		 }
		 if(input.equals("getAll")) {
			 getAllStudents(con);	
            }
			
		}
	}



	private static void getAllStudents(Connection con) throws SQLException {
		try (Statement stmt = con.createStatement()) {
				try (ResultSet resultSet = stmt.executeQuery("select * from student")) {
					while (resultSet.next()) {
						Integer id = resultSet.getInt("id");
						String promotion = resultSet.getString("promotion");
						Boolean isdelegate = resultSet.getBoolean("isdelegate");
						String firstname = resultSet.getString("firstname");
						String lastname = resultSet.getString("lastname");
						String mail = resultSet.getString("mail");
						String phonenumber = resultSet.getString("phonenumber");
						Integer nbmiss = resultSet.getInt("nbmiss");
						
						System.out.println("id: " + id + ", promotion: " + promotion + ", isdelegate: " + isdelegate + "firstname: " + firstname + "lastname: " + lastname + "mail: " + mail + "phonenumber: " + phonenumber + "nbmiss: " + nbmiss );
					}
				}
			}
	}
}
