package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.IOException;
import java.io.Writer;

/**
 * Factory used for constructing {@link RONParser} and {@link RONGenerator}
 * instances; both of which handle
 * <a href="https://github.com/ron-rs/ron">RON</a>
 * encoded data.
 */
public class RONFactory extends JsonFactory
{
	private static final long serialVersionUID = 1; // 2.6

	@Override
	protected RONGenerator _createGenerator(Writer out, IOContext ctxt) {
		return new RONGenerator(_generatorFeatures, _objectCodec, out, ctxt);
	}

	/**
	 * Specialise the return type of the generator, so the user does not need to downcast it.
	 */
	@Override
	public RONGenerator createGenerator(Writer w) throws IOException {
		IOContext ctxt = _createContext(w, false);
		return this._createGenerator(_decorate(w, ctxt), ctxt);
	}
}
