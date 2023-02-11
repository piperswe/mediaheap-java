package net.mediaheap.database;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class SingleConnectionDataSource implements DataSource {
    private final UncloseableConnection connection;
    private PrintWriter logWriter = null;
    private int loginTimeout = 0;
    public SingleConnectionDataSource(Connection connection) {
        this.connection = new UncloseableConnection(connection);
    }

    @Override
    public UncloseableConnection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public UncloseableConnection getConnection(String username, String password) throws SQLException {
        return connection;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        logWriter = out;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    private static class UncloseableConnection implements Connection {
        private final Connection c;

        UncloseableConnection(Connection c) {
            this.c = c;
        }

        @Override
        public Statement createStatement() throws SQLException {
            return c.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return c.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return c.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return c.nativeSQL(sql);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return c.getAutoCommit();
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            c.setAutoCommit(autoCommit);
        }

        @Override
        public void commit() throws SQLException {
            c.commit();
        }

        @Override
        public void rollback() throws SQLException {
            c.rollback();
        }

        @Override
        public void close() throws SQLException {
            // NOOP
        }

        @Override
        public boolean isClosed() throws SQLException {
            return c.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return c.getMetaData();
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return c.isReadOnly();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            c.setReadOnly(readOnly);
        }

        @Override
        public String getCatalog() throws SQLException {
            return c.getCatalog();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            c.setCatalog(catalog);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return c.getTransactionIsolation();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            c.setTransactionIsolation(level);
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return c.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            c.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return c.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return c.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return c.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return c.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            c.setTypeMap(map);
        }

        @Override
        public int getHoldability() throws SQLException {
            return c.getHoldability();
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            c.setHoldability(holdability);
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return c.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return c.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            c.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            c.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return c.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return c.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return c.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return c.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return c.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return c.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return c.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return c.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return c.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return c.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return c.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            c.setClientInfo(name, value);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return c.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return c.getClientInfo();
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            c.setClientInfo(properties);
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return c.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return c.createStruct(typeName, attributes);
        }

        @Override
        public String getSchema() throws SQLException {
            return c.getSchema();
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            c.setSchema(schema);
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            c.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            c.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return c.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            if (iface.isInstance(c)) {
                //noinspection unchecked
                return (T) c;
            } else {
                return c.unwrap(iface);
            }
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return iface.isInstance(c) || c.isWrapperFor(iface);
        }
    }
}
