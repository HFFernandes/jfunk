package com.mgmtp.jfunk.data;

import java.util.Map;

import com.mgmtp.jfunk.data.source.DataSource;

/**
 * Interface for a data set encapsulating entries for HTML forms. Data sets are provided by a
 * {@link DataSource}.
 * 
 * @version $Id$
 */
public interface DataSet {

	/**
	 * Checks if a key is present in the DataSet.
	 * 
	 * @param key
	 *            the key to check
	 * @return {@code true} if the specified key exists, {@code false} otherwise
	 */
	boolean containsKey(String key);

	/**
	 * Checks if a key is present in the DataSet.
	 * 
	 * @param key
	 *            the key to check
	 * @param index
	 *            the index
	 * @return {@code true} if the specified key exists, {@code false} otherwise
	 */
	boolean containsKey(String key, int index);

	/**
	 * Creates a copy of the DataSet.
	 * 
	 * @return the copy
	 */
	DataSet copy();

	/**
	 * Provides a copy of the DataSet which is read-only
	 * 
	 * @return the unmodifiable view of the DataSet
	 */
	Map<String, String> getDataView();

	/**
	 * Gets the value associated with the specified key.
	 * 
	 * @param key
	 *            the key
	 * @return the associated value. If an fixed value (see
	 *         {@link DataSet#setFixedValue(String, String)} has been specified for the specified
	 *         key, it is returned instead.
	 */
	String getValue(final String key);

	/**
	 * Gets the value associated with the specified key and index. The method is equivalent to
	 * calling {@code getValue(key + index)}.
	 * 
	 * @param key
	 *            the key
	 * @param index
	 *            the index
	 * @return the associated value. If a fixed value (see
	 *         {@link DataSet#setFixedValue(String, String)} has been specified for the specified
	 *         key, it is returned instead. A fixed value is not indexed, i. e. the same fixed value
	 *         will be returned for any index.
	 */
	String getValue(String key, int index);

	/**
	 * Convenience method for {@link #getValue(String)} which casts the result to a boolean
	 * primitive.
	 * 
	 * @see #getValue(String)
	 */
	boolean getValueAsBoolean(String key);

	/**
	 * Convenience method for {@link #getValue(String, int)} which casts the result to a boolean
	 * primitive.
	 * 
	 * @see #getValue(String,int)
	 */
	boolean getValueAsBoolean(String key, int index);

	/**
	 * Convenience method for {@link #getValue(String)} which casts the result to a Double object.
	 * 
	 * @see #getValue(String)
	 */
	Double getValueAsDouble(String key);

	/**
	 * Convenience method for {@link #getValue(String,int)} which casts the result to a Double
	 * object.
	 * 
	 * @see #getValue(String,int)
	 */
	Double getValueAsDouble(String key, int index);

	/**
	 * Convenience method for {@link #getValue(String)} which casts the result to a Integer object.
	 * 
	 * @see #getValue(String)
	 */
	Integer getValueAsInteger(String key);

	/**
	 * Convenience method for {@link #getValue(String,int)} which casts the result to a Integer
	 * object.
	 * 
	 * @see #getValue(String,int)
	 */
	Integer getValueAsInteger(String key, int index);

	/**
	 * Checks if a value is mapped to the specified key.
	 * 
	 * @param key
	 *            the key to check
	 * @return {@code true} if a value is mapped to the specified key, {@code false} otherwise
	 */
	boolean hasValue(String key);

	/**
	 * Checks if a value is mapped to the specified key.
	 * 
	 * @param key
	 *            the key to check
	 * @param index
	 *            the index
	 * @return {@code true} if a value is mapped to the specified key, {@code false} otherwise
	 */
	boolean hasValue(String key, int index);

	/**
	 * Removes a key from this DataSet. Calls to {@link #containsKey(String)} will return
	 * {@code false} afterwards.
	 * 
	 * @param key
	 *            the key to be deleted
	 * @return the previous value associated with key, or null if there was no mapping for the key
	 */
	String removeValue(final String key);

	/**
	 * Removes a key from this DataSet. Calls to {@link #containsKey(String, int)} will return
	 * {@code false} afterwards.
	 * 
	 * @param key
	 *            the key to be deleted
	 * @param index
	 *            the index
	 * @return the previous value associated with key, or null if there was no mapping for the key
	 */
	String removeValue(final String key, int index);

	/**
	 * Resets all values which were set by {@link #setFixedValue(String, String)}.
	 */
	void resetFixedValues();

	/**
	 * FIXME it is unclear why we need this - {@link #setValue(String, String)} should be sufficient
	 */
	void setFixedValue(final String key, final String value);

	/**
	 * Sets the value for the specified key and index.
	 * 
	 * @param key
	 *            the key to be set
	 * @param index
	 *            the index
	 * @param value
	 *            the value.
	 */
	void setValue(final String key, int index, final String value);

	/**
	 * Sets the value for the specified key.
	 * 
	 * @param key
	 *            the key to be set
	 * @param value
	 *            the value
	 */
	void setValue(final String key, final String value);
}