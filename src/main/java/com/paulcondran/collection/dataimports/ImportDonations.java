/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paulcondran.collection.dataimports;


import com.paulcondran.collection.components.CollectionUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.paulcondran.collection.data.CollectionDatabase;
import com.paulcondran.collection.model.data.CategoryDef;
import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.DonationCategory;
import com.paulcondran.collection.model.ui.OptionItem;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

/**
 *
 * @author tk
 */
public class ImportDonations {
    public String organisation;
        protected String getCellValueAsString(Cell cell) {
        String value = "";

        if (cell == null)
            return "";

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            if (cell.getBooleanCellValue())
                value = "true";
            else
                value = "false";
            break;

        case Cell.CELL_TYPE_NUMERIC:
            value = String.valueOf((int)cell.getNumericCellValue());
            break;

        case Cell.CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;

        case Cell.CELL_TYPE_FORMULA:
            value = cell.getStringCellValue();
            break;
        }
        return StringUtils.trimToEmpty(value);
    }
        protected BigDecimal getCellValueAsDecimal(Cell cell) {
        BigDecimal d = null;
        switch (cell.getCellType()) {

        case Cell.CELL_TYPE_NUMERIC:
        case Cell.CELL_TYPE_FORMULA:
             d = BigDecimal.valueOf(cell.getNumericCellValue());
            break;

        }
        return d;
    }

    protected Donation locateDonation(String RecNo)
    {
        CollectionDatabase database = CollectionDatabase.getInstance();
        EntityManager em = database.getEntityManager();
        
        Query q = em.createQuery("from Donation where receiptNo = '"+RecNo+"'");
        q.setMaxResults(1);

        List<Donation> list = q.getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);

    }
    protected boolean readRow(Row row) {
        boolean rowRead = false;
        List<OptionItem> cDefList = CollectionUtil.listCategories();

        int cellCount = row.getLastCellNum();

        Donation donation = null;
        List<DonationCategory> dcList = null;

        // rec no
        String val = getCellValueAsString(row.getCell(0));
        if (StringUtils.isEmpty(val))
        {
            return false;
        }
        donation = locateDonation(val);
        if (donation == null ) {
            donation = new Donation();
            donation.setReceiptNo(val);
            dcList = donation.getCategoryList();
        }
        else
        {
            dcList = donation.getCategoryList();
            dcList.clear();
        }
        // Date
        if (HSSFDateUtil.isCellDateFormatted(row.getCell(1))) {
            double dv = row.getCell(1).getNumericCellValue();
            java.util.Date date = HSSFDateUtil.getJavaDate(dv);
            java.sql.Date sqldate = new Date(date.getTime());

            donation.setDate(sqldate);
        }

        // organisation
        donation.setOrgChapter(StringUtils.deleteWhitespace(row.getCell(2).getStringCellValue()));
        // collector
        donation.setCollector(StringUtils.deleteWhitespace(row.getCell(3).getStringCellValue()));
        // memberId
        val = getCellValueAsString(row.getCell(4));
        donation.setMemberID(val);
        // DDref
        val = getCellValueAsString(row.getCell(5));
        donation.setDirectDebitRef(val);
        // title
        // Name
        val = getCellValueAsString(row.getCell(7));
        donation.setName(val);
        // total
        donation.setTotal(getCellValueAsDecimal(row.getCell(8)));
        // Cat 1
        // Cat 23
        StringBuffer sb = new StringBuffer();
        for (Integer i =9; i<cellCount; i++) {
            try {
                if (row.getCell(i)  == null ) {
                    continue;
                }
                int cellPos = row.getCell(i).getColumnIndex();
                if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING) {
                    sb.append(getCellValueAsString(row.getCell(i))+", ");
                }
                else if (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC){
                    BigDecimal d = getCellValueAsDecimal(row.getCell(i));
                    if (d.doubleValue() > 0) {
                        DonationCategory dc = new DonationCategory();
                        dc.setCategoryName(cDefList.get(cellPos-9).getCode());
                        dc.setAmount(d);
                        dcList.add(dc);
                        dc.setDonation(donation);
                        
                    }
                }
            } catch (Exception ex) {
                System.out.println(" Error trying to read category columnIDx="+ i);
                ex.printStackTrace();
            }
        }
        donation.setDetails(sb.toString());
        com.paulcondran.collection.data.CollectionDatabase database = CollectionDatabase.getInstance();
        database.persist(donation);
        donation = locateDonation(donation.getReceiptNo());
        return true;
    }

    public void processWorksheet(String filename) throws Exception {

        processWorksheet( new FileInputStream(filename));
    }
    public void processWorksheet(InputStream inputStream) throws Exception {

        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheetAt(0);

        int rowIndex = 1;
        while (true) {
            System.err.println("processing Row = " + rowIndex);
            Row row = sheet.getRow(rowIndex);

            if (row == null)
                break;
            else
                readRow(row);

            rowIndex++;
        }
        wb = null;
    }
}
