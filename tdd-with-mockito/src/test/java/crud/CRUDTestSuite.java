package crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public abstract class CRUDTestSuite {
	private CRUDInterface<TestObject> crudObject;
	
	private final String ID = "test.id";
	private final String DATA = "test.data";
	private TestObject testObject;
	
	protected abstract CRUDInterface<TestObject> getCrudObject();
	

	@BeforeEach
	public void before() {
		this.crudObject = getCrudObject();
		this.testObject = createTestObject(ID, DATA);
	}
	
	@Test
	public void createAndRead() {
		crudObject.create(testObject);
		
		TestObject resultObject = crudObject.read(ID);
		
		assertEquals(testObject, resultObject);
	}
	
	@Test
	public void update() {
		TestObject updateObject = createTestObject(testObject.getId(), "update.data");
		
		crudObject.create(testObject);
		crudObject.update(updateObject.getId(), updateObject);
		
		TestObject resultObject = crudObject.read(ID);
		
		assertEquals(updateObject.getId(), resultObject.getId());
		assertEquals(updateObject.getData(), resultObject.getData());
	}
	
	protected TestObject createTestObject(String id, String data) {
		// Mocking an interface: This grants control for all methods of the mocked interface (here TestObject)
		TestObject object = Mockito.mock(TestObject.class);

		// define a expected return values for the mocked methods using this syntax:
		// Mockito.when(<mocked object>.<mocked method()).thenReturn(<expected return value>);
		Mockito.when(object.getId()).thenReturn(id);
		Mockito.when(object.getData()).thenReturn(data);
		return object;
	}


	@Test
	public void delete() {		
		crudObject.create(testObject);
		crudObject.delete(testObject.getId());
		
		TestObject resultObject = crudObject.read(ID);
		
		assertNull(resultObject);
	}
	
	
	
}
