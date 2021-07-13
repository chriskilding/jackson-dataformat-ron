package com.fasterxml.jackson.dataformat.ron.databind;

import com.fasterxml.jackson.dataformat.ron.PackageVersion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RONMapperTest {

    /**
     * Ensure that the RON PackageVersion metadata is passed through by the mapper.
     */
    @Test
    public void testPackageVersion() {
        final RONMapper mapper = new RONMapper();
        assertEquals(PackageVersion.VERSION, mapper.version());
    }
}
