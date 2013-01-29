package acad.db.courserank.misc;

/**
 *
 * @author rishabh
 */

import acad.db.courserank.dbis.global;
import acad.db.courserank.dbis.profiles.profiles;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Vitz
 */
public class DataGenerator {
    
    public static void main (String[] args) {
        
        boolean PRINT_STUDENTS = false;
        boolean PRINT_INSTRUCTORS = false;
        boolean PRINT_COURSES = false;
        
        
        int currentYear = 2011;
        
        try {
            // Generate List of Departments
            ArrayList<String> depts = new ArrayList<String>();
            depts.add("Computer Science");
            depts.add("Electrical");
            depts.add("Chemical");
            depts.add("Civil");
            depts.add("Mechanical");
            depts.add("Metallurgical");
            depts.add("Aerospace");
            depts.add("Humanities");
            depts.add("Biology");
            depts.add("Mathematics");
            depts.add("Physics");
            
            // Generate List of Semesters
            ArrayList<String> sem = new ArrayList<String>();
            sem.add("Autumn");
            sem.add("Spring");
            sem.add("Summer");
            sem.add("Winter");
            
            // Generate Venues
            ArrayList<String> venues = new ArrayList<String>();
            venues.add("FC Kohli");
            venues.add("IRCC");
            venues.add("Convocation Hall");
            venues.add("Lecture Hall Complex");
            venues.add("Lecture Theater");
            venues.add("VMCC");
            
            // Generate students
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("FName.dat"))));
            ArrayList<String> fnames = new ArrayList<String>();
            String str = in.readLine();
            while (str != null && !"".equals(str)) {
                fnames.add(str);
                str = in.readLine();
            }
            in.close();
            
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("LName.dat"))));
            ArrayList<String> lnames = new ArrayList<String>();
            str = in.readLine();
            while (str != null && !"".equals(str)) {
                lnames.add(str);
                str = in.readLine();
            }
            in.close();
            
            // List of Names Created
            
            // Generate student ids
            ArrayList<String> ids = new ArrayList<String>();
            String id = "";
            Random r = new Random();
            for (int i=0; i<1000; i++) {
                id += r.nextInt(10);
                id += r.nextInt(10);
                if (r.nextBoolean()) id += r.nextInt(10);
                id += (r.nextBoolean() ? "0" : (r.nextBoolean() ? "D" : "X"));
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                if (!ids.contains(id)) ids.add(id);
                id = "";
            }
            
            //Create Students
            class Student {
                String id;
                String name;
                String password;
                String department;
                int cpi2;
                
                String print() {
                    String result = id + "\t" + name + "\t" + department + "\t" + (cpi2/100.0);
                    return result;
                }
            }
            
            ArrayList<Student> students = new ArrayList<Student>();
            r = new Random();
            Student s;
            for (int i=0; i<1000; i++) {
                s = new Student();
                s.id = ids.get(i);
                s.name = fnames.get(r.nextInt(fnames.size())) + " " + lnames.get(r.nextInt(lnames.size()));
                s.password = s.id;
                s.department = depts.get(r.nextInt(depts.size()));
                s.cpi2 = 500 + r.nextInt(500);
                students.add(s);
            }
            
            // Students Generated
            if (PRINT_STUDENTS) {
                for (Student stud: students) {
                    System.out.println(stud.print());
                }
            }
            
            System.out.println("Generation: Students Generated");
            
            // Generate Instructors
            class Instructor {
                String id;
                String name;
                String department;
                int rating2;
                
                String print() {
                    return id + "\t" + name + "\t" + department + "\t" + (rating2/100.0);
                }
            }
            
            ids = new ArrayList<String>();
            id = "";
            r = new Random();
            for (int i=0; i<100; i++) {
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                id += r.nextInt(10);
                if (!ids.contains(id)) ids.add(id);
                id = "";
            }
            
            ArrayList<Instructor> instructors = new ArrayList<Instructor>();
            r = new Random();
            Instructor instr;
            for (int i=0; i<100; i++) {
                instr = new Instructor();
                instr.id = ids.get(i);
                instr.name = fnames.get(r.nextInt(fnames.size())) + " " + lnames.get(r.nextInt(lnames.size()));
                instr.department = depts.get(r.nextInt(depts.size()));
                instr.rating2 = 0;
                instructors.add(instr);
            }
            
            //Instructors Generated
            if (PRINT_INSTRUCTORS) {
                for (Instructor i: instructors) {
                    System.out.println(i.print());
                }
            }
            
            System.out.println("Generation: Instructors Generated");
            
            // Generate Courses (read from external file)
            class Course {
                String id;
                String name;
                String department;
                String description;
                
                String print() {
                    return id + "\t" + name + "\t" + department;// + "\t" + description;
                }
            }
            
            ArrayList<Course> courses = new ArrayList<Course>();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Courses.dat"))));
            Course crs;
            str = in.readLine();
            while (str != null && !"".equals(str)) {
                crs = new Course();
                crs.id = str;
                crs.name = in.readLine();
                crs.department = in.readLine();
                crs.description = in.readLine();
                courses.add(crs);
                str = in.readLine();
            }
            in.close();
            
            // Courses Generated
            if (PRINT_COURSES) {
                for (Course c: courses) {
                    System.out.println(c.print());
                }
            }
            
            System.out.println("Generation: Courses Generated");
            
            //Timeslot Table Data
            class Timeslot {
                int id;
                int day;
                int start_hr;
                int start_min;
                int end_hr;
                int end_min;
            }
            
            ArrayList<Timeslot> slots = new ArrayList<Timeslot>();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Timeslot.dat"))));
            Timeslot ts;
            str = in.readLine();
            String[] strSplit;
            while (str != null && !"".equals(str)) {
                ts = new Timeslot();
                strSplit = str.split(" ");
                ts.id = Integer.parseInt(strSplit[0]);
                ts.day = Integer.parseInt(strSplit[1]);
                ts.start_hr = Integer.parseInt((strSplit[2].split(":")[0]));
                ts.start_min = Integer.parseInt((strSplit[2].split(":")[1]));
                ts.end_hr = Integer.parseInt((strSplit[3].split(":")[0]));
                ts.end_min = Integer.parseInt((strSplit[3].split(":")[1]));
                slots.add(ts);
                str = in.readLine();
            }
            in.close();
            
            // Timeslots read
            
            System.out.println("Generation: Timeslots Generated");
            
            //Projects Table Data
            class Project {
                int id;
                String name;
                String description;
            }
            
            ArrayList<Project> projects = new ArrayList<Project>();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Projects.dat"))));
            Project p;
            str = in.readLine();
            while (str != null && !"".equals(str)) {
                p = new Project();
                p.id = Integer.parseInt(str);
                str = in.readLine();
                p.name = str;
                str = in.readLine();
                p.description = str;
                projects.add(p);
                str = in.readLine();
            }
            in.close();
            
            // Projects read
            
            System.out.println("Generation: Projects Generated");
            
            //Work Profile Table Data
            class Work {
                int id;
                String name;
                String description;
                String period;
                int length;
                int stipend;
                String location;
                String category;
                ArrayList<String> depts;
            }
            
            ArrayList<Work> works = new ArrayList<Work>();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("Work.dat"))));
            Work w;
            str = in.readLine();
            while (str != null && !"".equals(str)) {
                w = new Work();
                w.id = Integer.parseInt(str);
                str = in.readLine();
                w.name = str;
                str = in.readLine();
                w.description = str;
                str = in.readLine();
                w.period = str;
                str = in.readLine();
                w.length = Integer.parseInt(str);
                str = in.readLine();
                w.stipend = Integer.parseInt(str);
                str = in.readLine();
                w.location = str;
                str = in.readLine();
                w.category = str;
                str = in.readLine();
                w.depts = new ArrayList<String>();
                w.depts.addAll(Arrays.asList(str.split(",")));
                works.add(w);
                str = in.readLine();
            }
            in.close();
            
            // Work Profiles read
            
            System.out.println("Generation: Work Profiles Generated");
            
            // Now adding to database
            Connection conn = global.getConnection();

            
            // Insert Students
            PreparedStatement pStmt = conn.prepareStatement("insert into student values (?, ?, ?, ?, ?, MD5(?), 0, 0, 0, 0, 0, 0)");
            for (int i=0; i<students.size(); i++) {
                Student stud = students.get(i);
                pStmt.setString(1, stud.id);
                pStmt.setString(2, stud.name);
                pStmt.setString(3, stud.department);
                
                BufferedImage im = ImageIO.read(new File("defaultImage.jpg"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(im, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                pStmt.setBlob(4, bais);
                
                pStmt.setDouble(5, stud.cpi2/100.0);
                pStmt.setString(6, stud.password);
                
                pStmt.executeUpdate();
            }
            pStmt.close();
            
            System.out.println("Insertion: student finished");
            
            // Insert Instructors
            pStmt = conn.prepareStatement("insert into instructor values (?, ?, ?, ?)");
            for (int i=0; i<instructors.size(); i++) {
                Instructor instructor = instructors.get(i);
                pStmt.setString(1, instructor.id);
                pStmt.setString(2, instructor.name);
                pStmt.setString(3, instructor.department);
                pStmt.setNull(4, java.sql.Types.DECIMAL);
                
                pStmt.executeUpdate();
            }
            pStmt.close();
            
            System.out.println("Insertion: instructor finished");
            
            // Insert Courses
            pStmt = conn.prepareStatement("insert into course values (?, ?, ?, ?)");
            for (int i=0; i<courses.size(); i++) {
                Course course = courses.get(i);
                pStmt.setString(1, course.id);
                pStmt.setString(2, course.name);
                pStmt.setString(3, course.department);
                pStmt.setString(4, course.description);
                
                pStmt.executeUpdate();
            }
            pStmt.close();
            
            System.out.println("Insertion: course finished");
            
            // Insert Timeslots
            pStmt = conn.prepareStatement("insert into timeslot values(?, ?, ?, ?)");
            for (Timeslot t: slots) {
                pStmt.setInt(1, t.id);
                pStmt.setInt(2, t.day);
                pStmt.setString(3, t.start_hr + ":" + t.start_min + ":00");
                pStmt.setString(4, t.end_hr + ":" + t.end_min + ":00");
                pStmt.executeUpdate();
            }
            pStmt.close();
            
            System.out.println("Insertion: timeslot finished");
            
            // Insert Offerings
            pStmt = conn.prepareStatement("insert into offering values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int numOfferings = 0;
            for (int i=0; i<courses.size(); i++) {
                r = new Random();
                Course course = courses.get(i);
                int jmax = r.nextInt(4);
                numOfferings += jmax;       // Maintain the total number of offerings
                for (int j=0; j<jmax; j++) {
                    pStmt.setNull(1, java.sql.Types.INTEGER);
                    pStmt.setString(2, course.id);
                    int year = 2008 + r.nextInt(5);
                    pStmt.setInt(3, year);
                    pStmt.setString(4, (r.nextInt(10) < 8 ? sem.get(r.nextInt(2)) : sem.get(r.nextInt(2) + 2)));
                    int credits = 2 + r.nextInt(6);
                    pStmt.setInt(5, credits);
                    pStmt.setNull(6, java.sql.Types.DECIMAL);
                    pStmt.setInt(7, credits);
                    pStmt.setString(8, venues.get(r.nextInt(venues.size())));
                    pStmt.setInt(9, slots.get(r.nextInt(slots.size())).id);
                    
                    // Grades
                    if (year <= currentYear) {
                        int grades[] = new int[10];
                        grades[0] = (r.nextInt(10) < 9 ? 0 : 1);
                        grades[1] = 1 + r.nextInt(10);
                        grades[2] = 3 + r.nextInt(20);
                        grades[3] = 5 + r.nextInt(20);
                        grades[4] = 4 + r.nextInt(10);
                        grades[5] = 2 + r.nextInt(10);
                        grades[6] = r.nextInt(10);
                        grades[7] = r.nextInt(10);
                        grades[8] = (r.nextInt(10) < 8 ? r.nextInt(3) : 0);
                        grades[9] = (r.nextInt(10) < 8 ? r.nextInt(3) : 0);

                        int strength = 0;
                        for (int k=0; k<10; k++) {
                            strength += grades[k];
                            pStmt.setInt(11+k, grades[k]);
                        }
                        pStmt.setInt(10, strength);
                    }
                    else {
                        for (int k=0; k<11; k++) {
                            pStmt.setNull(10+k, java.sql.Types.INTEGER);
                        }
                    }
                    pStmt.executeUpdate();
                }
            }
            pStmt.close();
            
            System.out.println("Insertion: offering finished");
            
            // Insert Prerequisites
            pStmt = conn.prepareStatement("insert into prerequisite values(?, ?)");
            // Iterate upto courses.size()-1 because last course cannot be have any prerequisite
            for (int i=0; i<courses.size()-1; i++) {
                Course head = courses.get(i);
                Course tail = courses.get(i+1 + r.nextInt(courses.size()-(i+1)));
                for (int k=0; k<3 && !tail.department.equals(head.department); k++) {
                    tail = courses.get(i+1 + r.nextInt(courses.size()-(i+1)));
                }
                if (tail.department.equals(head.department)) {
                    pStmt.setString(1, head.id);
                    pStmt.setString(2, tail.id);
                    pStmt.executeUpdate();
                }
            }
            pStmt.close();
            
            System.out.println("Insertion: prerequisite finished");
            
            // Insert into takes
            pStmt = conn.prepareStatement("insert into takes values(?, ?, ?)");
            PreparedStatement pStmt2 = conn.prepareStatement("select offering_id, strength, year from offering");
            ResultSet rs2 = pStmt2.executeQuery();
            while(rs2.next()) {
                HashSet<Student> studs = new HashSet<Student>();
                for (int i=0; i<rs2.getInt(2); i++) {
                    if (r.nextBoolean()) studs.add(students.get(r.nextInt(students.size())));
                }
                for (Student stud: studs) {
                    pStmt.setString(1, stud.id);
                    pStmt.setInt(2, rs2.getInt(1));
                    String grade;
                    if (r.nextBoolean()) {
                        if (r.nextBoolean()) {
                            if (r.nextBoolean()) {
                                if (r.nextBoolean() && r.nextBoolean()) {
                                    grade = "AP";
                                }
                                else grade = "AA";
                            }
                            else {
                                grade = "AB";
                            }
                        }
                        else {
                            if (r.nextBoolean()) {
                                grade = "BB";
                            }
                            else {
                                grade = "BC";
                            }
                        }
                    }
                    else {
                        if (r.nextBoolean()) {
                            if (r.nextBoolean()) {
                                grade = "CC";
                            }
                            else {
                                grade = "CD";
                            }
                        }
                        else {
                            if (r.nextBoolean()) {
                                if (r.nextBoolean()) grade = "DD";
                                else grade = "FR";
                            }
                            else {
                                grade = "XX";
                            }
                        }
                    }
                    if (rs2.getInt(3) > currentYear) pStmt.setNull(3, java.sql.Types.VARCHAR);
                    else pStmt.setString(3, grade);
                    pStmt.executeUpdate();
                }
            }
            
            System.out.println("Insertion: takes finished");
            
            // Insert into teaches
            pStmt = conn.prepareStatement("insert into teaches values(?, ?, ?, ?)");
            pStmt2 = conn.prepareStatement("select offering_id, aww from offering");
            rs2 = pStmt2.executeQuery();
            while(rs2.next()) {
                HashSet<Instructor> instrs = new HashSet<Instructor>();
                instrs.add(instructors.get(r.nextInt(instructors.size())));
                for (int i=0; i<2; i++) {
                    if (r.nextBoolean()) instrs.add(instructors.get(r.nextInt(instructors.size())));
                    if (r.nextBoolean() && r.nextBoolean()) instrs.add(instructors.get(r.nextInt(instructors.size())));
                }
                for (Instructor instructor: instrs) {
                    pStmt.setString(1, instructor.id);
                    pStmt.setInt(2, rs2.getInt(1));
                    pStmt.setNull(3, java.sql.Types.DECIMAL);
                    pStmt.setDouble(4, rs2.getDouble(2));
                    pStmt.executeUpdate();
                }
            }
            
            System.out.println("Insertion: teaches finished");
            
            // Insert Projects
            pStmt = conn.prepareStatement("insert into project values(?, ?, ?, ?, ?)");
            
            PreparedStatement pStmt3 = conn.prepareStatement("select count(distinct offering_id) from offering");
            ResultSet rs3 = pStmt3.executeQuery();
            rs3.next();
            numOfferings = rs3.getInt(1);
            pStmt3.close();
            
            for (Project proj: projects) {
                pStmt.setInt(1, proj.id);
                pStmt.setString(2, proj.name);
                pStmt.setString(3, proj.description);
                
                pStmt2 = conn.prepareStatement("select offering_id, year from offering limit ?,1");
                
                int tupleNumber = r.nextInt(numOfferings);
                pStmt2.setInt(1, tupleNumber);
                rs2 = pStmt2.executeQuery();
                rs2.next();
                while(rs2.getInt(2) > currentYear) {
                    tupleNumber = r.nextInt(numOfferings);
                    pStmt2.setInt(1, tupleNumber);
                    rs2 = pStmt2.executeQuery();
                    rs2.next();
                }
                int o_id = rs2.getInt(1);
                pStmt2.close();
                
                pStmt2 = conn.prepareStatement("select instructor_id from teaches where offering_id = ?");
                pStmt2.setInt(1, o_id);
                rs2 = pStmt2.executeQuery();
                rs2.next();
                
                pStmt.setInt(4, o_id);
                pStmt.setString(5, rs2.getString(1));
                pStmt.executeUpdate();
                
                pStmt2.close();
            }
            pStmt.close();
            
            System.out.println("Insertion: project finished");
            
            // Insert into takes_project
            pStmt = conn.prepareStatement("insert into takes_project values (?, ?, ?)");
            pStmt2 = conn.prepareStatement("select project_id, offering_id from project");
            pStmt3 = conn.prepareStatement("select student_id from takes where offering_id = ?");
            rs3 = null;
            rs2 = pStmt2.executeQuery();
            while (rs2.next()) {
                pStmt3.setInt(1, rs2.getInt(2));
                rs3 = pStmt3.executeQuery();
                while(rs3.next()) {
                    if (r.nextInt(9) > 6) {
                        pStmt.setString(1, rs3.getString(1));
                        pStmt.setInt(2, rs2.getInt(1));
                        pStmt.setDouble(3, (201 + r.nextInt(800))/10.0);
                        pStmt.executeUpdate();
                    }
                }
            }
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            
            System.out.println("Insertion: takes_project finished");
            
            // Insert into work_profile
            pStmt = conn.prepareStatement("insert into work_profile values(?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (Work wp: works) {
                pStmt.setInt(1, wp.id);
                pStmt.setString(2, wp.name);
                pStmt.setString(3, wp.description);
                pStmt.setString(4, wp.period);
                pStmt.setInt(5, wp.length);
                pStmt.setInt(6, wp.stipend);
                pStmt.setString(7, wp.location);
                pStmt.setString(8, wp.category);
                pStmt.executeUpdate();
            }
            pStmt.close();
            
            System.out.println("Insertion: work_profile finished");
            
            // Insert into work_department
            pStmt = conn.prepareStatement("insert into work_department values(?, ?)");
            
            for (Work wp: works) {
                for (int i=0; i<wp.depts.size(); i++) {
                    pStmt.setInt(1, wp.id);
                    pStmt.setString(2, wp.depts.get(i));
                    pStmt.executeUpdate();
                }
            }
            pStmt.close();
            
            System.out.println("Insertion: work_department finished");
            
            // Insert into takes_work
            pStmt = conn.prepareStatement("insert into takes_work values (?, ?)");
            for (Work wp: works) {
                int numStudents = r.nextInt(4);
                HashSet<Student> studs = new HashSet<Student>();
                for (int i=0; i<numStudents; i++) {
                    Student stud = students.get(r.nextInt(students.size()));
                    while (!wp.depts.contains(stud.department)) {
                        stud = students.get(r.nextInt(students.size()));
                    }
                    studs.add(stud);
                }
                for (Student stud: studs) {
                    pStmt.setString(1, stud.id);
                    pStmt.setInt(2, wp.id);
                    pStmt.executeUpdate();
                }
            }
            pStmt.close();
            
            System.out.println("Insertion: takes_work finished");
            
            // Insert into classrep
            pStmt = conn.prepareStatement("insert into classrep values (?, ?)");
            pStmt2 = conn.prepareStatement("select offering_id from offering");
            PreparedStatement pStmt4 = conn.prepareStatement("select count(student_id) from takes where offering_id = ?");
            ResultSet rs4 = null;
            pStmt3 = conn.prepareStatement("select * from (select student_id from takes where offering_id = ?) as T limit ?,1");
            rs2 = pStmt2.executeQuery();
            while(rs2.next()) {
                if (r.nextBoolean()) {
                    continue;
                }
                else {
                    pStmt4.setInt(1, rs2.getInt(1));
                    rs4 = pStmt4.executeQuery();
                    rs4.next();
                    int numStudents = rs4.getInt(1);
                    if (numStudents == 0) continue;
                    pStmt3.setInt(1, rs2.getInt(1));
                    pStmt3.setInt(2, 1 + r.nextInt(numStudents));
                    rs3 = pStmt3.executeQuery();
                    if (!rs3.next()) continue;
                    pStmt.setString(1, rs3.getString(1));
                    pStmt.setInt(2, rs2.getInt(1));
                    pStmt.executeUpdate();
                }
            }
            pStmt.close();
            pStmt2.close();
            pStmt3.close();
            pStmt4.close();
            
            System.out.println("Insertion: classrep finished");
            
            // Insert Feedback
            
            pStmt2 = conn.prepareStatement("select offering_id, aww from offering");
            pStmt3 = conn.prepareStatement("select instructor_id from teaches where offering_id = ?");
            pStmt4 = conn.prepareStatement("select student_id from takes where offering_id = ?");
            
            rs2 = pStmt2.executeQuery();
            while (rs2.next()) {
                pStmt3.setInt(1, rs2.getInt(1));
                rs3 = pStmt3.executeQuery();
                while (rs3.next()) {
                    pStmt4.setInt(1, rs2.getInt(1));
                    rs4 = pStmt4.executeQuery();
                    while (rs4.next()) {
                        if (r.nextInt(7) > 5) {
                            // Add a feedback entry for this student, instructor, offering
                            profiles.submitFeedback(rs4.getString(1), rs3.getString(1), rs2.getInt(1), 1 + r.nextInt(5), 1 + r.nextInt(5), 1 + r.nextInt(5), (float)(rs2.getInt(2) + r.nextInt(9)/2.0 - 1));
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
            pStmt2.close();
            pStmt3.close();
            pStmt4.close();
            
            System.out.println("Insertion: feedback finished");
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("File Not Found Exception: " + fnfe.toString());
            System.err.println("Current Dir: " + System.getProperty("user.dir"));
        }
        catch (IOException ioe) {
            System.err.println("IO Exception: " + ioe.toString());
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Class Not Found Exception: " + cnfe.toString());
        }
        catch (SQLException sqle) {
            System.err.println("SQL Exception: " + sqle.toString());
        }
    }
}