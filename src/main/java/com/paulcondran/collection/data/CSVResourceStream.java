package com.paulcondran.collection.data;

import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Streaming resource for exporting data to CSV
 */
public class CSVResourceStream<T> extends AbstractResourceStreamWriter {

    private CellProcessor[] processors;
    private String[] header;
    private List<T> resultsList;

    /**
     *
     *
     * @param processors
     * @param header
     * @param resultsList
     */
    public CSVResourceStream(CellProcessor[] processors, String[] header, List<T> resultsList) {
        this.processors = processors;
        this.header = header;
        this.resultsList = resultsList;
    }


    /**
     *
     * @param outputStream
     * @throws IOException
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {

        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new OutputStreamWriter(outputStream),
                    CsvPreference.STANDARD_PREFERENCE);

            // write the header
            beanWriter.writeHeader(header);

            // write the beans
            for( T result : resultsList ) {
                beanWriter.write(result, header, processors);
            }

        }
        finally {
            if( beanWriter != null ) {
                beanWriter.close();
            }
        }
    }

    public List<T> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<T> resultsList) {
        this.resultsList = resultsList;
    }
}
