package com.fasterxml.jackson.dataformat.ron.generator;

import org.junit.Test;

import static org.junit.Assert.*;

public class RONWriteContextTest {

    @Test
    public void testChildArrayContext() {
        final RONWriteContext childArray = RONWriteContext.createRootContext().createChildArrayContext();

        assertTrue(childArray.inAnArray());
    }

    @Test
    public void testChildObjectContext() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();

        assertTrue(childObject.inAnObject());
    }

    @Test
    public void testChildStructContext() {
        final RONWriteContext childStruct = RONWriteContext.createRootContext().createChildStructContext();

        assertTrue(childStruct.inStruct());
    }

    @Test
    public void testChildEnumContext() {
        final RONWriteContext childEnum = RONWriteContext.createRootContext().createChildEnumContext();

        assertTrue(childEnum.inEnum());
    }

    @Test
    public void testChildTupleContext() {
        final RONWriteContext childTuple = RONWriteContext.createRootContext().createChildTupleContext();

        assertTrue(childTuple.inTuple());
    }

    @Test
    public void testWriteName() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();

        assertTrue(childObject.writeName("foo"));
    }

    @Test
    public void testWriteNameError() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();

        childObject.writeName("foo");
        assertFalse(childObject.writeName("foo"));
    }

    @Test
    public void testHasCurrentName() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();
        childObject.writeName("foo");
        assertTrue(childObject.hasCurrentName());
    }

    @Test
    public void testGetCurrentName() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();
        childObject.writeName("foo");
        assertEquals("foo", childObject.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();
        childObject.writeName("foo");
        assertTrue(childObject.writeValue());
    }

    @Test
    public void testWriteValueError() {
        final RONWriteContext childObject = RONWriteContext.createRootContext().createChildObjectContext();
        assertFalse(childObject.writeValue());
    }

    @Test
    public void testGetParent() {
        final RONWriteContext root = RONWriteContext.createRootContext();
        final RONWriteContext childObject = root.createChildObjectContext();
        assertEquals(root, childObject.getParent());
    }
}
