package modeling.ocl.sql;
import java.util.ArrayList;
import java.util.List;

import modeling.ocl.sql.OCL2PSQL;

public class Runner {
	public static void main (String[] args) throws Exception {
		
		String dataModelFilePath = null;
		List<String> contextVars = new ArrayList<String>();
		List<String> contextTypes = new ArrayList<String>();
		String ocl = null;
		
		for (int i = 0; i < args.length; i++) {
			if ("-dm".equalsIgnoreCase(args[i])) {
				dataModelFilePath = args[++i];
			} else if ("-ctx".equalsIgnoreCase(args[i])) {
				while (!args[i + 1].contains("-")) {
					String[] s = args[i + 1].split(":");
					contextVars.add(s[0]);
					contextTypes.add(s[1]);
					i++;
				}
			} else if ("-ocl".equalsIgnoreCase(args[i])) {
				ocl = args[i + 1];
			}
		}
		
		if (dataModelFilePath == null) {
			throw new Exception("Missing input datamodel file path!");
		}

		if (ocl == null) {
			throw new Exception("Missing OCL expression!");
		}
		
		OCL2PSQL ocl2psql = new OCL2PSQL();
		try {
			ocl2psql.setDataModelFromFilePath(dataModelFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < contextVars.size(); i++) {
			ocl2psql.setContextualType(contextVars.get(i), contextTypes.get(i));
		}
		
		String sql = ocl2psql.mapToString(ocl);
		System.out.println(sql);
	}
}
