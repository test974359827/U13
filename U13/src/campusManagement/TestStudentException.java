package campusManagement;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import exceptions.*;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.*;

public class TestStudentException {
	static CampusManagement management;
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	private static Examination math1;
	private static Examination se;
	private static Examination math2;
	private static Examination fop;
	private static Examination cs;
	
	private static Student johnDoe;
	private static Student peterClark;
	private static Student taylorSmith;
	private static Student annaWilliams;
	private static Student ShynDavfard;
	
	@Before
	public void initTest() throws CampusManagementException {
		management = new CampusManagement("Test", Semester.WiSe_16_17);

		math1 = management.addPastExamination("Mathematics I", 9, Semester.SoSe_17,
				LocalDateTime.parse("05.10.2017 09:00", formatter), LocalDateTime.parse("05.10.2017 11:30", formatter));
		se = management.addPastExamination("Software Engineering", 5, Semester.SoSe_17,
				LocalDateTime.parse("25.09.2017 13:30", formatter), LocalDateTime.parse("25.09.2017 15:00", formatter));
		math2 = management.addPastExamination("Mathematics II", 9, Semester.WiSe_16_17,
				LocalDateTime.parse("03.04.2017 08:00", formatter), LocalDateTime.parse("03.04.2017 09:30", formatter));
		cs = management.addExamination("Computer Security", 5, LocalDateTime.parse("12.02.2018 08:00", formatter),
				LocalDateTime.parse("12.02.2018 09:30", formatter));
		
		johnDoe = management.addStudent("John", "Doe", 11111, "Computer Science B.Sc.");
		peterClark = management.addStudent("Peter", "Clark", 22222, "Computer Science B.Ed.");
		taylorSmith = management.addStudent("Taylor", "Smith", 33333, "Computer Science B.Sc.");

		management.registerStudentForExamination(johnDoe, math1);
		management.registerStudentForExamination(johnDoe, se);
		management.registerStudentForExamination(johnDoe, math2);
		management.addExaminationGradeForStudent(2.0, johnDoe, math1);
		management.addExaminationGradeForStudent(2.0, johnDoe, se);
		management.addExaminationGradeForStudent(2.0, johnDoe, math2);

		management.registerStudentForExamination(peterClark, math1);
		management.registerStudentForExamination(peterClark, se);
		management.registerStudentForExamination(peterClark, math2);
		management.addExaminationGradeForStudent(4.0, peterClark, math1);
		management.addExaminationGradeForStudent(1.7, peterClark, se);
		
	}
	
	
	@Test
	public void Test_CP(){
		
		assertEquals(null , fop);
		try {
			fop = management.addPastExamination("Functional and Object-oriented Programming Concepts ",-2, Semester.SoSe_17,
					LocalDateTime.parse("05.10.2017 09:00", formatter), LocalDateTime.parse("05.10.2017 11:30", formatter));
		} catch (Exception e) {
			assert(e instanceof InvalidValueException);
		}
		assertEquals(null , fop);
	}
	
	@Test
	public void Test_Date(){
		assertEquals(null , fop);

		try {
			fop = management.addPastExamination("Functional and Object-oriented Programming Concepts ",2, Semester.SoSe_17,
					LocalDateTime.parse("05.10.2017 09:00", formatter), LocalDateTime.parse("03.10.2017 11:30", formatter));
		} catch (Exception e) {
			assert(e instanceof InvalidValueException);
		}
		assertEquals(null , fop);

		try {
			fop = management.addPastExamination("Functional and Object-oriented Programming Concepts ",2, Semester.SoSe_17,
					LocalDateTime.parse("05.10.2017 09:00", formatter), LocalDateTime.parse("05.10.2017 08:30", formatter));
		} catch (Exception e) {
			assert(e instanceof InvalidValueException);
		}
		assertEquals(null , fop);

	}
	
	@Test
	public void Test_SameExam(){
		try {
			math1 = management.addPastExamination("Mathematics I", 9, Semester.SoSe_17,
					LocalDateTime.parse("02.10.2017 09:00", formatter), LocalDateTime.parse("02.10.2017 11:30", formatter));
		} catch (Exception e) {
			assert(e instanceof ExaminationAlreadyExistsException);
		}
	}
	
	@Test
	public void Test_ExmNotReg(){
		try {
			management.addExaminationGradeForStudent(2.0, johnDoe, fop);
		} catch (Exception e) {
			assert(e instanceof StudentRegistrationException);
		}
		
		
	}
	
	@Test
	public void Test_hasPoint(){
		try {
			management.addExaminationGradeForStudent(3.0, johnDoe, math2);
		} catch (Exception e) {
			assert(e instanceof GradeAlreadyExistsException);
		}
	}
	
	@Test
	public void Test_PScale(){
		try {
			management.addExaminationGradeForStudent(4.1, peterClark, math2);
		} catch (Exception e) {
			assert(e instanceof InvalidValueException);
		}
	}
	
	@Test
	public void Test_StdID(){
		try {
			taylorSmith = management.addStudent("Taylor", "Smith", 11111, "Computer Science B.Sc.");
		} catch (Exception e) {
			assert(e instanceof InvalidValueException);
		}
	}
	
	@Test
	public void Test_ExmReg(){
		try {
			management.registerStudentForExamination(peterClark, math1);
		} catch (Exception e) {
			assert(e instanceof StudentRegistrationException);
		}
	}
	@Test
	public void Test_addPastExamination() throws CampusManagementException{
		
		fop = management.addPastExamination("Functional and Object-oriented Programming Concepts ",2, Semester.SoSe_17,
				LocalDateTime.parse("05.10.2017 09:00", formatter), LocalDateTime.parse("05.10.2017 11:30", formatter));
		
		assertEquals(fop, management.getExaminations().get(management.getExaminations().size()-1));

	}
	
	@Test
	public void Test_addExaminationGradeForStudent() throws CampusManagementException{
		management.registerStudentForExamination(taylorSmith, math2);
		management.registerStudentForExamination(taylorSmith, math1);
		management.registerStudentForExamination(taylorSmith, cs);

		management.addExaminationGradeForStudent(3.0, taylorSmith, math2);
		management.addExaminationGradeForStudent(2.0, taylorSmith, cs);
		management.addExaminationGradeForStudent(3.3, taylorSmith, math1);
		
		
		assertEquals(3, taylorSmith.getGrades().size());
		assertEquals(new Double(3.0), new Double(taylorSmith.getGrades().get(0).getGrade()));
		assertEquals(new Double(2.0), new Double(taylorSmith.getGrades().get(1).getGrade()));
		assertEquals(new Double(3.3), new Double(taylorSmith.getGrades().get(2).getGrade()));

	}
	
	@Test
	public void Test_addStudent() throws InvalidValueException{
		annaWilliams = management.addStudent("Anna", "Williams", 44444, "Computer Science B.Sc.");

		assertEquals("Anna", management.getStudents().get(management.getStudents().size()-1).getFirstName());
	}
	
	@Test
	public void Test_registerStudentForExamination() throws CampusManagementException{
		ShynDavfard = management.addStudent("Shayan", "Davari Fard", 15129, "Computer Science B.Sc.");

		management.registerStudentForExamination(ShynDavfard, math1);
		management.registerStudentForExamination(ShynDavfard, cs);
		management.registerStudentForExamination(ShynDavfard, math2);
		
		management.addExaminationGradeForStudent(3.0, ShynDavfard, math2);
		management.addExaminationGradeForStudent(2.0, ShynDavfard, cs);
		management.addExaminationGradeForStudent(3.3, ShynDavfard, math1);
		
		assertEquals(3, ShynDavfard.getGrades().size());
		assertEquals(new Double(3.0), new Double(ShynDavfard.getGrades().get(0).getGrade()));
		assertEquals(new Double(2.0), new Double(ShynDavfard.getGrades().get(1).getGrade()));
		assertEquals(new Double(3.3), new Double(ShynDavfard.getGrades().get(2).getGrade()));
	}

}
