package org.earthster.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * A class for generating domain object graphs with random values.
 */
public class Generator {

	/**
	 * Creates a list of n instances of the given clazz.
	 */
	public static <T> List<T> create(Class<T> clazz, int n) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < n; i++) {
			T instance = create(clazz);
			list.add(instance);
		}
		return list;
	}

	/**
	 * Create a new instance of the given class.
	 */
	public static <T> T create(Class<T> clazz) {
		T instance = null;
		try {
			instance = tryCreateInstance(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	/**
	 * Try to create a new instance.
	 */
	private static <T> T tryCreateInstance(Class<T> clazz) throws Exception {
		T instance;
		instance = clazz.newInstance();
		generateAndSetFieldValues(instance, clazz.getFields());
		generateAndSetFieldValues(instance, clazz.getDeclaredFields());
		return instance;
	}

	/**
	 * Set generated field values to the created instance.
	 */
	@SuppressWarnings("unchecked")
	private static <T> void generateAndSetFieldValues(T instance, Field[] fields)
			throws Exception {
		for (Field field : fields) {
			field.setAccessible(true);
			Object currentValue = field.get(instance);
			if (currentValue instanceof List) {
				List<Object> list = (List<Object>) currentValue;
				generateListValues(field, list);
			} else if (!Modifier.isFinal(field.getModifiers())) {
				Object value = generateValue(field.getType());
				field.set(instance, value);
			}
		}
	}

	/**
	 * Generates a random number of list values.
	 */
	private static void generateListValues(Field field, List<Object> list) {
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) field
					.getGenericType();
			Type[] actualTypeArgs = paramType.getActualTypeArguments();
			if (actualTypeArgs != null && actualTypeArgs.length > 0
					&& actualTypeArgs[0] instanceof Class) {
				int n = new Random().nextInt(10) + 1;
				for (int i = 0; i < n; i++) {
					Object val = generateValue((Class<?>) actualTypeArgs[0]);
					list.add(val);
				}
			}
		}
	}

	/**
	 * Generate a value for the given type.
	 */
	private static Object generateValue(Class<?> type) {

		Object value = null;
		Random rand = new Random();

		if (type.equals(String.class)) {
			value = UUID.randomUUID().toString();

		} else if (type.equals(Double.class) || type.equals(double.class)) {
			value = rand.nextDouble();

		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			value = rand.nextInt();

		} else if (type.equals(Float.class) || type.equals(float.class)) {
			value = rand.nextFloat();

		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			value = rand.nextBoolean();

		} else if (type.equals(Long.class) || type.equals(long.class)) {
			value = rand.nextLong();

		} else {
			value = create(type);

		}
		return value;
	}

}
