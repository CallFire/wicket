/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util;

import org.apache.wicket.util.string.PrependingStringBuffer;

/**
 * Encodes long values into the specified alphabet. Encoding is useful when long values need to be
 * represented in their string form and shorter values are preferred; by using alphabets of length
 * greater than ten shorter values can be obtained. For example, to encode values into their
 * hexadecimal representations the {@code 0123456789ABCDEF} can be used. Long values can be
 * shortened even further by using longer alphabets.
 * 
 * @author igor
 */
public class LongEncoder
{
	/**
	 * default alphabet that should be safe to use in most circumstances, while still providing good
	 * shortening of long values
	 */
	public static String DEFAULT = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private LongEncoder()
	{

	}

	/**
	 * Encodes the value into the default alphabet: {@value #DEFAULT}
	 * 
	 * @param value
	 * @return encoded value
	 */
	public static String encode(long value)
	{
		return encode(value, DEFAULT);
	}

	/**
	 * Decodes value using the default alphabet: {@value #DEFAULT}
	 * 
	 * @param value
	 * @return decoded value
	 */
	public static long decode(String value)
	{
		return decode(value, DEFAULT);
	}

	/**
	 * Encodes value into the specified alphabet
	 * 
	 * @param value
	 * @param alphabet
	 * @return encoded value
	 */
	public static String encode(long value, String alphabet)
	{
		final int len = alphabet.length();
		PrependingStringBuffer buff = new PrependingStringBuffer();
		do
		{
			int mod = (int)(value % len);
			buff.prepend(alphabet.charAt(mod));
			value = value / len;
		}
		while (value > 0);
		return buff.toString();
	}

	/**
	 * Decodes value using the specified alphabet
	 * 
	 * @param value
	 * @param alphabet
	 * @return decoded value
	 */
	public static long decode(String value, String alphabet)
	{
		final int factor = alphabet.length();
		long num = 0;
		for (int i = 0, len = value.length(); i < len; i++)
		{
			num = num * factor + alphabet.indexOf(value.charAt(i));
		}
		return num;
	}
}