package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import demo.MyConnection;

class MyConnectionTest {

	@Test
	void init() throws ClassNotFoundException {
		assertNotEquals(null, new MyConnection());
	}

}
