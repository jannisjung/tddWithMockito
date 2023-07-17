package crud;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.reset;

import java.util.Map;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

public class TestCachedCRUD extends CRUDTestSuite {
	
	private static Map<String, TestObject> mockStorage = new HashMap<>();

	// The spy method allows to partially mock classes and abstract classes.
	// I.e., mocked methods are overwritten, all other implemented methods can be used as is.
	@SuppressWarnings("unchecked")
	private static CachedCRUD<TestObject> cachedCRUDMock = Mockito.spy(CachedCRUD.class);
	TestObject testObject = createTestObject("test", "data");
	
	
	@BeforeAll
	public static void beforeClass() {
		// To add dynamic behaviour to methods with a return value use this syntax:
		// Mockito.when(<mocked object>.<mocked method>(any(<Parameter>.class[, any(...)]))).then(invocation -> <dynamic logic>)); // invocation provides the arguments of the method signature via invocation.getArgument(<index>)
		Mockito.when(cachedCRUDMock.read(any(String.class))).then(invocation -> mockStorage.get(invocation.getArgument(0)));
		
		// To add dynamic behaviour to methods with no return value (void) use this syntax:
		// Mockito.doAnswer(invocation -> <invocation logic; return null>).when(<mocked object>).<mocked void method>(any(<Parameter>.class)[, any(...)]));
		Mockito.doAnswer(invocation -> {
			 TestObject object = invocation.getArgument(0);
			 mockStorage.put(object.getId(), object);
			 return null;
		 }).when(cachedCRUDMock).createCached(any(TestObject.class));

		 Mockito.doAnswer(invocation -> {
			 String id = invocation.getArgument(0);
			 TestObject object = invocation.getArgument(1);
			 mockStorage.put(id, object);
			 return null;
		 }).when(cachedCRUDMock).updateCached(any(String.class), any(TestObject.class));
		 
		 Mockito.doAnswer(invocation -> {
			 String id = invocation.getArgument(0);
			 mockStorage.remove(id);
			 return null;
		 }).when(cachedCRUDMock).deleteCached(any(String.class));
	}
	
	@SuppressWarnings("unchecked")
	@AfterEach
	public void after() {
		reset(cachedCRUDMock);
	}
	
	@Test
	public void whenCreate_updateCache_isCalled() {
		cachedCRUDMock.create(testObject);
		Mockito.verify(cachedCRUDMock).updateCache(eq(testObject.getId()), eq(testObject));
	}
	
	@Test
	public void whenRead_isInCache_isCalled() {
		cachedCRUDMock.create(testObject);
		cachedCRUDMock.read(testObject.getId());
		Mockito.verify(cachedCRUDMock).isInCache(eq(testObject.getId()));
		
	}
	
	@Test
	public void whenUpdate_updateCache_isCalled() {
		TestObject updateObject = createTestObject(testObject.getId(), "updated");
		cachedCRUDMock.create(testObject);
		cachedCRUDMock.update(testObject.getId(), updateObject);
		
		Mockito.verify(cachedCRUDMock, times(2)).updateCache(any(String.class), any(TestObject.class));
	}

	@Override
	protected CRUDInterface<TestObject> getCrudObject() {
		return cachedCRUDMock; 
	}
	
	
}
