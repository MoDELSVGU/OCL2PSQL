import org.vgu.ocl2psql.OCL2PSQL;

public class Main {
	public static void main (String[] args) {
		OCL2PSQL ocl2psql = new OCL2PSQL();
		try {
			ocl2psql.setDataModelFromFilePath("src/main/resources/context-model/CarPerson_context-new-model.json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ocl2psql.setContextualType("kcaller", "Lecturer");
//		/* "ocl": "true",
//		   "sql": "TRUE" */
//		ocl2psql.setContextualType("kstudents", "Student");
		String sql = ocl2psql.mapToString("Car.allInstances()->collect(c|c.owners->forAll(p|p.name='Artur'))");
		System.out.println(sql);
		System.out.println();
		
//		/* "ocl": "kcaller.students->exists(s|s=kstudents)",
//		   "sql": "EXISTS (SELECT 1 FROM Enrollment WHERE lecturers = kcaller AND kstudents = students)" */
//		ocl2psql.setContextualType("klecturers", "Lecturer");
//		sql = ocl2psql.mapToString("kcaller.students->exists(s|s.lecturers->exists(l|l=klecturers))");
//		System.out.println(sql);
//		System.out.println();
//		
//		/* "ocl": "kcaller = kself",
//		   "sql": "kcaller = kself" */
//		ocl2psql.setContextualType("kself", "Lecturer");
//		sql = ocl2psql.mapToString("kcaller=kself");
//		System.out.println(sql);
//		System.out.println();
//		
//		/* "ocl": "kcaller.students->exists(s|s = kself)",
//		   "sql": "EXISTS (SELECT 1 FROM Enrollment WHERE lecturers = kcaller AND kself = students)" */
//		ocl2psql.setContextualType("kself", "Student");
//		sql = ocl2psql.mapToString("kcaller.students->exists(s|s = kself)");
//		System.out.println(sql);
//		System.out.println();
//		
//		/* "ocl": "kcaller.students->exists(s|s.lecturers->exists(l|l=kself))",
//		   "sql": "EXISTS (SELECT 1 FROM Enrollment e1 JOIN Enrollment e2 ON e1.students = e2.students WHERE e1.lecturers = kcaller AND e2.lecturers = kself)" */
//		ocl2psql.setContextualType("kself", "Lecturer");
//		sql = ocl2psql.mapToString("kcaller.students->exists(s|s.lecturers->exists(l|l=kself))");
//		System.out.println(sql);
//		System.out.println();
//		
//		/* "ocl": "kcaller.students->exists(s|s.lecturers->exists(l|l=klecturers))",
//		   "sql": "EXISTS (SELECT 1 FROM Enrollment e1 JOIN Enrollment e2 ON e1.students = e2.students WHERE e1.lecturers = kcaller AND e2.lecturers = klecturers)" */
//		ocl2psql.setContextualType("klecturers", "Lecturer");
//		sql = ocl2psql.mapToString("kcaller.students->exists(s|s.lecturers->exists(l|l=klecturers))");
//		System.out.println(sql);
//		System.out.println();
		
	}
}
