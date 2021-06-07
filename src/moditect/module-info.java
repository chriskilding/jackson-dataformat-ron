// Generated using Moditect maven plugin
module com.fasterxml.jackson.dataformat.ron {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports com.fasterxml.jackson.dataformat.ron;

    provides com.fasterxml.jackson.core.JsonFactory with
        com.fasterxml.jackson.dataformat.ron.RONFactory;
}
