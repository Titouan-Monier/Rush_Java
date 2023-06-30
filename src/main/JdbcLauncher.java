package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class JdbcLauncher {
    private static final String SHOW_TABLES = "show tables";
    //String SQL Query to Create the table student
    private static final String CREATE_TABLE_STUDENT = "create table student(id int not null  auto_increment primary key,"
            + "promotion varchar(255) not null," + "is_delegate boolean not null," + "firstname varchar(255) not null,"
            + "lastname varchar(255) not null," + "mail varchar(255) not null," + "phonenumber varchar(255) not null," + "nbmiss int not null)";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Establish connection with port 3306 from database "testjava", with the user root and no password
        try (Connection con = DriverManager.getConnection("jdbc:mySQL://localhost:3306/testjava", "root", "")) {
        	//Create the statement connection
            try (Statement stmt = con.createStatement()) {
            	//initiate boolean studenTabletoRemove
                boolean studentTableToRemove = false;
                try (ResultSet resultSet = stmt.executeQuery(SHOW_TABLES)) {
                    while (resultSet.next()) {
                        String tableName = resultSet.getString(1);
                        System.out.println("table name: " + tableName);

                        //verify if the table "student" already exist (compare with the java method "equals(String)"
                        if ("student".equals(tableName)) {
                            studentTableToRemove = true;
                            break;
                        }
                        // else we create the table
                        else {
                            studentTableToRemove = false;
                        }
                    }
                }
                //then if boolean is false we create the table "student"
                if (!studentTableToRemove) {
                    System.out.println("student table creation");
                    stmt.execute(CREATE_TABLE_STUDENT);
                }
            }
            
            //First console Log to get the command the user wants to execute 
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            //Propose the CRUD's methods to the user 
            System.out.print("Enter the command you want to do (getAll, create, delete, averagemiss, sortByFirstname, sortByNbmiss, getPromo) : ");
            String input = reader.readLine();

            if(input.equals("create")) {
                //create the input Student and add his attributes
                Student studentInput = new Student();
                addStudentAttributsByInput(reader, studentInput);
                addStudentInDatabase(con, studentInput);
            }
            //if the user write any of the equals strings, it applies the following methods using con (connection to the dbb) and reader(to read the user input) 
            if(input.equals("delete")) {
                deleteStudentInDatabase(con, reader);

            }
            if(input.equals("getAll")) {
                getAllStudents(con);
            }
            if(input.equals("averagemiss")) {
            	averageMiss(con, reader);
            }
            if(input.equals("sortByFirstname")) {
            	sortByFirstname(con,reader);
            }
            if(input.equals("getPromo")) {
            	getPromo(con,reader);
            }
            if(input.equals("sortByNbmiss")) {
            	sortByNbmiss(con,reader);
            }
        }


    }
    
    private static void getPromo(Connection con, BufferedReader reader) throws SQLException, IOException {
	        System.out.print("Please, Enter the Promotion: ");
	        //read the entered promotion 
	        String readPromotion = reader.readLine();
	        Statement stmt = con.createStatement();
	        //SQL query that selects all from the student table where the column promotion = the writing promotion the user wrote
		    String query =  "SELECT * FROM student WHERE promotion = '" + readPromotion + "'" ;
		    ResultSet resultSet = stmt.executeQuery(query);
		    //Iteration on each student 
	        while(resultSet.next()) {
	        	////Call the constructor Student to get in the attributes then show them in console
	        	Student studentToDisplay = new Student(resultSet.getString("promotion"),resultSet.getBoolean("is_delegate"),resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getString("mail"),resultSet.getString("phonenumber"),resultSet.getInt("nbmiss"));
	        	System.out.println(studentToDisplay);
	    }
    }
    
    private static void sortByFirstname(Connection con, BufferedReader reader) throws SQLException, IOException {
	  	Statement stmt = con.createStatement();
	  	//SQL query that select all from the table student and sorts by the firstname in ascendant order
	    String query =  "SELECT * FROM student ORDER BY firstname ASC";
	    ResultSet resultSet = stmt.executeQuery(query);
	    while(resultSet.next()) {
	    	//Call the constructor Student to get in the attributes then show them in console
	    	Student studentToSort = new Student(resultSet.getString("promotion"),resultSet.getBoolean("is_delegate"),resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getString("mail"),resultSet.getString("phonenumber"),resultSet.getInt("nbmiss"));
        	System.out.println(studentToSort);
	    }
    }
    
    private static void sortByNbmiss(Connection con, BufferedReader reader) throws SQLException, IOException {
	  	Statement stmt = con.createStatement();
	  	//SQL query that sorts the nbmis by descendant order
	    String query =  "SELECT * FROM student ORDER BY nbmiss DESC";
	    ResultSet resultSet = stmt.executeQuery(query);
	    while(resultSet.next()) {
	    	//Call the constructor Student to get in the attributes then show them in console
	    	Student studentToSort = new Student(resultSet.getString("promotion"),resultSet.getBoolean("is_delegate"),resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getString("mail"),resultSet.getString("phonenumber"),resultSet.getInt("nbmiss"));
        	System.out.println(studentToSort);
	    }
    }
    
    private static void averageMiss(Connection con, BufferedReader reader) throws SQLException, IOException {
    	  	Statement stmt = con.createStatement();
    	  	//SQL query that calculates the average of nbmiss as the value average_nbmiss from all the student table
    	    String query =  "SELECT AVG(nbmiss) AS average_nbmiss FROM student;";
    	    ResultSet resultSet = stmt.executeQuery(query);
    	    if (resultSet.next()) {
    	    	//set the double(a float like) value and get it from average_nbmiss 
    	    	double averageNbMiss = resultSet.getDouble("average_nbmiss");
    	    	System.out.println("The average of missing students is: " + averageNbMiss);
    	    }
    	    
   }

    
    private static void deleteStudentInDatabase(Connection con, BufferedReader reader) throws SQLException, IOException {
        Student studentDelete = new Student();
        System.out.print("Enter the firstname: ");
        String firstname = reader.readLine();
        studentDelete.setFirstname(firstname);

        System.out.print("Enter the lastname: ");
        String lastname = reader.readLine();
        studentDelete.setLastname(lastname);
        
        Statement stmt1 = con.createStatement();
        // SQL query that select all data from student where the firstname/lastname = the entered name 
        String query1 = "SELECT * FROM student WHERE firstname = '" + firstname + "' and lastname = '" + lastname + "'";
        ResultSet resultSet1 = stmt1.executeQuery(query1);
       
        
        while (resultSet1.next()) {
            if (resultSet1.getBoolean("is_delegate")== false) {
            	//SQL query to delete all from table "student", it's a prepared statement (safer)
                try (PreparedStatement stmt2 = con.prepareStatement("DELETE FROM student WHERE firstname = ? and lastname = ? ")) {
                	//add the values to the query
                    stmt2.setString(1, studentDelete.getFirstname());
                    stmt2.setString(2, studentDelete.getLastname());
                    stmt2.executeUpdate();
                    System.out.println(studentDelete.getFirstname() + " " + studentDelete.getLastname() + " has been deleted");
                }
            } else {
                System.out.println("You cannot delete a Delegate");
            }
        }
    } 
    
    private static void addStudentInDatabase(Connection con, Student studentInput) throws SQLException {
        Statement stmt = con.createStatement();
        String query = "SELECT * FROM " + "student";
        ResultSet resultSet = stmt.executeQuery(query);
        boolean alreadyExist = false;
        while(resultSet.next()) {
            //to create a student we verify in all students table if the  firstname and (&&) lastname or (||) mail are equals or not and set the boolean alreadyExist
            if(resultSet.getString("mail").equals(studentInput.getMail())|| resultSet.getString("firstname").equals(studentInput.getFirstname())&& resultSet.getString("lastname").equals(studentInput.getLastname())){
                System.out.println("the student has already been register");
                alreadyExist=true;
            }
        }
        if (alreadyExist==false) {
        	//SQL query that Create a student in database 
            try (PreparedStatement stmt2 = con.prepareStatement("INSERT INTO student"
                    + "(promotion, is_delegate, firstname, lastname, mail, phonenumber, nbmiss)"
                    +" VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt2.setString(1, studentInput.getPromotion());
                stmt2.setBoolean(2, studentInput.getIs_delegate());
                stmt2.setString(3, studentInput.getFirstname());
                stmt2.setString(4, studentInput.getLastname());
                stmt2.setString(5, studentInput.getMail());
                stmt2.setString(6, studentInput.getPhonenumber());
                stmt2.setInt(7, studentInput.getNbmiss());
                stmt2.executeUpdate();

                System.out.println("You have create: " + studentInput.getFirstname() + " " + studentInput.getLastname() + " to the database.");
            }
        }
    }

    private static void addStudentAttributsByInput(BufferedReader reader, Student studentInput) throws IOException {
        System.out.print("Enter the promotion: ");
        String promotion = reader.readLine();
        studentInput.setPromotion(promotion);

        System.out.print("Enter the is_delegate (true/false): ");
        String isdelegate = reader.readLine();
        studentInput.setIs_delegate(Boolean.parseBoolean(isdelegate));

        System.out.print("Enter the firstname: ");
        String firstname = reader.readLine();
        studentInput.setFirstname(firstname);

        System.out.print("Enter the lastname: ");
        String lastname = reader.readLine();
        studentInput.setLastname(lastname);

        System.out.print("Enter the mail: ");
        String mail = reader.readLine();
        studentInput.setMail(mail);

        System.out.print("Enter the phonenumber: ");
        String phonenumber = reader.readLine();
        studentInput.setPhonenumber(phonenumber);

        System.out.print("Enter the nbmiss: ");
        String nbmiss = reader.readLine();
        studentInput.setNbmiss(Integer.parseInt(nbmiss));
    }


    private static void getAllStudents(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
        	//SQL query that selects all data from table student, then show them
            try (ResultSet resultSet = stmt.executeQuery("select * from student")) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String promotion = resultSet.getString("promotion");
                    Boolean is_delegate = resultSet.getBoolean("is_delegate");
                    String firstname = resultSet.getString("firstname");
                    String lastname = resultSet.getString("lastname");
                    String mail = resultSet.getString("mail");
                    String phonenumber = resultSet.getString("phonenumber");
                    Integer nbmiss = resultSet.getInt("nbmiss");

                    System.out.println("id: " + id + ", promotion: " + promotion + ", isdelegate: " + is_delegate + "firstname: " + firstname + "lastname: " + lastname + "mail: " + mail + "phonenumber: " + phonenumber + "nbmiss: " + nbmiss );
                }
            }
        }
    }
}