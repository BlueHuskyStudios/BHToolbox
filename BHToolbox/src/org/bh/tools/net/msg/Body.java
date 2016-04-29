package org.bh.tools.net.msg;

/**
 * Copyright BHStudios ©2016 BH-1-PS <hr>
 *
 * Created by Kyli Rouge on 2016-01-31.
 */
public class Body<DataType> implements CharSequence {
	protected DataType     _content;
	protected CharSequence _cachedStringVersion;

	public Body(DataType content) {
		_content = content;
	}

	public DataType getContent() {
		return _content;
	}

	public void setContent(DataType content) {
		_content = content;
		_cachedStringVersion = null;
	}

	private CharSequence getCachedStringVersion() {
		return _cachedStringVersion == null
		       ? (_content == null
		          ? ""
		          : String.valueOf(_content))
		       : _cachedStringVersion;
	}

	@Override public int length() {
		return getCachedStringVersion().length();
	}

	@Override public char charAt(int index) {
		return _content == null ? 0 : getCachedStringVersion().charAt(index);
	}

	@Override public CharSequence subSequence(int start, int end) {
		return _content == null ? null : getCachedStringVersion().subSequence(start, end);
	}
}
