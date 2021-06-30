package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.dataformat.ron.generator.RONGenerator;
import com.fasterxml.jackson.dataformat.ron.parser.RONParser;

import java.io.*;

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

	@Override
	protected RONParser _createParser(Reader r, IOContext ctxt) {
		return new RONParser(ctxt, _parserFeatures, r, _objectCodec, _rootCharSymbols.makeChild(_factoryFeatures));
	}

	@Override
	protected RONParser _createParser(char[] data, int offset, int len, IOContext ctxt,
									   boolean recyclable) {
		return new RONParser(ctxt, _parserFeatures, null, _objectCodec,
				_rootCharSymbols.makeChild(_factoryFeatures),
				data, offset, offset+len, recyclable);
	}

	@Override
	protected RONParser _createParser(DataInput input, IOContext ctxt) {
		throw new UnsupportedOperationException("Operation not supported by factory of type "+getClass().getName());
	}

	/**
	 * Enforce the use of the Reader API.
	 */
	@Override
	public boolean canUseCharArrays() {
		return false;
	}

	/**
	 * Specialise the return type of the generator, so the user does not need to downcast it.
	 */
	@Override
	public RONGenerator createGenerator(Writer w) throws IOException {
		IOContext ctxt = _createContext(w, false);
		return this._createGenerator(_decorate(w, ctxt), ctxt);
	}

	/**
	 * Specialise the return type of the parser, so the user does not need to downcast it.
	 */
	@Override
	public RONParser createParser(Reader r) throws IOException {
		IOContext ctxt = _createContext(r, false);
		return this._createParser(_decorate(r, ctxt), ctxt);
	}
}
