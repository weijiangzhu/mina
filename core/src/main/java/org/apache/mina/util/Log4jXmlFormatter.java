/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.util;

import org.slf4j.MDC;
import org.slf4j.helpers.BasicMDCAdapter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Set;
import java.util.Arrays;

/**
 * Implementation of {@link java.util.logging.Formatter} that generates xml in the log4j format.
 * <p>
 * The generated xml corresponds 100% with what is generated by
 * log4j's <a href=http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/XMLLayout.html">XMLLayout</a>
 * <p>
 * The MDC properties will only be correct when <code>format</code> is called from the same thread
 * that generated the LogRecord.
 * <p>
 * The file and line attributes in the locationInfo element will always be "?"
 * since java.util.logging.LogRecord does not provide that info.
 * <p>
 * The implementation is heavily based on org.apache.log4j.xml.XMLLayout
 * </p>
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev$, $Date$
 */
public class Log4jXmlFormatter extends Formatter {

    private final int DEFAULT_SIZE = 256;
    private final int UPPER_LIMIT = 2048;

    private StringBuffer buf = new StringBuffer(DEFAULT_SIZE);
    private boolean locationInfo = false;
    private boolean properties = false;

    /**
     * The <b>LocationInfo</b> option takes a boolean value. By default,
     * it is set to false which means there will be no location
     * information output by this layout. If the the option is set to
     * true, then the file name and line number of the statement at the
     * origin of the log statement will be output.
     *
     * @param flag whether locationInfo should be output by this layout
     */
    public void setLocationInfo(boolean flag) {
        locationInfo = flag;
    }

    /**
     * Returns the current value of the <b>LocationInfo</b> option.
     *
     * @return whether locationInfo will be output by this layout
     */
    public boolean getLocationInfo() {
        return locationInfo;
    }

    /**
     * Sets whether MDC key-value pairs should be output, default false.
     *
     * @param flag new value.
     */
    public void setProperties(final boolean flag) {
        properties = flag;
    }

    /**
     * Gets whether MDC key-value pairs should be output.
     *
     * @return true if MDC key-value pairs are output.
     */
    public boolean getProperties() {
        return properties;
    }

    @SuppressWarnings("unchecked")
    public String format(final LogRecord record) {
        // Reset working buffer. If the buffer is too large, then we need a new
        // one in order to avoid the penalty of creating a large array.
        if (buf.capacity() > UPPER_LIMIT) {
            buf = new StringBuffer(DEFAULT_SIZE);
        } else {
            buf.setLength(0);
        }
        buf.append("<log4j:event logger=\"");
        buf.append(Transform.escapeTags(record.getLoggerName()));
        buf.append("\" timestamp=\"");
        buf.append(record.getMillis());
        buf.append("\" level=\"");

        buf.append(Transform.escapeTags(record.getLevel().getName()));
        buf.append("\" thread=\"");
        buf.append(String.valueOf(record.getThreadID()));
        buf.append("\">\r\n");

        buf.append("<log4j:message><![CDATA[");
        // Append the rendered message. Also make sure to escape any
        // existing CDATA sections.
        Transform.appendEscapingCDATA(buf, record.getMessage());
        buf.append("]]></log4j:message>\r\n");

        if (record.getThrown() != null) {
            String[] s = Transform.getThrowableStrRep(record.getThrown());
            if (s != null) {
                buf.append("<log4j:throwable><![CDATA[");
                for (String value : s) {
                    Transform.appendEscapingCDATA(buf, value);
                    buf.append("\r\n");
                }
                buf.append("]]></log4j:throwable>\r\n");
            }
        }

        if (locationInfo) {
            buf.append("<log4j:locationInfo class=\"");
            buf.append(Transform.escapeTags(record.getSourceClassName()));
            buf.append("\" method=\"");
            buf.append(Transform.escapeTags(record.getSourceMethodName()));
            buf.append("\" file=\"?\" line=\"?\"/>\r\n");
        }

        if (properties) {
            if (MDC.getMDCAdapter() instanceof BasicMDCAdapter) {
                BasicMDCAdapter mdcAdapter = (BasicMDCAdapter) MDC.getMDCAdapter();
                Set keySet = mdcAdapter.getKeys();
                if (keySet != null && keySet.size() > 0) {
                    buf.append("<log4j:properties>\r\n");
                    Object[] keys = keySet.toArray();
                    Arrays.sort(keys);
                    for (Object key1 : keys) {
                        String key = key1.toString();
                        Object val = mdcAdapter.get(key);
                        if (val != null) {
                            buf.append("<log4j:data name=\"");
                            buf.append(Transform.escapeTags(key));
                            buf.append("\" value=\"");
                            buf.append(Transform.escapeTags(String.valueOf(val)));
                            buf.append("\"/>\r\n");
                        }
                    }
                    buf.append("</log4j:properties>\r\n");
                }
            }
        }
        buf.append("</log4j:event>\r\n\r\n");

        return buf.toString();
    }

}
