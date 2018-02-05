package campusManagement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/**
 * Class represents a examination in our management system
 * 
 * @author Lukas Roehrig
 */
public class Examination {

	/**
	 * name of the examinations
	 */
	private String name;

	/**
	 * credit points achieved if this examination is passed
	 */
	private int creditPoints;

	/**
	 * semester this examination takes place
	 */
	private Semester semester;

	/**
	 * date and start time of the examination
	 */
	LocalDateTime dateBegin;

	/**
	 * date and end time of the examination
	 */
	LocalDateTime dateEnd;

	/**
	 * list of students who registered themselves for this examination
	 */
	private LinkedList<Student> studentsRegistered;

	/**
	 * list of grades which where achieved in this examination
	 */
	private LinkedList<ExaminationGrade> grades;

	/**
	 * Constructs a new Examination with the parameters given
	 * 
	 * @param name
	 * @param creditPoints
	 * @param semester
	 * @param dateBegin
	 * @param dateEnd
	 */
	public Examination(String name, int creditPoints, Semester semester, LocalDateTime dateBegin,
			LocalDateTime dateEnd) {
		this.name = name;
		this.creditPoints = creditPoints;
		this.semester = semester;
		setDateBegin(dateBegin);
		setDateEnd(dateEnd);
		studentsRegistered = new LinkedList<Student>();
		grades = new LinkedList<ExaminationGrade>();
	}

	/**
	 * @return the name of the examination
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return credit points achieved if this examination is passed
	 */
	public int getCreditPoints() {
		return creditPoints;
	}

	/**
	 * @return date and start time of the examination
	 */
	public LocalDateTime getDateBegin() {
		return dateBegin;
	}

	/**
	 * @return date and end time of the examination
	 */
	public LocalDateTime getDateEnd() {
		return dateEnd;
	}

	/**
	 * @return list of students who registered themselves for this examination
	 */
	public LinkedList<Student> getStudentsRegistered() {
		return studentsRegistered;
	}

	/**
	 * @return list of grades which where achieved in this examination
	 */
	public LinkedList<ExaminationGrade> getGrades() {
		return grades;
	}

	/**
	 * @return semester this examination takes place
	 */
	public Semester getSemester() {
		return semester;
	}

	/**
	 * overrides the date and start time of this examination with the date and
	 * time given
	 * 
	 * @param dateBegin
	 */
	public void setDateBegin(LocalDateTime dateBegin) {
		this.dateBegin = dateBegin;
	}

	/**
	 * overrides the date and end time of this examination with the date and
	 * time given
	 * 
	 * @param dateEnd
	 */
	public void setDateEnd(LocalDateTime dateEnd) {
		this.dateEnd = dateEnd;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Examination: ");
		sb.append(getName());
		sb.append(" - CP: ");
		sb.append(getCreditPoints());
		sb.append(" - ");
		sb.append("Students registered: ");
		sb.append(getStudentsRegistered().size());
		sb.append(" - ");
		sb.append("Grades given: ");
		sb.append(grades.size());
		return sb.toString();
	}
	
	/**
	 * liefert ein Filter-Pr�dikat zur�ck, das alle Pr�fungsnoten
	 * mit der �bergebenen Note selektiert.
	 * 
	 * @param grade
	 * @return
	 */
	public Predicate<ExaminationGrade> filterGradesByGrade(double grade){
		return (ExmGrd -> ExmGrd.getGrade() == grade);
	}
	
	/**
	 * liefert ein Filter-Pr�dikat zur�ck, das alle Pr�fungsnoten 
	 * mit dem �bergebenen Student selektiert.
	 * 
	 * @param student
	 * @return
	 */
	public Predicate<ExaminationGrade> filterGradesByStudent(Student student){
		return (ExmGrd -> ExmGrd.getStudent() == student);
	}
	
	/**
	 * 	bekommt ein Filter-Pr�dikat �bergeben und liefert alle
	 *  Pr�fungsnoten der Pr�fung in einer java.util.List zur�ck,
	 *  die der Filter nicht aussortiert hat.
	 * 
	 * @param filter
	 * @return
	 */
	public List<ExaminationGrade> getFilteredGrades(Predicate<ExaminationGrade> filter){
		return grades.stream().filter(filter).collect(Collectors.toList());
	}
	
	/**
	 * liefert den Durchschnitt aller Noten der Pr�fung zur�ck
	 * 
	 * @return
	 */
	public double getAverageGrade(){
		return grades.stream().mapToDouble(a -> a.getGrade()).average().getAsDouble();
	}
	
	
	/**
	 * liefert die Notenverteilung in Form einer HashMap zur�ck.
	 * Die Keys der HashMap entsprechen der Note und die Values
	 * der HashMap, die entsprechende Anzahl der H�ufigkeit 
	 * der Note. Gibt es bei  einer Pr�fung beispielsweise 
	 * 3x1.0 , 2x4.0 sowie 5x2.3, so sollte die HashMap 
	 * folgenderma�en aussehen, wenn man die toString() 
	 * Methode verwendet um Sie zu visualisieren: {1.0=3, 4.0=2, 2.3=5}.
	 *  
	 * @return
	 */
	public Map<Double, Integer> getDistributionOfGrades(){
		Map<Double, Integer> NV = new HashMap<Double, Integer>();
		grades.stream().forEach(a -> NV.put(a.getGrade(), 0));
		grades.stream().forEach(a -> NV.put(a.getGrade(), NV.get(a.getGrade()) + 1));
		return NV;
	}
}
