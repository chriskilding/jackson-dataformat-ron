package com.fasterxml.jackson.dataformat.ron;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.*;
import java.net.URL;

/**
 * Factory used for constructing {@link RONParser} and {@link RONGenerator}
 * instances; both of which handle
 * <a href="https://github.com/ron-rs/ron">RON</a>
 * encoded data.
 */
public class RONFactory extends JsonFactory
{
	private static final long serialVersionUID = 1; // 2.6

}
