package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class JdbcLauncher {
    private static final String SHOW_TABLES = "show tables";

    private static final String CREATE_TABLE_STUDENT = "create table student(id int not null  auto_increment primary key,"
            + "promotion varchar(255) not null," + "is_delegate boolean not null," + "firstname varchar(255) not null,"
            + "lastname varchar(255) not null," + "mail varchar(255) not null," + "phonenumber varchar(255) not null," + "nbmiss int not null)";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
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
            System.out.print("Enter the command you want to do (getAll, getById, create, delete, averagemiss) : ");
            String input = reader.readLine();

            if(input.equals("create")) {
                //create the input Student and add his attributs
                Student studentInput = new Student();
                addStudentAttributsByInput(reader, studentInput);
                addStudentInDatabase(con, studentInput);
            }
            
            if(input.equals("delete")) {
                deleteStudentInDatabase(con, reader);

            }
            if(input.equals("getAll")) {
                getAllStudents(con);
            }
            if(input.equals("averagemiss")) {
            	averageMiss(con, reader);
            }
        }


    }
    
    private static void averageMiss(Connection con, BufferedReader reader) throws SQLException, IOException {
    	  	Statement stmt4 = con.createStatement();
    	    String query5 =  "SELECT AVG(nbmiss) AS average_nbmiss FROM student;";
    	    ResultSet resultSet3 = stmt4.executeQuery(query5);
    	    if (resultSet3.next()) {
    	    	double averageNbMiss = resultSet3.getDouble("average_nbmiss");
    	    	System.out.println("The average of missing students is: " + averageNbMiss);
    	    }
    	    
   }

    
    private static void deleteStudentInDatabase(Connection con, BufferedReader reader) throws SQLException, IOException {
        Student studentDelete = new Student();
        System.out.print("Enter the firstname: ");
        String firstname2 = reader.readLine();
        studentDelete.setFirstname(firstname2);

        System.out.print("Enter the lastname: ");
        String lastname2 = reader.readLine();
        studentDelete.setLastname(lastname2);
        
        Statement stmt4 = con.createStatement();
        String query2 = "SELECT * FROM student WHERE firstname = '" + firstname2 + "' and lastname = '" + lastname2 + "'";
        ResultSet resultSet2 = stmt4.executeQuery(query2);
       
        
        while (resultSet2.next()) {
            if (resultSet2.getBoolean("is_delegate")== false) {
                try (PreparedStatement stmt3 = con.prepareStatement("DELETE FROM student WHERE firstname = ? and lastname = ? ")) {
                    stmt3.setString(1, studentDelete.getFirstname());
                    stmt3.setString(2, studentDelete.getLastname());
                    stmt3.executeUpdate();
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
            //to create a student we verify in all students table if the  firstname and (&&) lastname or (||) mail are equals or not and set the boolean alreadyexist
            if(resultSet.getString("mail").equals(studentInput.getMail())|| resultSet.getString("firstname").equals(studentInput.getFirstname())&& resultSet.getString("lastname").equals(studentInput.getLastname())){
                System.out.println("the student has already been register");
                alreadyExist=true;
            }
        }
        if (alreadyExist==false) {

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