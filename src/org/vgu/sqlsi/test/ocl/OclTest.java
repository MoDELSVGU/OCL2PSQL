package org.vgu.sqlsi.test.ocl;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;


import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclException;
import org.vgu.sqlsi.ocl.expressions.OclExpression;

public class OclTest {

	private MyObject to;

	@Before
	public void setUp() {
		to = new MyObject("Robert", "Herschke");
		to.setAge((int) Math.round(Math.random() * Integer.MAX_VALUE));
		to.setChildren((int) Math.round(Math.random() * Integer.MAX_VALUE));
		to.setIncome(Math.random() * Double.MAX_VALUE);
		to.setOutcome(Math.random() * Double.MAX_VALUE);
		to.setFemale(false);
		to.setMale(true);
		to.setMainAddress(new MySecondObject("street0", "city0", 65760));
		to.addAddress(to.getMainAddress());
		to.addAddress(new MySecondObject("street1", "city1", 65760));
		to.addAddress(new MySecondObject("street5", "city5", 65760));
		to.addAddress(new MySecondObject("street2", "city2", 65760));
		to.addAddress(new MySecondObject("street6", "city6", 65760));
		to.addAddress(new MySecondObject("street3", "city3", 65760));
		to.addAddress(new MySecondObject("street7", "city7", 65760));
		to.addAddress(new MySecondObject("street4", "city4", 65760));
	}

	private void testOcl(String ocl, Object self, Object expected) {
		try {
			Object result = callOcl(ocl, self);
			assertEquals("Result for the OCL '" + ocl + "' is not valid!",
					expected, result);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Script is not executed, an exception is raised!");
		}
	}

	private Object callOcl(String ocl, Object self) throws OclException {
		DefaultOclContext context = new DefaultOclContext(self);
		System.out.print(ocl + " = ");
		Object result = OclExpression.eval(ocl, context);
		System.out.println(result == null ? "null" : "("
				+ result.getClass().getName() + ") " + result);
		return result;
	}

	@Test(expected = OclException.class)
	public void allInstancesTest() throws Exception {
		callOcl("de::herschke::ocl::MyObject::allInstances()->notEmpty()", null);
	}

	@Test
	public void simpleTest() {
		testOcl("self", to, to);
	}

	@Test
	public void propertyTest() {
		testOcl("self.firstname", to, "Robert");
	}

	@Test
	public void operationTest() {
		testOcl("self.greetings('Welcome')", to, "Welcome Robert Herschke");
	}

	@Test
	public void chainTest() {
		testOcl("self.greetings('Welcome').concat(' to the world!').length()",
				to, "Welcome Robert Herschke to the world!".length());
		testOcl("self.mainAddress.street", to, "street0");
	}

	@Test
	public void testIntegerArithmetic() {
		// Arithmetic for integers
		testOcl("self.age + self.children", to, to.getAge() + to.getChildren());
		testOcl("self.age - self.children", to, to.getAge() - to.getChildren());
		testOcl("self.age * self.children", to, to.getAge() * to.getChildren());
		testOcl("self.age / self.children", to, to.getAge() / to.getChildren());
		testOcl("self.age.mod(self.children)", to, to.getAge()
				% to.getChildren());
		testOcl("self.age.div(self.children)", to, to.getAge()
				/ to.getChildren());
		testOcl("self.age.min(self.children)", to, Math.min(to.getAge(), to
				.getChildren()));
		testOcl("self.age.max(self.children)", to, Math.max(to.getAge(), to
				.getChildren()));
	}

	@Test
	public void testRealArithmetic() {
		// Arithmetic for reals
		testOcl("self.income + self.outcome", to, to.getIncome()
				+ to.getOutcome());
		testOcl("self.income - self.outcome", to, to.getIncome()
				- to.getOutcome());
		testOcl("self.income * self.outcome", to, to.getIncome()
				* to.getOutcome());
		testOcl("self.income / self.outcome", to, to.getIncome()
				/ to.getOutcome());
		testOcl("self.income.min(self.outcome)", to, Math.min(to.getIncome(),
				to.getOutcome()));
		testOcl("self.income.max(self.outcome)", to, Math.max(to.getIncome(),
				to.getOutcome()));
		testOcl("self.income.round()", to, Math.round(to.getIncome()));
		testOcl("self.income.floor()", to, Math.floor(to.getIncome()));
	}

	@Test
	public void testMixedArithmetic() {
		// Arithmetic for mixed types
		testOcl("self.income + self.age", to, to.getIncome() + to.getAge());
		testOcl("self.income - self.children", to, to.getIncome()
				- to.getChildren());
		testOcl("self.income * self.age", to, to.getIncome() * to.getAge());
		testOcl("self.income / self.children", to, to.getIncome()
				/ to.getChildren());
		testOcl("self.age + self.outcome", to, to.getAge() + to.getOutcome());
		testOcl("self.children - self.outcome", to, to.getChildren()
				- to.getOutcome());
		testOcl("self.age * self.outcome", to, to.getAge() * to.getOutcome());
		testOcl("self.children / self.outcome", to, to.getChildren()
				/ to.getOutcome());

	}

	@Test
	public void testStringArithmetic() {
		// Arithmetic for string types
		testOcl("self.lastname + self.firstname", to, "HerschkeRobert");
		testOcl("self.lastname + ', ' + self.firstname", to, "Herschke, Robert");

	}

	@Test
	public void testBooleanArithmetic() {
		// Arithmetic for boolean types
		testOcl("self.female and self.male", to, false);
		testOcl("self.female and self.female", to, false);
		testOcl("self.male and self.male", to, true);
		testOcl("self.female or self.male", to, true);
		testOcl("self.female or self.female", to, false);
		testOcl("self.male or self.male", to, true);
		testOcl("self.female xor self.male", to, true);
		testOcl("self.female xor self.female", to, false);
		testOcl("self.male xor self.male", to, false);
		testOcl("self.female implies self.male", to, true);
		testOcl("self.male implies self.female", to, false);
		testOcl("self.female implies self.female", to, true);
		testOcl("self.male implies self.male", to, true);

	}

	@Test
	public void testStringOperations() {
		// String operations
		testOcl("self.lastname.concat(self.firstname)", to, "HerschkeRobert");
		testOcl("self.lastname.substring(4,6)", to, "sch");
		testOcl("self.lastname.toLower()", to, "herschke");
		testOcl("self.firstname.toUpper()", to, "ROBERT");
		testOcl("self.firstname.size()", to, 6);

	}

	@Test
	public void testEqualsOperations() {
		// equals/notEquals operations
		testOcl("self.firstname = self.firstname", to, true);
		testOcl("self.firstname = self.lastname", to, false);
		testOcl("self.firstname <> self.firstname", to, false);
		testOcl("self.firstname <> self.lastname", to, true);
		testOcl("self.female = self.female", to, true);
		testOcl("self.female = self.male", to, false);
		testOcl("self.male <> self.female", to, true);
		testOcl("self.male <> self.male", to, false);
		testOcl("self.age = self.age", to, true);
		testOcl("self.age = self.children", to, (to.getAge() == to
				.getChildren()));
		testOcl("self.age <> self.age", to, false);
		testOcl("self.age <> self.children", to, (to.getAge() != to
				.getChildren()));
		testOcl("self.income = self.income", to, true);
		testOcl("self.income = self.outcome", to, (to.getIncome() == to
				.getOutcome()));
		testOcl("self.income <> self.income", to, false);
		testOcl("self.income <> self.outcome", to, (to.getIncome() != to
				.getOutcome()));

	}

	@Test
	public void testCompareOperations() {
		// compare operations
		testOcl("self.age > self.children", to,
				(to.getAge() > to.getChildren()));
		testOcl("self.age >= self.children", to, (to.getAge() >= to
				.getChildren()));
		testOcl("self.age < self.children", to,
				(to.getAge() < to.getChildren()));
		testOcl("self.age <= self.children", to, (to.getAge() <= to
				.getChildren()));
		testOcl("self.income > self.outcome", to, (to.getIncome() > to
				.getOutcome()));
		testOcl("self.income >= self.outcome", to, (to.getIncome() >= to
				.getOutcome()));
		testOcl("self.income < self.outcome", to, (to.getIncome() < to
				.getOutcome()));
		testOcl("self.income <= self.outcome", to, (to.getIncome() <= to
				.getOutcome()));

	}

	@Test
	public void testOclAnyOperations() {
		// OclAny operations
		testOcl("self.firstname.oclIsUndefined()", to, false);
		testOcl("self.anything.oclIsUndefined()", to, true);

	}

	@Test
	public void testOclUndefined() {
		testOcl("self.anything.name", to, null);
	}

	@Test
	public void testOclTypeSupport() {
		testOcl("self.firstname.oclIsKindOf(java::lang::String)", to, true);
		testOcl("self.lastname.oclIsKindOf(String)", to, true);
		testOcl("self.age.oclIsKindOf(Integer)", to, true);
		testOcl("self.income.oclIsKindOf(Real)", to, true);
		testOcl("self.male.oclIsKindOf(Boolean)", to, true);
		testOcl("self.oclIsKindOf(de::herschke::ocl::MyObject)", to, true);
		testOcl("self.oclIsKindOf(MyObject)", to, true);
		testOcl("self.anything.oclIsKindOf(OclVoid)", to, true);
		testOcl("self.anything.type = OclVoid", to, true);
		testOcl("self.firstname.type = String", to, true);
		testOcl("self.lastname.type = String", to, true);
		testOcl("self.age.type = Integer", to, true);
		testOcl("self.income.type = Real", to, true);
		testOcl("self.mainAddress.type = de::herschke::ocl::MySecondObject",
				to, true);
	}

	@Test
	public void testOclIfSupport() {
		testOcl(
				"self.firstname + if self.female then 'male' endif + self.lastname",
				to, "RobertnullHerschke");
		testOcl(
				"self.firstname + if self.male then 'male' else 'female' endif + self.lastname",
				to, "RobertmaleHerschke");
		testOcl(
				"if self.firstname + if self.male then 'male' else 'female' endif + self.lastname = 'RobertmaleHerschke' then 'male' else 'female' endif",
				to, "male");
		testOcl("if false then true endif", to, null);
	}

	@Test
	public void testShortFormCollect() {
		testOcl("self.addresses.zipcode->size()", to, to.getAddresses().size());
	}

	@Test
	public void testOclCollectionSupport() {
		testOcl("self.addresses->isEmpty()", to, false);
		testOcl("self.addresses->notEmpty()", to, true);
		testOcl("self.addresses->size()", to, to.getAddresses().size());
		testOcl("self.addresses->count(self.mainAddress)", to, 1);
		testOcl("self.mainAddress->isEmpty()", to, false);
		testOcl("self.mainAddress->notEmpty()", to, true);
		testOcl("self.mainAddress->size()", to, 1);
		testOcl("self.mainAddress->first()", to, to.getMainAddress());
		testOcl("self.mainAddress->last()", to, to.getMainAddress());
		testOcl("self.anything->isEmpty()", to, true);
		testOcl("self.anything->notEmpty()", to, false);
		testOcl("self.anything->size()", to, 0);
		testOcl("self.addresses->asSequence()->indexOf(self.mainAddress) > 0",
				to, true);
		testOcl("self.addresses->includes(self.mainAddress)", to, true);
		testOcl("self.addresses->excludes(self.mainAddress)", to, false);
		testOcl("self.addresses->includesAll(self.mainAddress)", to, true);
		testOcl("self.addresses->excludesAll(self.mainAddress)", to, false);
		testOcl("self.addresses->includesAll(self.addresses)", to, true);
		testOcl("self.addresses->excludesAll(self.addresses)", to, false);
		testOcl("self.addresses->includesAll(self.anything)", to, true);
		testOcl("self.addresses->excludesAll(self.anything)", to, true);
		testOcl("self.addresses.zipcode->sum()", to,
				to.getAddresses().size() * 65760d);
		testOcl("self.addresses = self.mainAddress", to, false);
		testOcl("self.addresses = self.addresses", to, true);
		testOcl("(self.addresses - self.mainAddress)->size()", to, to
				.getAddresses().size() - 1);
		testOcl(
				"(self.addresses - self.mainAddress)->excludes(self.mainAddress)", to,
				true);
		testOcl(
				"(self.addresses - self.mainAddress)->including(self.mainAddress) = self.addresses",
				to, true);
		testOcl("self.addresses->including(self.mainAddress)->size() = self.addresses.size()", to, true);
		testOcl("Bag{self}->including(self)->size()", to, 1);
	}

	@Test
	public void testOclIteratorSupport() {
		testOcl(
				"self.addresses->collect(a|a.street + ' ' + a.zipcode + ' ' + a.city)->asSequence()->first().startsWith('street')",
				to, true);
		testOcl("self.addresses->exists(a|a.street = 'street1')", to, true);
		testOcl(
				"self.addresses->select(a|a.street = 'street2')->asSequence()->first().city",
				to, "city2");
		testOcl(
				"self.addresses->reject(a|a.street = 'street2')->size() = self.addresses->size() - 1",
				to, true);
		testOcl(
				"self.addresses->any(a|a.street.startsWith('street')).street.substring(1,6)",
				to, "street");
		testOcl("self.addresses->one(a|a.street = 'street1')", to, true);
		testOcl("self.addresses->isUnique(a|a.street)", to, true);
		testOcl(
				"self.addresses->sortedBy(a|a.street)->asSequence()->first().street",
				to, "street0");
		testOcl(
				"self.addresses->sortedBy(a|a.street)->asSequence()->last().street",
				to, "street" + (to.getAddresses().size() - 1));
	}

	@Test
	public void testLetInSupport() {
		testOcl("let a = 7577 in a", to, 7577);
		testOcl(
				"let a:String = 'test' in if a = 'test' then 'tast' else 'tust' endif",
				to, "tast");
		testOcl(
				"let a:de::herschke::ocl::MySecondObject = self.mainAddress in a.street",
				to, "street0");
		testOcl(
				"'tist' + let a:String = 'test' in if a = 'test' then 'tast' else 'tust' endif + 'test'",
				to, "tisttasttest");
		testOcl(
				"let address = self.addresses->select(a|a.street = 'street2')->asSequence() in (let size = address->size() in size=1) and address->first().city = 'city2'",
				to, true);
	}

	@Test
	public void testCollectionLiterals() {
		testOcl("let a:Set(Integer) = Set{1,2,3,4,5,6,7,8,9,10} in a->size()",
				null, 10);
		testOcl(
				"let a:Sequence(String) = Sequence{'a','b','c','d','e'} in a->size() + a->last()",
				null, "5e");
		testOcl(
				"let a:OrderedSet(String) = OrderedSet{'x','k','z','a','s'} in a->first()",
				null, "a");
	}

	@Test
	public void testIterateSupport() {
		testOcl(
				"let a:OrderedSet(String) = OrderedSet{'x','k','z','a','s'} in a->iterate(s;acc:String=''| acc + s)",
				null, "aksxz");
		testOcl(
				"let a:OrderedSet(String) = OrderedSet{'x','k','z','a','s'} in a->iterate(s;acc:String| acc + s)",
				null, "aksxz");
		testOcl(
				"let a:Set(Integer) = Set{1,2,3,4,5,6,7,8,9,10} in a->iterate(x;y:Integer=1|y * x)",
				null, 3628800);
	}

	@Test
	public void testEnumSupport() {
		testOcl("de::herschke::ocl::MyEnum::test2", null, MyEnum.test2);
		testOcl("MyEnum::test1", null, MyEnum.test1);
		testOcl("Set{MyEnum::test2, MyEnum::test3}->size()", null, 2);
	}

	@Test
	public void testClassFieldSupport() {
		testOcl("de::herschke::ocl::MyObject::myStaticText", null,
				MyObject.myStaticText);
	}

	@Test
	public void testClassMethodSupport() {
		testOcl("de::herschke::ocl::MyObject::myStaticText()", null, MyObject
				.myStaticText());
	}

	@Test
	public void testException() {
		try {
			callOcl("self.someError()", to);
			fail("an OCLEvaluationException must be raised!");
		} catch (Exception e) {
			assertEquals(MyException.class, e.getCause().getCause().getClass());
		}
	}
}
