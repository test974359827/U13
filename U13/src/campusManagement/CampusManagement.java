package campusManagement;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import exceptions.*;

/**
 * Class represents a campus management system
 * 
 * @author Lukas Roehrig
 */
public class CampusManagement {

	/**
	 * name of the system
	 */
	private String name;

	/**
	 * current semester
	 */
	private Semester currentSemester;

	/**
	 * students registered in the system
	 */
	private LinkedList<Student> students;

	/**
	 * examinations registered in the system
	 */
	private LinkedList<Examination> examinations;

	/**
	 * Constructs a new management system given the parameters
	 * 
	 * @param name
	 * @param currentSemester
	 */
	public CampusManagement(String name, Semester currentSemester) {
		this.name = name;
		this.currentSemester = currentSemester;
		students = new LinkedList<Student>();
		examinations = new LinkedList<Examination>();
	}

	/**
	 * @return the name of the system
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the current semester
	 */
	public Semester getCurrentSemester() {
		return currentSemester;
	}

	/**
	 * @return the list of all students registered
	 */
	public LinkedList<Student> getStudents() {
		return students;
	}

	/**
	 * @return the list of all examinations registered
	 */
	public LinkedList<Examination> getExaminations() {
		return examinations;
	}


	/**
	 * Adds a new student to the list of students with the parameters given
	 * 
	 * @param firstName
	 * @param lastName
	 * @param matriculationNumber
	 * @param courseOfStudies
	 * @return
	 * @throws InvalidValueException 
	 */
	public Student addStudent(String firstName, String lastName, int matriculationNumber, String courseOfStudies) throws InvalidValueException {
		
		if(!(getFilteredStudents(filterStudentsByMatriculationNumber(matriculationNumber)).isEmpty()))
			throw new InvalidValueException("MSG");//TODO
		
		Student newStudent = new Student(firstName, lastName, matriculationNumber, courseOfStudies, currentSemester);
		students.add(newStudent);
		return newStudent;
	}

	/**
	 * Adds a new examination to the list of examinations which takes places in
	 * the current semester
	 * 
	 * @param name
	 * @param creditPoints
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 * @throws CampusManagementException 
	 */
	public Examination addExamination(String name, int creditPoints, LocalDateTime dateBegin, LocalDateTime dateEnd) throws CampusManagementException {
		return addPastExamination(name, creditPoints, currentSemester, dateBegin, dateEnd);
	}

	/**
	 * Adds a past examination to the list of examinations which took place in
	 * another semester
	 * 
	 * @param name
	 * @param creditPoints
	 * @param semester
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 * @throws CampusManagementException 
	 */
	public Examination addPastExamination(String name, int creditPoints, Semester semester, LocalDateTime dateBegin,
			LocalDateTime dateEnd) throws CampusManagementException {
		// REGEL 1
		if(creditPoints < 1 )
			throw new InvalidValueException("MSG");//TODO
		// REGEL 2
		if(dateBegin.getDayOfYear() != dateEnd.getDayOfYear() ||
				dateBegin.getHour() >= dateEnd.getHour())
			throw new InvalidValueException("MSG");//TODO

		// REGEL 3
		if(!(getFilteredExaminations(filterExaminationsByName(name)).isEmpty()))
			if(getFilteredExaminations(filterExaminationsByName(name)).get(0).getSemester().equals(semester))
				throw new ExaminationAlreadyExistsException("MSG");//TODO
				
		Examination newExamination = new Examination(name, creditPoints, semester, dateBegin, dateEnd);
		examinations.add(newExamination);
		return newExamination;
	}
	

	/**
	 * Registers a given student to a given examination
	 * 
	 * @param student
	 * @param examination
	 * @throws StudentRegistrationException 
	 */
	public void registerStudentForExamination(Student student, Examination examination) throws StudentRegistrationException {
		
		if(getFilteredExaminations(filterExaminationsByName(examination.getName())).get(0)
				.getStudentsRegistered().stream()
				.filter(filterStudentsByMatriculationNumber
						(student.getMatriculationNumber())).count() > 0)
			throw new StudentRegistrationException("MSG"); //TODO
		
		student.getExaminationsRegistered().add(examination);
		examination.getStudentsRegistered().add(student);
	}

	/**
	 * Adds a new grade achieved by a student given the student and the
	 * examination
	 * 
	 * @param grade
	 * @param student
	 * @param examination
	 * @throws CampusManagementException 
	 */
	public void addExaminationGradeForStudent(double grade, Student student,
			Examination examination) throws CampusManagementException {
		
		boolean temp = true;
		for(Examination exm : student.getExaminationsRegistered())
			if(exm.equals(examination))
				temp = false;
		if(temp)
			throw new StudentRegistrationException("MSG");
		
		
		
		if(student.getGrades().stream().filter(grd -> grd.getExamination().equals(examination)).count() != 0 )
			throw new GradeAlreadyExistsException("MSG");//TODO
		
		if(!(grade == 1.0 || grade == 1.3 || grade == 1.7 || grade == 2.0 ||
				grade == 2.3 || grade == 2.7 || grade == 3.0 || grade == 3.3 
				|| grade == 3.7 || grade == 4.0 || grade == 5.0 ))
			throw new InvalidValueException("MSG");//TODO
		
		ExaminationGrade examinationGrade = new ExaminationGrade(grade, student, examination);
		student.getGrades().add(examinationGrade);
		examination.getGrades().add(examinationGrade);
	}
	
	/**
	 * liefert ein Filter-
	 * Pr�dikat zur�ck, das alle Studenten mit dem �bergebenen Vor- und Nachname selektiert.
	 * Gro�- und Kleinschreibung soll dabei ignoriert werden
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	public Predicate<Student> filterStudentsByName(String firstName, String lastName){
		return (std -> std.getFirstName().toLowerCase().equals(firstName.toLowerCase()) && 
				std.getLastName().toLowerCase().equals(lastName.toLowerCase()));
	}
	
	/**
	 * liefert ein Filter-
	 * Pr�dikat zur�ck, das alle Studenten mit der �bergebenen Matrikelnummer selektiert
	 * 
	 * @param matriculationNumber
	 * @return
	 */
	public Predicate<Student> filterStudentsByMatriculationNumber(int matriculationNumber){
		return (std -> std.getMatriculationNumber() == matriculationNumber);
	}
	
	/**
	 * 	liefert ein Filter-Pr�dikat zur�ck, das alle Studenten mit dem �bergebenen 
	 * Studiengang selektiert. Gro�- undKleinschreibung soll dabei ignoriert werden.
	 * 
	 * @param courseOfStudies
	 * @return
	 */
	public Predicate<Student> filterStudentsByCourseOfStudies(String courseOfStudies){
		return (std -> std.getCourseOfStudies().toLowerCase().equals(courseOfStudies.toLowerCase()));
	}
	
	/**
	 * bekommt ein Filter-Pr�dikat �bergeben und liefert alle im System
	 * registrierten Studenten in einer java.util.List zur�ck, die der 
	 * Filter nicht aussortiert hat.
	 * 
	 * @param filter
	 * @return
	 */
	public List<Student> getFilteredStudents(Predicate<Student> filter){ 
		return students.stream().filter(filter).collect(Collectors.toList()); // AGHA alan bas negatiov ina biad ya hamina ? 
	}
	
	/**
	 * liefert ein Filter-Pr�dikat zur�ck,das alle Pr�fungen mit dem 
	 * �bergebenen Name selektiert. Gro�- und Kleinschreibungsoll 
	 * dabei ignoriert werden.
	 * 
	 * @param name
	 * @return
	 */
	public Predicate<Examination> filterExaminationsByName(String name){
		return (exm -> exm.getName().toLowerCase().equals(name.toLowerCase()));
	}
	
	/**
	 * liefert ein Filter-Pr�dikat zur�ck das alle Pr�fungen
	 *  mit der �bergebenen Anzahl an Credit Points selektiert.
	 * 
	 * @param cp
	 * @return
	 */
	public Predicate<Examination> filterExaminationsByCreditPoints(int cp){
		return (exm -> exm.getCreditPoints() == cp);
	}
	
	/**
	 * liefert ein Filter-Pr�dikat zur�ck, das alle Pr�fungen die 
	 * in dem �bergebenen Semester stattfinden selektiert.
	 * 
	 * @param semester
	 * @return
	 */
	public Predicate<Examination> filterExaminationsBySemester(Semester semester){
		return (exm -> exm.getSemester().equals(semester));
	}
	
	/**
	 * bekommt ein Filter-Pr�dikat �bergeben und liefert alle im System 
	 * registrierten Pr�fungen in einer java.util.Listzur�ck, die der Filter 
	 * nicht aussortiert hat.
	 * 
	 * @param filter
	 * @return
	 */
	public List<Examination> getFilteredExaminations(Predicate<Examination> filter){
		return examinations.stream().filter(filter).collect(Collectors.toList());
	}
}
