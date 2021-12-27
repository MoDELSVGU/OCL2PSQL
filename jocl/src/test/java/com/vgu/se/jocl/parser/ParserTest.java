/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author: thian
***************************************************************************/

package com.vgu.se.jocl.parser;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.dm2schema.dm.DataModel;

import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.parser.simple.SimpleParser;

public class ParserTest {

    @SuppressWarnings("unused")
    private static JSONArray plainUMLContext;
    @SuppressWarnings("unused")
    private static String oclExpStr;

    @SuppressWarnings("unused")
    private static Map<Integer, Integer> testInt = new HashMap<>();

    static {
        String s = "[" + "{\"class\" : \"Car\","
                + "\"attributes\" : [{\"name\" : \"color\", \"type\" : \"String\"}]"
                + "}," + "{\"class\" : \"Person\","
                + "\"attributes\" : [{\"name\" : \"name\", \"type\" : \"String\"}]"
                + "}," + "{\"association\" : \"Ownership\","
                + "\"ends\" : [\"owners\", \"ownedCars\"],"
                + "\"classes\" : [\"Car\", \"Person\"]" + "}" + "]";

        try {
            plainUMLContext = (JSONArray) new JSONParser().parse(s);
            oclExpStr = "Car.allInstances() -> forAll(c|c.owners -> select(p|p.name='Hoang')->forAll(c|c.color = 'blue')) -> collect(d|d->forAll(d2|d2))";
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException, Exception {
        SimpleParser parser = new SimpleParser();
        String[] ocls = getTestCases();

        File dataModelFile = new File("src/test/resources/uni_pof_dm.json");
        DataModel dataModel = new DataModel(new JSONParser().parse(
                new FileReader(dataModelFile.getAbsolutePath())));
        
        for (int i = 0; i < ocls.length; i++) {
            Expression result = parser.parse(ocls[i], dataModel);
            System.out.println(result);
        }
    }

    @SuppressWarnings("unused")
    private static String trim(String input) {

        String spacePatt = "^(\\s)*|(\\s)*$";
        Matcher m = Pattern.compile(spacePatt).matcher(input);
        if (m.find()) {
            return input.replaceAll(spacePatt, "");
        }

        return input;
    }
    
    private static String[] getTestCases() {
        String[] ocls = {
//                "Student.allInstances()->collect(s|s.enrolls)",
//                "Student.allInstances()->forAll(s| @SQL(timestampdiff(years, student.dob, curdate())) > 18)",
//                "Student.allInstances()->forAll(s| @SQL(CURDATE()))",
//                "@SQL(CURDATE())",
//                "Module_Period.allInstances() ->forAll(m|m.ends < m.period.ends and m.starts > m.period.starts)",
                "University.allInstances()->collect(u|u.programs.modules.module_periods)"

//                "Account.allInstances()->forAll(a| not a.student.oclIsUndefined() implies a.lecturer.oclIsUndefined() and a.assistant.oclIsUndefined())",
//                "Account.allInstances() ->forAll(a| not a.lecturer.oclIsUndefined()  implies a.student.oclIsUndefined() and a.assistant.oclIsUndefined())",
//                "Account.allInstances() ->forAll(a| not a.assistant.oclIsUndefined()  implies a.lecturer.oclIsUndefined() and a.student.oclIsUndefined())",
//                "Enrollment.allInstances() ->forAll(e|e.ends.oclIsUndefined() or e.ends > e.starts)",
//                "Enrollment.allInstances() ->forAll(e|e.starts > e.program.doe)",
//                "Enrollment.allInstances() ->forAll(e1| Enrollment.allInstances() ->forAll(e2| e1 <> e2 implies (e1.program.university = e2.program.university implies (e1.student <> e2.student implies e1.student.code <> e2.student.code))))",
//                "Enrollment.allInstances() ->forAll(e1|Enrollment.allInstances() ->forAll(e2|e1.student = e2.student implies e1.starts > e2.ends or e1.ends < e2.starts))",
//                "Exam.allInstances() ->forAll(e|e.starts < e.ends)",
//                "Exam.allInstances() ->forAll(e|@SQL(TIMESTAMPDIFF(day,e.date,e.deadline)) > 1)",
//                "Exam.allInstances() ->forAll(e1|Exam.allInstances() ->select(e2|e2.module_period = e1.module_period) ->size() <= 2)",
//                "Exam.allInstances() ->forAll(e1|Exam.allInstances() ->forAll(e2| (e2 = e1) or (e2.module_period <> e1.module_period) or e2.starts > e1.ends or e2.ends < e1.starts))",
//                "Exam.allInstances() ->forAll(e1|Exam.allInstances() ->forAll(e2| e1 = e2 or e1.module_period.module.module_group = e2.module_period.module.module_group implies e1.starts > e2.ends or e1.ends < e2.starts))",
//                "Exam.allInstances() ->forAll(e| Session.allInstances() ->select(s| s.module_period = e.module_period) ->forAll(s| s.date < e.date))",
//                "Exam.allInstances() ->forAll(e1| Exam.allInstances() ->forAll(e2| e1 <> e2 implies (e1.module_period = e2.module_period implies (e1.date <> e2.date) or (e1.starts > e2.ends or e1.ends < e2.starts))))",
//                "Exam.allInstances()->forAll(e1| Exam.allInstances() ->forAll(e2| e1 <> e2 implies e1.rooms -> forAll( r1 | e2.rooms -> forAll( r2 | r1 <> r2 ))))",
//                "Module.allInstances() ->forAll(m|m.prerequisites ->forAll(p| m <> p))",
//                "Program.allInstances()->forAll(p|p.modules->collect(m|m.module_group.group_name)->isUnique())",
//                "Program.allInstances()->forAll(p|p.modules->collect(m|m.module_group.code)->isUnique())",
//                "Program.allInstances()->forAll(p| p.modules->collect(m| m.name)->isUnique())",
//                "Program.allInstances()->forAll(p| p.modules->collect(m| m.code)->isUnique())",
//                "Module_Period.allInstances() ->forAll(mp1|Module_Period.allInstances() ->forAll(mp2| (mp1 = mp2) or ((mp1.module.code = mp2.module.code and mp1.period.starts = mp2.period.starts) implies mp1.lecturer <> mp2.lecturer)))",
//                "Module_Period.allInstances() ->forAll(m|m.starts < m.ends)",
//                "Module_Period.allInstances() ->forAll(m|m.ends < m.period.ends and m.starts > m.period.starts)",
//                "Period.allInstances() ->forAll(p| p.starts < p.ends)",
//                "Program.allInstances() ->forAll(p|p.doe > p.university.doe)",
//                "Program.allInstances() ->forAll(p|p.doe < @SQL(CURDATE()))",
//                "Record.allInstances() ->forAll(r1| Record.allInstances() ->collect(r3| r1.student = r3.student and r3.module_period.module = r1.module_period.module and r3.status = 'failed' ) ->size() <=3)",
//                "Record.allInstances() ->forAll(r1| Record.allInstances() ->forAll(r2| (r1 = r2) or (r1.student = r2.student and r1.module_period.module = r2.module_period.module and r1.status = 'passed') implies r1.module_period.starts > r2.module_period.ends))",
//                "Record.allInstances() ->forAll(r1| Record.allInstances() ->forAll(r2| (r1 = r2) or (r1.student <> r2.student) or r1.sessions->forAll(s1| r2.sessions-> forAll(s2| s1.starts > s2.ends or s1.ends < s2.starts)) ))",
//                "Record.allInstances() ->forAll(r1| Record.allInstances() ->forAll(r2| (r1 = r2) or (r1.student <> r2.student) or r1.sessions->forAll(s1| r2.sessions-> forAll(s2| s1.starts > s2.ends or s1.ends < s2.starts)) ))",
//                "Record.allInstances() ->forAll(r| r.sessions->forAll(s|s.module_period = r.module_period))",
//                "Registration_Exam.allInstances() ->forAll(re1| Registration_Exam.allInstances() ->forAll(re2| re1.record.student = re2.record.student implies (re1 <> re2 implies (re1.exam.starts > re2.exam.ends or re1.exam.ends < re2.exam.starts))))",
//                "Registration_Exam.allInstances() ->forAll(re| re.record.module_period = re.exam.module_period)",
//                "Registration_Exam.allInstances() ->forAll(re1| re1.record.status = 'passed' implies Registration_Exam.allInstances() ->forAll(re2|re2 = re1 or re2.exam.module_period.module <> re1.exam.module_period.module or re1.datetime > re2.datetime)) ",
//                "Registration_Exam.allInstances() ->forAll(re| re.datetime <= re.exam.deadline)",
//                "Registration_Exam.allInstances() ->forAll(re| re.record.module_period = re.exam.module_period)",
//                "Session.allInstances() ->forAll(s| s.date > s.module_period.starts and s.date < s.module_period.ends)",
//                "Session.allInstances() ->forAll(s| s.starts < s.ends)",
//                "Session.allInstances() ->forAll(s1| Session.allInstances() ->forAll(s2| (s1 = s2) or (s1.module_period = s2.module_period implies s1.date <> s2.date or s1.starts > s2.ends or s1.ends < s2.starts)))",
//                "Session.allInstances() ->forAll(s1| Session.allInstances() ->forAll(s2| (s1 = s2) or (s1.room = s2.room implies s1.date <> s2.date or s1.starts > s2.ends or s1.ends < s2.starts)))",
//                "Session.allInstances() ->forAll(s1| Session.allInstances() ->forAll(s2| (s1 <> s2 implies (s1.module_period.lecturer = s2.module_period.lecturer implies (s1.date = s2.date implies (s1.starts > s2.ends or s1.ends < s2.starts))))))",
//                "Student.allInstances() ->forAll(s|@SQL(TIMESTAMPDIFF(year,s.dob,@SQL(CURDATE()))) > 17)",
//                "University.allInstances() ->forAll(u|u.programs->collect(p|p.name)->isUnique())",
//                "University.allInstances() ->forAll(u|u.programs->collect(p|p.code)->isUnique())",
//                "University.allInstances() ->forAll(u|u.doe < @SQL(CURDATE()))",

//             "Car.allInstances()",
//             "Car.allInstances()->asSet()",
//             "Car.allInstances()->collect(c|c.color < @SQL_CURDATE())",
//             "Car.allInstances()->collect(c|c.color < @SQL(CURDATE()) )",
//             "Car.allInstances()->collect(c|c.owners)",
//             "Car.allInstances()->collect(c|c.color)->asSet()",
//             "Car.allInstances()->collect(c|c.color)->isEmpty()",
//             "Car.allInstances()->collect(c|c.color)->notEmpty()",
//             "Car.allInstances()->collect(c|c.color)->size()",
//             "Car.allInstances()->collect(c|c.owners)",
//             "Car.allInstances()->collect(c|c.owners)->flatten()",
//             "Car.allInstances()->collect(c|c.owners)->flatten()->size()",
//             "Car.allInstances()->collect(c|c.owners)->size()",
//             "Car.allInstances()->collect(c|c.owners->asSet())->flatten()",
//             "Car.allInstances()->collect(c|c.owners->isEmpty())",
//             "Car.allInstances()->collect(c|c.owners->notEmpty())",
//             "Car.allInstances()->exists(c|c.color <> 'no-color')",
//             "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'no-name'))",
//             "Car.allInstances()->exists(c|c.owners->isEmpty())",
//             "Car.allInstances()->exists(c|c.owners->notEmpty())",
//             "Car.allInstances()->forAll(c|'blue'=c.color)",
//             "Car.allInstances()->forAll(c|c.owners->collect(p|p)->size() = 1)",
//             "Car.allInstances()->forAll(c|c.owners->collect(p|p.ownedCars)->flatten()->size()=1)",
//             "Car.allInstances()->forAll(c|c.owners->exists(p|p.name = 'no-name'))",
//             "Car.allInstances()->forAll(c|c.owners->forAll(p|p.ownedCars->forAll(c1|c1.color=c.color)))",
//             "Car.allInstances()->forAll(c|c.owners->isEmpty())",
//             "Car.allInstances()->forAll(c|c.owners->notEmpty())",
//             "Car.allInstances()->forAll(c|c.owners->select(p|p.name.oclIsUndefined())->size()=0)",
//             "Car.allInstances()->isEmpty()",
//             "Car.allInstances()->notEmpty()",
//             "Car.allInstances()->select(c|c.color = 'no-color')",
//             "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//             "Car.allInstances()->select(c|c.color.oclIsUndefined())",
//             "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//             "Car.allInstances()->select(c|c.owners->exists(p|p.name.oclIsUndefined()))->size()",
//             "Car.allInstances()->size()",
//             "Person.allInstances()->collect(p|p.ownedCars->size())",
//             "Person.allInstances()->exists(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//             "Person.allInstances()->exists(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//             "Person.allInstances()->exists(p|p.ownedCars->size() < 10)",
//             "Person.allInstances()->exists(p|p.ownedCars->size() = 10)",
//             "Person.allInstances()->forAll(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//             "Person.allInstances()->forAll(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//             "Person.allInstances()->forAll(p|p.ownedCars->size() > 0)",
//             "Person.allInstances()->forAll(p|true)",
//             "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color <> 'no-color'))",
//             "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color.oclIsUndefined()))",
//             "Person.allInstances()->select(p|p.ownedCars->forAll(c|c.color <> 'no-color'))",
//             "true",
//             "self",
//             "self = caller",
//             "self.name",
//             "self.name = 'Hoang'",
//             "self.ownedCars",
//             "self.ownedCars->size()",
//             "self.ownedCars->exists(c|c.color = 'black')",
//             "Car.allInstances()->exists(c|c.owners->exists(p|p = self))",
//             "Car.allInstances()->exists(c|c.owners->exists(p|p.ownedCars->size() < self.ownedCars->size()))",
//             "Car.allInstances()->exists(c| c.color = 'blue')",

        };
        
        return ocls;
    }

}
