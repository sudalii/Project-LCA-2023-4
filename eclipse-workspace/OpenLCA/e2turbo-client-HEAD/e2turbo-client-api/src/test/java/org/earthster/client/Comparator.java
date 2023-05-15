package org.earthster.client;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * A class for object comparison.
 */
public class Comparator {

	private Object object1;
	private Object object2;

	/**
	 * Creates a new comparator for the given objects.
	 */
	public Comparator(Object obj1, Object obj2) {
		this.object1 = obj1;
		this.object2 = obj2;
	}

	/**
	 * Returns true if every field of object 1 is equal to the respective field
	 * in object 2.
	 */
	public boolean areFieldsEqual() {
		boolean areEqual = false;
		if (isInputValid()) {
			try {
				Class<?> clazz = object1.getClass();
				areEqual = areFieldsEqual(clazz.getDeclaredFields())
						&& areFieldsEqual(clazz.getFields());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return areEqual;
	}

	/**
	 * Returns true if the fields with the given names are equal in the both
	 * objects.
	 */
	public boolean areFieldsEqual(String... fieldNames) {
		boolean areEqual = false;
		if (isInputValid()) {
			try {
				Class<?> clazz = object1.getClass();
				Field[] fields = new Field[fieldNames.length];
				for (int i = 0; i < fields.length; i++) {
					Field field = clazz.getDeclaredField(fieldNames[i]);
					fields[i] = field;
				}
				areEqual = areFieldsEqual(fields);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return areEqual;
	}

	/**
	 * Returns true if the input of the comparator is valid. It is valid if both
	 * objects are not null and have the same class.
	 */
	private boolean isInputValid() {
		return object1 != null && object2 != null
				&& object1.getClass().equals(object2.getClass());
	}

	/**
	 * Tests if the given field values are equal for the two objects.
	 */
	private boolean areFieldsEqual(Field[] fields)
			throws IllegalAccessException {
		boolean areEqual = true;
		int i = 0;
		while (areEqual && i < fields.length) {
			areEqual = isFieldEqual(fields[i]);
			i++;
		}
		return areEqual;
	}

	/**
	 * Returns true if the values of the given field are equal in both objects.
	 */
	private boolean isFieldEqual(Field field) throws IllegalAccessException {
		boolean areEqual;
		field.setAccessible(true);
		Object value1 = field.get(object1);
		Object value2 = field.get(object2);
		if (value1 instanceof List && value2 instanceof List) {
			areEqual = areListsEqual((List<?>) value1, (List<?>) value2);
		} else {
			areEqual = areObjectsEqual(value1, value2);
		}
		return areEqual;
	}

	/**
	 * Returns true if the lists have the same content.
	 */
	private boolean areListsEqual(List<?> list1, List<?> list2) {
		boolean areEqual = true;
		if (list1.size() != list2.size()) {
			areEqual = false;
		} else {
			Iterator<?> it = list1.iterator();
			while (areEqual && it.hasNext()) {
				Object val1 = it.next();
				Object val2 = findEqual(val1, list2);
				areEqual = areObjectsEqual(val1, val2);
			}
		}
		return areEqual;
	}

	/**
	 * Find an object that is equal to the given object in the given list
	 */
	private Object findEqual(Object object, List<?> list) {
		Object result = null;
		Iterator<?> it = list.iterator();
		while (result == null && it.hasNext()) {
			Object candidate = it.next();
			if (candidate.equals(object)) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * Returns true if the given objects are equal.
	 */
	private boolean areObjectsEqual(Object object1, Object object2) {
		boolean areEqual = false;
		if (object1 == null && object2 == null) {
			areEqual = true;
		} else if (notNullAndSameClass(object1, object2)) {
			areEqual = tryAreObjectEqual(object1, object2);
		}
		return areEqual;
	}

	/**
	 * Returns true if the given objects are equal.
	 */
	private boolean tryAreObjectEqual(Object object1, Object object2) {
		boolean areEqual = false;
		if (isPrimitive(object1.getClass())) {
			areEqual = object1.equals(object2);
		} else {
			Comparator comparator = new Comparator(object1, object2);
			areEqual = comparator.areFieldsEqual();
		}
		return areEqual;
	}

	/**
	 * Returns true if the two objects are not NULL and have the same class.
	 */
	private boolean notNullAndSameClass(Object object1, Object object2) {
		return object1 != null && object2 != null
				&& object1.getClass().equals(object2.getClass());
	}

	/**
	 * Returns true if the given type is a primitive. In this comparator this
	 * includes also the wrappers for primitives and strings.
	 */
	private boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || type.equals(Boolean.class)
				|| type.equals(Byte.class) || type.equals(Character.class)
				|| type.equals(Short.class) || type.equals(Integer.class)
				|| type.equals(Long.class) || type.equals(Float.class)
				|| type.equals(Double.class) || type.equals(String.class);
	}
}
