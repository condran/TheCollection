package com.paulcondran.collection.data;

import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Streaming resource for exporting data to CSV
 */
public abstract class CSVResourceStream extends AbstractResourceStreamWriter {

    private CellProcessor[] processors;
    private String[] header;

    /**
     *
     *
     * @param processors
     * @param header
     * @param resultsList
     */
    public CSVResourceStream() {
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    /**
     *
     * @param outputStream
     * @throws IOException
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {

        ICsvListWriter listWriter = null;
        try {
            listWriter = new CsvListWriter(new OutputStreamWriter(outputStream),
                    CsvPreference.STANDARD_PREFERENCE);

            // write the header
            listWriter.writeHeader(getHeaders());

            // write the beans
            for( List<Object> results : getObjectsList() ) {
                listWriter.write(results, getCellProcessors());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if( listWriter != null ) {
                listWriter.close();
            }
        }
    }

    public abstract List<List<Object>> getObjectsList();

    public abstract CellProcessor[] getCellProcessors();

    public abstract String[] getHeaders();
}
