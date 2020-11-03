import oracle.jdbc.driver.*;
import oracle.jdbc.proxy.annotation.Pre;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Connection_Test {
    public static int CompareString(String action) {
        if (action.equalsIgnoreCase("ID")) return 1;
        if (action.equalsIgnoreCase("Name")) return 2;
        if (action.equalsIgnoreCase("Department")) return 3;
        if (action.equalsIgnoreCase("Address")) return 4;
        if (action.equalsIgnoreCase("Birthdate")) return 5;
        if (action.equalsIgnoreCase("Gender")) return 6;
        return 0;
    }

    public static int getAction() {
        System.out.println("What do you want to do? \n1. Modify Personal Information 2. List all courses available 3. List all courses registered 4. register courses ");
        System.out.println("Enter the corresponding number (Enter -1 to quit):");
        Scanner scanner = new Scanner(System.in);
        int action = scanner.nextInt();
        return action;
    }

    public static boolean Welcomestudent(OracleConnection conn, int userid) throws SQLException, IOException {
        String stmt1 = "SELECT STUDENT_NAME FROM STUDENTS WHERE STUDENT_ID=?";
        PreparedStatement stmt = conn.prepareStatement(stmt1);
        stmt.setInt(1, userid);
        ResultSet rset = stmt.executeQuery();
        if (rset.next()) {
            System.out.println("Welcome, " + rset.getString(1));
            return true;
        } else {
            return false;
        }
    }

    public static void MainFrame(OracleConnection conn, int action, int userid) throws SQLException, IOException {
        switch (action) {
            case 1:
                String stmt1 = "SELECT * FROM STUDENTS WHERE STUDENT_ID=?";
                PreparedStatement stmt = conn.prepareStatement(stmt1);
                stmt.setInt(1, userid);
                ResultSet rset = stmt.executeQuery();
                System.out.println("Your personal information:");
                while (rset.next()) {
                    System.out.print("ID: " + rset.getString(1) + "\nName: " + rset.getString(2) + "\nDepartment: " +
                            rset.getString(3) + "\nAddress: " +
                            rset.getString(4) + "\nBirthdate: " + rset.getDate(5) + "\nGender: " + rset.getString(6));
                }
                System.out.println("\nWhat information do you want to modify?(Enter Quit to go back)");
                Scanner scanner2 = new Scanner(System.in);
                String action2 = scanner2.nextLine();
                if (action2.equalsIgnoreCase("Quit")) return;
                switch (CompareString(action2)) {
                    case 1:
                        System.out.println("You don't have the permission to change your id");
                        return;
                    case 2:
                        System.out.println("Enter your new name:");
                        String action_S = scanner2.nextLine();
                        String stmt2 = "UPDATE STUDENTS SET STUDENT_NAME=? WHERE STUDENT_ID=?";
                        stmt = conn.prepareStatement(stmt2);
                        stmt.setString(1, action_S);
                        stmt.setInt(2, userid);
                        stmt.executeUpdate();
                        System.out.println("You have updated your Name");
                        return;
                    case 3:
                        System.out.println("You don't have the permission to change the department you belong to");
                        return;
                    case 4:
                        System.out.println("Enter the new address: (Enter Quit to logout)");
                        action2 = scanner2.nextLine();
                        if (action2.equalsIgnoreCase("Quit")) return;
                        stmt2 = "UPDATE STUDENTS SET ADDRESS=? WHERE STUDENT_ID =?";
                        stmt = conn.prepareStatement(stmt2);
                        stmt.setString(1, action2);
                        stmt.setInt(2, userid);
                        stmt.executeUpdate();
                        System.out.println("The address has been updated");
                        return;
                    case 5:
                        System.out.println("You don't have the permission to change your birthday");
                        return;
                    case 6:
                        System.out.println("You don't have the permission to change your gender");
                        return;

                }
                return;
            case 2:
                stmt1 = "SELECT * FROM COURSES";
                stmt = conn.prepareStatement(stmt1);
                rset = stmt.executeQuery();
                while (rset.next()) {
                    System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) + "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                }
                return;
            case 3:
                stmt1 = "SELECT COURSE_ID, REG_DATE, GRADE FROM ENROLLMENT WHERE ENROLLMENT.STUDENT_ID=?";
                stmt = conn.prepareStatement(stmt1);
                stmt.setInt(1, userid);
                rset = stmt.executeQuery();
                if (rset.next()) {
                    System.out.println("Course ID:" + rset.getString(1) + "\nRegistration Date:" + rset.getDate(2) + "\nGrade:" + rset.getInt(3) + "\n");
                    while (rset.next()) {
                        System.out.println("Course ID:" + rset.getString(1) + "\nRegistration Date:" + rset.getDate(2) + "\nGrade:" + rset.getInt(3) + "\n");
                    }
                } else {
                    System.out.println("No course is registerted!");
                }
                return;
            case 4:
                boolean Flag = false;
                while (!Flag) {
                    System.out.println("Enter the course id you want to register (Enter Quit to go back):");
                    Scanner scanner3 = new Scanner(System.in);
                    String action3 = scanner3.nextLine();
                    if (action3.equalsIgnoreCase("Quit")) return;
                    stmt1 = "SELECT * FROM COURSES WHERE COURSE_ID=?";
                    stmt = conn.prepareStatement(stmt1);
                    stmt.setString(1, action3);
                    rset = stmt.executeQuery();
                    System.out.println("Your chosen course information is as below:");
                    while (rset.next()) {
                        System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) +
                                "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                    }
                    System.out.println("Is this the course you want to register? (Yes or No)");
                    Scanner scanner4 = new Scanner(System.in);
                    String action4 = scanner4.nextLine();
                    boolean Flag2 = false;
                    while (!Flag2) {
                        if (action4.equalsIgnoreCase("Yes")) {
                            Calendar calendar = Calendar.getInstance();
                            java.util.Date currentDate = calendar.getTime();
                            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
                            String stmt2 = "INSERT INTO ENROLLMENT VALUES(?,?,?,?)";
                            stmt = conn.prepareStatement(stmt2);
                            String userstring = String.valueOf(userid);
                            stmt.setString(1, userstring);
                            stmt.setString(2, action3);
                            stmt.setDate(3, sqlDate);
                            stmt.setInt(4, 0);
                            stmt.executeUpdate();
                            System.out.println("You have registered the course");
                            Flag2 = true;
                        } else if (action4.equalsIgnoreCase("No")) {
                            System.out.println("Please enter the correct course ID:");
                            action3 = scanner3.nextLine();
                            stmt1 = "SELECT * FROM COURSES WHERE COURSE_ID=?";
                            stmt = conn.prepareStatement(stmt1);
                            stmt.setString(1, action3);
                            rset = stmt.executeQuery();
                            System.out.println("Your chosen course information is as below:");
                            while (rset.next()) {
                                System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) +
                                        "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                            }
                            System.out.println("Is this the course you want to register? (Yes or No)");
                            action4 = scanner4.nextLine();
                        }
                    }
                    System.out.println("Do you have other course to register (Yes or No)");
                    action4 = scanner4.nextLine();
                    if (action4.equalsIgnoreCase("No")) {
                        Flag = true;
                    }
                }
                return;
        }
    }

    public static void AdminWelcome() {
        System.out.println("Welcome Admin, what do you want to do?");

    }

    public static int getaction_admin() {
        System.out.println("1.list all the courses  and students  2.list all the students in the department 3.add a new course or student\n" +
                "4.modify the information of courses or students 5.delete a course or a student 6.modify the grade of a student for one of his/her registered courses");
        Scanner scanner = new Scanner(System.in);
        int action = scanner.nextInt();
        return action;
    }

    public static void MainFrame_Admin(OracleConnection conn, int action) throws SQLException, IOException {
        switch (action) {
            case 1: {
                ListALL_admin(conn);
                return;
            }
            case 2: {
                ListAll_department(conn);
                return;
            }
            case 3: {
                Add_student_course(conn);
                return;
            }
            case 4: {
                ModifyInformation(conn);
                return;
            }
            case 5: {
                Delete_student_course(conn);
                return;
            }
            case 6: {
                ModifyGrade_Admin(conn);
                return;
            }
        }
    }

    public static void ListALL_admin(OracleConnection conn) throws SQLException, IOException {
        System.out.println("You want to list students or courses? (Enter Quit to go back)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Quit")) return;
        if (choice.equalsIgnoreCase("students")) {
            String statement = "SELECT DISTINCT * FROM STUDENTS";
            PreparedStatement stmt = conn.prepareStatement(statement);
            ResultSet rset = stmt.executeQuery();
            int count = 1;
            while (rset.next()) {
                System.out.println(count + ".");
                System.out.println(rset.getString(1));
                System.out.println(rset.getString(2));
                System.out.println(rset.getString(3));
                System.out.println(rset.getString(4));
                System.out.println(rset.getDate(5));
                System.out.println(rset.getString(6) + "\n");
                count++;
            }
            System.out.println("Do you want to list all courses? (Yes or No)");
            String Boolean = scanner.nextLine();
            if (Boolean.equalsIgnoreCase("Yes")) {
                statement = "SELECT DISTINCT * FROM COURSES";
                stmt = conn.prepareStatement(statement);
                rset = stmt.executeQuery();
                count = 1;
                while (rset.next()) {
                    System.out.println(count + ".");
                    System.out.println(rset.getString(1));
                    System.out.println(rset.getString(2));
                    System.out.println(rset.getString(3));
                    System.out.println(rset.getString(4));
                    count++;
                }
            }
        } else if (choice.equalsIgnoreCase("courses")) {
            String statement = "SELECT DISTINCT * FROM COURSES";
            PreparedStatement stmt = conn.prepareStatement(statement);
            ResultSet rset = stmt.executeQuery();
            int count = 1;
            while (rset.next()) {
                System.out.println(count + ".");
                System.out.println(rset.getString(1));
                System.out.println(rset.getString(2));
                System.out.println(rset.getString(3));
                System.out.println(rset.getString(4));
                count++;
            }
            System.out.println("Do you want to list all students? (Yes or No)");
            String Boolean = scanner.nextLine();
            if (Boolean.equalsIgnoreCase("yes")) {
                statement = "SELECT DISTINCT * FROM STUDENTS";
                stmt = conn.prepareStatement(statement);
                rset = stmt.executeQuery();
                count = 1;
                while (rset.next()) {
                    System.out.println(count + ".");
                    System.out.println(count + "." + rset.getString(1));
                    System.out.println(rset.getString(2));
                    System.out.println(rset.getString(3));
                    System.out.println(rset.getString(4));
                    System.out.println(rset.getDate(5));
                    System.out.println(rset.getString(6) + "\n");
                    count++;
                }
            }
        }
    }

    public static void ListAll_department(OracleConnection conn) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        boolean FLAG = false;
        while (!FLAG) {
            System.out.println("Which department do you want to show? (Enter Quit to go back)");
            String department = scanner.nextLine();
            if (department.equalsIgnoreCase("Quit")) return;
            String statement = "SELECT * FROM STUDENTS WHERE DEPARTMENT= ? ";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setString(1, department);
            ResultSet rset = stmt.executeQuery();
            int count = 1;
            if (rset.next()) {
                System.out.println(count + ".");
                System.out.println(rset.getString(1));
                System.out.println(rset.getString(2));
                System.out.println(rset.getString(3));
                System.out.println(rset.getString(4));
                System.out.println(rset.getDate(5));
                System.out.println(rset.getString(6)+"\n");
                count++;
                while (rset.next()) {
                    System.out.println(count + ".");
                    System.out.println(rset.getString(1));
                    System.out.println(rset.getString(2));
                    System.out.println(rset.getString(3));
                    System.out.println(rset.getString(4));
                    System.out.println(rset.getDate(5));
                    System.out.println(rset.getString(6)+"\n");
                    count++;
                }
            } else {
                System.out.println("No result");
            }
            System.out.println("Do you need to list student from  other deparments? (Yes or No)");
            String yesorno = scanner.nextLine();
            boolean inputerror = true;
            while (inputerror) {
                if (yesorno.equalsIgnoreCase("yes")) {
                    FLAG = false;
                    inputerror = false;
                } else if (yesorno.equalsIgnoreCase("no")) {
                    FLAG = true;
                    inputerror = false;
                } else {
                    System.out.println("Please input correctly:");
                    yesorno = scanner.nextLine();
                }
            }
        }
    }

    public static void ModifyInformation(OracleConnection conn) throws SQLException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You want to modify course or student information? (Enter quit to go back)");
        String Modify = scanner.nextLine();
        if (Modify.equalsIgnoreCase("Quit")) return;
        int whose = 0;
        if (Modify.equalsIgnoreCase("student")) {
            boolean End = false;
            System.out.println("You want to modify whose information? (Enter his/her student id)");
            while (!End) {
                whose = scanner.nextInt();
                String statement = " SELECT * FROM STUDENTS WHERE STUDENT_ID = ?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setInt(1, whose);
                ResultSet rset = stmt.executeQuery();
                if (rset.next()) {
                    System.out.println("Current Information");
                    System.out.println(rset.getString(1));
                    System.out.println(rset.getString(2));
                    System.out.println(rset.getString(3));
                    System.out.println(rset.getString(4));
                    System.out.println(rset.getDate(5));
                    System.out.println(rset.getString(6));
                    while (rset.next()) {
                        System.out.println(rset.getString(1));
                        System.out.println(rset.getString(2));
                        System.out.println(rset.getString(3));
                        System.out.println(rset.getString(4));
                        System.out.println(rset.getDate(5));
                        System.out.println(rset.getString(6));
                    }
                    End = true;
                } else {
                    System.out.println("No this person, please try again:");
                    continue;
                }
                System.out.println("What information do you want to modify?");
                System.out.println("1.ID 2.Name 3.Department 4. Address 5.Birthday 6.Gender");
                int Type = scanner.nextInt();
                ModifyPersonalInfo_Admin(conn, Type, whose);
                System.out.println("Do you have to modify other information? (Yes or No)");
                Scanner scanner2 = new Scanner(System.in);
                String Continue = scanner2.nextLine();
                if (Continue.equalsIgnoreCase("YES")) End = false;
                else if (Continue.equalsIgnoreCase("No")) End = true;
            }
        } else if (Modify.equalsIgnoreCase("course")) {
            boolean End = false;
            System.out.println("Which course do you want to modify");
            while (!End) {
                String course_Modify = scanner.nextLine();
                String statement = "SELECT * FROM COURSES WHERE COURSE_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, course_Modify);
                ResultSet rset = stmt.executeQuery();
                if (rset.next()) {
                    System.out.println("Current Information");
                    System.out.println(rset.getString(1));
                    System.out.println(rset.getString(2));
                    System.out.println(rset.getString(3));
                    System.out.println(rset.getString(4));
                    while (rset.next()) {
                        System.out.println(rset.getString(1));
                        System.out.println(rset.getString(2));
                        System.out.println(rset.getString(3));
                        System.out.println(rset.getString(4));
                    }
                    End=true;
                }
                else{
                    System.out.println("Incorrect Course ID ");
                    System.out.println("Please enter correct COURSE_ID:");
                    End=false;
                }
                boolean End2=false;
                while(!End2&&End) {
                    System.out.println("which information do you want to modify?");
                    System.out.println("1.COURSE_ID 2.Course_Title 3.STAFF_NAME 4.SECTION");
                    int option = scanner.nextInt();
                    ModifyCourseInformation_admin(conn, option, course_Modify);
                    System.out.println("Do you have to modify other information? (Yes or No)");
                    Scanner temp = new Scanner(System.in);
                    String Continue = temp.nextLine();
                    if (Continue.equalsIgnoreCase("YES")) End2 = false;
                    else if (Continue.equalsIgnoreCase("No")) End2 = true;
                }
            }
        }

    }

    public static void ModifyPersonalInfo_Admin(OracleConnection conn, int type, int oldid) throws SQLException, IOException {
        switch (type) {
            case 1: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new id:");
                int newid = scanner.nextInt();
                String statement = " UPDATE STUDENTS SET STUDENT_ID= ? WHERE STUDENT_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setInt(1, newid);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have updated the student id");
                return;
            }
            case 2: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new name:");
                String newname = scanner.nextLine();
                String statement = " UPDATE STUDENTS SET STUDENT_NAME= ? WHERE STUDENT_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, newname);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have updated the student name");
                return;
            }
            case 3: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new address:");
                String newaddres = scanner.nextLine();
                String statement = " UPDATE STUDENTS SET ADDRESS= ? WHERE STUDENT_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, newaddres);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have updated the student address");
                return;
            }
            case 4: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new department:");
                String newdepartment = scanner.nextLine();
                String statement = " UPDATE STUDENTS SET DEPARTMENT= ? WHERE STUDENT_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, newdepartment);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have updated the student address");
                return;
            }
            case 5: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new date (YYYY-MM-DD)");
                String newdate = scanner.nextLine();
                java.sql.Date date = java.sql.Date.valueOf(newdate);
                String statement = " UPDATE STUDENTS SET BIRTHDATE = ? WHERE STUDENT_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setDate(1, date);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have updated the student birthdate");
                return;
            }
            case 6: {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter the new gender (Female / Male):");
                String newgender = scanner.nextLine();
                String statement = "UPDATE STUDENTS SET GENDER=? WHERE STUDENT_ID=?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, newgender);
                stmt.setInt(2, oldid);
                stmt.executeUpdate();
                System.out.println("You have upated the student gender");
                return;
            }
        }
    }

    public static void ModifyCourseInformation_admin(OracleConnection conn, int option, String courseid) throws SQLException {
        switch (option) {
            case 1: {
                System.out.println("Please enter the new course id:");
                Scanner scanner = new Scanner(System.in);
                String courseid_new = scanner.nextLine();
                String statement = " UPDATE COURSES SET COURSE_ID=? WHERE COURSE_ID =?";
                String statement2 = "UPDATE ENROLLMENT SET COURSE_ID =? WHERE COURSE_ID=?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                PreparedStatement stmt2 = conn.prepareStatement(statement2);
                stmt.setString(1, courseid_new);
                stmt.setString(2, courseid);
                stmt2.setString(1, courseid_new);
                stmt2.setString(2, courseid);
                stmt.executeUpdate();
                stmt2.executeUpdate();
                System.out.println("Course id has been updated");
                return;
            }
            case 2: {
                System.out.println("Please enter the new course title");
                Scanner scanner = new Scanner(System.in);
                String coursetitle_new = scanner.nextLine();
                String statement = " UPDATE COURSES SET COURSE_TITLE=? WHERE COURSE_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, coursetitle_new);
                stmt.setString(2, courseid);
                stmt.executeUpdate();
                System.out.println("Course title has been updated");
                return;
            }
            case 3: {
                System.out.println("Please enter the new staff name:");
                Scanner scanner = new Scanner(System.in);
                String staffname = scanner.nextLine();
                String statement = " UPDATE COURSES SET STAFF_NAME=? WHERE COURSE_ID =?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, staffname);
                stmt.setString(2, courseid);
                stmt.executeUpdate();
                System.out.println("Staff name of this course has been updated");
                return;
            }
            case 4: {
                System.out.println("Please enter the new section:");
                Scanner scanner = new Scanner(System.in);
                String newsection = scanner.nextLine();
                String statement = "UPDATE COURSES SET SECTION=? WHERE COURSE_ID=?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, newsection);
                stmt.setString(2, courseid);
                stmt.executeUpdate();
                System.out.println("Section of this course has been updated");
            }
        }
    }

    public static void Add_student_course(OracleConnection conn) throws SQLException {
        System.out.println("what do you want to add course or student? (Enter Quit to go back)");
        Scanner scanner = new Scanner(System.in);
        String add = scanner.nextLine();
        if (add.equalsIgnoreCase("Quit")) return;
        if (add.equalsIgnoreCase("student")) {
            boolean End = false;
            while (!End) {
                System.out.println("Please enter the new student information:");
                System.out.println("STUDENT_ID:");
                String Id = scanner.nextLine();
                System.out.println("STUDENT_NAME:");
                String Name = scanner.nextLine();
                System.out.println("DEPARTMENT:");
                String Department = scanner.nextLine();
                System.out.println("ADDRESS:");
                String Address = scanner.nextLine();
                System.out.println("BIRTHDAY (YYYY-MM-DD):");
                String Birthday = scanner.nextLine();
                java.sql.Date date = java.sql.Date.valueOf(Birthday);
                System.out.println("GENDER:");
                String Gender = scanner.nextLine();
                String statement = " INSERT INTO STUDENTS VALUES(?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, Id);
                stmt.setString(2, Name);
                stmt.setString(3, Department);
                stmt.setString(4, Address);
                stmt.setDate(5, date);
                stmt.setString(6, Gender);
                stmt.executeUpdate();
                System.out.println("You have added a new student.");
                System.out.print("Do you have to add one more new student? (Yes or No)");
                String end = scanner.nextLine();
                if (end.equalsIgnoreCase("Yes")) End = false;
                else if (end.equalsIgnoreCase("No")) End = true;
            }
        } else if (add.equalsIgnoreCase("course")) {
            boolean End = false;
            while (!End) {
                System.out.println("Please enter the new course information:");
                System.out.println("COURSE_ID:");
                String Id = scanner.nextLine();
                System.out.println("COURSE_TITLE:");
                String TITLE = scanner.nextLine();
                System.out.println("STAFF_NAME:");
                String STAFF_NAME = scanner.nextLine();
                System.out.println("SECTION:");
                String SECTION = scanner.nextLine();
                String statement = " INSERT INTO COURSES VALUES(?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, Id);
                stmt.setString(2, TITLE);
                stmt.setString(3, STAFF_NAME);
                stmt.setString(4, SECTION);
                stmt.executeUpdate();
                System.out.println("You have added a new course.");
                System.out.print("Do you have to add one more new course? (Yes or No)");
                String end = scanner.nextLine();
                if (end.equalsIgnoreCase("Yes")) End = false;
                else if (end.equalsIgnoreCase("No")) End = true;
            }
        }
        else{
            System.out.println("Please enter correctly:");
            add=scanner.nextLine();
        }

    }

    public static void Delete_student_course(OracleConnection conn) throws SQLException {
        System.out.println("What do you want to delete? (Enter Quit to go back)");
        Scanner scanner = new Scanner(System.in);
        String delete = scanner.nextLine();
        if (delete.equalsIgnoreCase("Quit")) return;
        if (delete.equalsIgnoreCase("course")) {
            boolean End = false;
            while (!End) {
                System.out.println("Which course do you want to delete? (Enter the course id)");
                String courseid = scanner.nextLine();
                String statement = "DELETE FROM COURSES WHERE COURSE_ID=?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, courseid);
                stmt.executeUpdate();
                System.out.println("The course has been deleted");
                System.out.println("Do you have to delete other courses? (Yes or No)");
                String end = scanner.nextLine();
                if (end.equalsIgnoreCase("Yes")) End = false;
                else if (end.equalsIgnoreCase("No")) End = true;
            }
        } else if (delete.equalsIgnoreCase("student")) {
            boolean End = false;
            while (!End) {
                System.out.println("Which student do you want to delete? (Enter the student id)");
                String studentid = scanner.nextLine();
                String statement = "DELETE FROM STUDENTS WHERE STUDENT_ID=?";
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.setString(1, studentid);
                stmt.executeUpdate();
                System.out.println("The student has been deleted");
                System.out.println("Do you have to delete other students? (Yes or No)");
                String end = scanner.nextLine();
                if (end.equalsIgnoreCase("Yes")) End = false;
                else if (end.equalsIgnoreCase("No")) End = true;
            }
        }
    }

    public static void ModifyGrade_Admin(OracleConnection conn) throws SQLException {
        boolean End = false;
        while (!End) {
            System.out.println("You want to modify grade of which student? (Enter Quit to go back)");
            Scanner scanner = new Scanner(System.in);
            String student = scanner.nextLine();
            if (student.equalsIgnoreCase("Quit")) return;
            String temp="SELECT COURSE_ID,REG_DATE, GRADE FROM ENROLLMENT WHERE STUDENT_ID =?";
            PreparedStatement temp1=conn.prepareStatement(temp);
            temp1.setString(1,student);
            ResultSet rset=temp1.executeQuery();
            if(rset.next()){
                System.out.println("Registered courses: \n");
                System.out.println("Course id: "+rset.getString(1));
                System.out.println("Reg_Day:"+rset.getDate(2));
                System.out.println("Grade:"+rset.getInt(3)+"\n");
                while(rset.next()){
                    System.out.println("Course id:"+rset.getString(1));
                    System.out.println("Reg_Day:"+rset.getDate(2));
                    System.out.println("Grade:"+rset.getInt(3)+"\n");
                }
            }
            else{
                System.out.println("No course registered / No this student");
                break;
            }
            System.out.println("Please enter the course");
            String course = scanner.nextLine();
            System.out.println("Please enter the grade");
            int grade = scanner.nextInt();
            String statement = "UPDATE ENROLLMENT SET GRADE=? WHERE STUDENT_ID=? AND COURSE_ID=?";
            PreparedStatement stmt = conn.prepareStatement(statement);
            stmt.setInt(1, grade);
            stmt.setString(2, student);
            stmt.setString(3, course);
            stmt.executeUpdate();
            System.out.println("You has successfully updated the grade");
            System.out.println("Do you want to modify grade of other courses (Yes or no)");
            Scanner scanner2 = new Scanner(System.in);
            String END = scanner2.nextLine();
            if (END.equalsIgnoreCase("Yes")) End = false;
            else if (END.equalsIgnoreCase("No")) End = true;
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        String username, password;
        username = "\"Student ID\"";
        password = "Passowrd";
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:dbms", username, password);
        String Continue;
        Scanner scanner2 = new Scanner(System.in);
        System.out.println("You are student or admin?");
        Scanner scanner = new Scanner(System.in);
        String LoginType = scanner.nextLine();
        if (LoginType.equalsIgnoreCase("Admin")) {
            AdminWelcome();
            do {
                int action = getaction_admin();
                if(action==-1)break;
                MainFrame_Admin(conn, action);
                System.out.print("Do you have other things to do? (Yes or No)");
                Continue = scanner2.nextLine();
            } while (Continue.equalsIgnoreCase("Yes"));
            System.out.println("Thank you for using our system. GoodBye!");
            conn.close();
        } else {
            System.out.println("Please enter your id");
            int userid = scanner.nextInt();
            while (!Welcomestudent(conn, userid)) {
                System.out.println("Please enter correct student id:");
                userid=scanner.nextInt();
            }
            int action;
            do {
                action = getAction();
                if(action==-1)break;
                MainFrame(conn, action, userid);
                System.out.print("Do you have other things to do? (Yes or No)");
                Continue = scanner2.nextLine();
            } while (Continue.equalsIgnoreCase("Yes"));
            System.out.println("Thank you for using our system. GoodBye!");
            conn.close();
        }
        /*String stmt1="SELECT STUDENT_NAME FROM STUDENTS WHERE STUDENT_ID=?";
        PreparedStatement stmt = conn.prepareStatement(stmt1);
        stmt.setInt(1,userid);
        ResultSet rset = stmt.executeQuery();
        if(rset.next()){
            System.out.println("Welcome, "+ rset.getString(1));
            System.out.println("What do you want to do? \n1. Modify Personal Information 2. List all courses available 3. List all courses registered 4. register courses ");
            System.out.println("Enter the corresponding number:");
            int action=scanner.nextInt();
            switch (action) {
                case 1:
                    stmt1 = "SELECT * FROM STUDENTS WHERE STUDENT_ID=?";
                    stmt = conn.prepareStatement(stmt1);
                    stmt.setInt(1, userid);
                    rset = stmt.executeQuery();
                    System.out.println("Your personal information:");
                    while (rset.next()) {
                        System.out.print("ID: " + rset.getString(1) + "\nName: " + rset.getString(2) + "\nDepartment: " +
                                rset.getString(3) + "\nAddress: " +
                                rset.getString(4) + "\nBirthdate: " + rset.getDate(5) + "\nGender: " + rset.getString(6));
                    }
                    System.out.println("\nWhat information do you want to modify?");
                    Scanner scanner2 = new Scanner(System.in);
                    String action2 = scanner2.nextLine();
                    switch (CompareString(action2)) {
                        case 1:
                            System.out.println("You don't have the permission to change your id");
                            return;
                        case 2:
                            System.out.println("Enter your new name:");
                            String action_S = scanner2.nextLine();
                            String stmt2 = "UPDATE STUDENTS SET STUDENT_NAME=? WHERE STUDENT_ID=?";
                            stmt = conn.prepareStatement(stmt2);
                            stmt.setString(1, action_S);
                            stmt.setInt(2, userid);
                            System.out.println("You have updated your Name");
                            stmt2 = "SELECT STUDENT_NAME FROM STUDENTS WHERE STUDENT_ID=?";
                            stmt = conn.prepareStatement(stmt1);
                            stmt.setInt(1, userid);
                            rset = stmt.executeQuery();
                            if (rset.next()) {
                                System.out.println(rset.getString(1));
                            }
                            return;
                        case 3:
                            System.out.println("You don't have the permission to change the department you belong to");
                            return;
                        case 4:
                            System.out.println("Enter the new address:");
                            action2 = scanner2.nextLine();
                            stmt2 = "UPDATE STUDENTS SET ADDRESS=? WHERE STUDENT_ID =?";
                            stmt = conn.prepareStatement(stmt2);
                            stmt.setString(1, action2);
                            stmt.setInt(2, userid);
                            return;
                        case 5:
                            System.out.println("You don't have the permission to change your birthday");
                            return;
                        case 6:
                            System.out.println("You don't have the permission to change your gender");
                            return;

                    }
                    return;
                case 2:
                    stmt1 = "SELECT * FROM COURSES";
                    stmt = conn.prepareStatement(stmt1);
                    rset = stmt.executeQuery();
                    while (rset.next()) {
                        System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) + "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                    }
                    return;
                case 3:
                    stmt1 = "SELECT COURSE_ID,REG_DATE,GRADE FROM ENROLLMENT WHERE ENROLLMENT.STUDENT_ID=?";
                    stmt = conn.prepareStatement(stmt1);
                    stmt.setInt(1, userid);
                    rset = stmt.executeQuery();
                    while (rset.next()) {
                        System.out.println("Course ID:" + rset.getString(1) + "\nRegistration Date:" + rset.getDate(2) + "\nGrade:" + rset.getInt(3) + "\n");
                    }
                    return;
                case 4:
                    Boolean Flag = false;
                    while (!Flag) {
                        System.out.println("Enter the course id you want to register :");
                        Scanner scanner3 = new Scanner(System.in);
                        String action3 = scanner3.nextLine();
                        stmt1 = "SELECT * FROM COURSES WHERE COURSE_ID=?";
                        stmt = conn.prepareStatement(stmt1);
                        stmt.setString(1, action3);
                        rset = stmt.executeQuery();
                        System.out.println("Your chosen course information is as below:");
                        while (rset.next()) {
                            System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) +
                                    "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                        }
                        System.out.println("Is this the course you want to register? (Yes or No)");
                        Scanner scanner4 = new Scanner(System.in);
                        String action4 = scanner4.nextLine();
                        Boolean Flag2 = false;
                        while (!Flag2) {
                            if (action4.equalsIgnoreCase("Yes")) {
                                Calendar calendar = Calendar.getInstance();
                                java.util.Date currentDate = calendar.getTime();
                                java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
                                stmt1 = "INSERT INTO ENROLLMENT VALUES(userid,action3,sqlDate,NULL)";
                                Flag2 = true;
                            } else if (action4.equalsIgnoreCase("No")) {
                                System.out.println("Please enter the correct course ID:");
                                action3 = scanner3.nextLine();
                                stmt1 = "SELECT * FROM COURSES WHERE COURSE_ID=?";
                                stmt = conn.prepareStatement(stmt1);
                                stmt.setString(1, action3);
                                rset = stmt.executeQuery();
                                System.out.println("Your chosen course information is as below:");
                                while (rset.next()) {
                                    System.out.println("COURSE ID:" + rset.getString(1) + "\nCOURSE TITLE:" + rset.getString(2) +
                                            "\nSTAFF NAME:" + rset.getString(3) + "\nSECTION:" + rset.getString(4) + "\n");
                                }
                                System.out.println("Is this the course you want to register? (Yes or No)");
                                action4 = scanner4.nextLine();
                            }
                        }
                        System.out.println("Do you have other course to register (Yes or No)");
                        action4 = scanner4.nextLine();
                        if (action4.equalsIgnoreCase("No")) {
                            Flag=true;
                        }
                    }
                    return;
                }
            System.out.print("Do you have other things to do? (Yes or No)");
            String Continue=scanner.nextLine();

        }
        else{
            System.out.println("Connection Fail");
        }

        conn.close();
    }*/
    }
}


