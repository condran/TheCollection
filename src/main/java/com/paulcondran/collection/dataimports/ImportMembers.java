/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paulcondran.collection.dataimports;


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
import com.paulcondran.collection.model.data.Member;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author tk
 */
public class ImportMembers {
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
        return StringUtils.stripToEmpty(value);
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

    protected Member locateMember(String mID)
    {
        CollectionDatabase database = CollectionDatabase.getInstance();
        EntityManager em = database.getEntityManager();
        
        Query q = em.createQuery("from Member where memberID = '"+mID+"'");
        q.setMaxResults(1);

        List<Member> list = q.getResultList();
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);

    }

    protected boolean readRow(Row row) {
        boolean rowRead = false;

        int cellCount = row.getPhysicalNumberOfCells();

        Member member = null;

        // rec no
        String val = null;
        // val = getCellValueAsString(val);
        if (row.getCell(0) == null ||
                StringUtils.isEmpty(row.getCell(0).getStringCellValue()) ||
                cellCount <13)
        {
            return false;
        }

        val = getCellValueAsString(row.getCell(2));
        member = locateMember(val);
        if (member == null ) {
            member = new Member();
            member.setMemberID(val);
        }
        // Org
        val = getCellValueAsString(row.getCell(0));
        member.setOrganisation(val);

        //    Family Code:	
        val = getCellValueAsString(row.getCell(1));
        member.setFamilyName(val);

//    DDRef
        val = getCellValueAsString(row.getCell(3));
        member.setDirectDebitRef(val);
        
//    Member Name
        val = getCellValueAsString(row.getCell(4));
        member.setName(val);
        
//    Street Address
        val = getCellValueAsString(row.getCell(5));
        member.setAddress1(val);
        
//    Suburb
        val = getCellValueAsString(row.getCell(6));
        member.setSuburb(val);

//    Postcode
        val = getCellValueAsString(row.getCell(7));
        member.setPostcode(val);
        
//    State	
        val = getCellValueAsString(row.getCell(8));
        member.setState(val);

//    Telephone	
        
        val = getCellValueAsString(row.getCell(9));
        member.setContactNo(val);

//    Mobile	
        if (row.getCell(9).getCellType() == Cell.CELL_TYPE_NUMERIC) {
            int mno = (int) row.getCell(10).getNumericCellValue();
             member.setMobileNo(mno+"");
        }
        else
        {
            val = getCellValueAsString(row.getCell(10));
            member.setMobileNo(val);
        }
//    Email Address
        val = getCellValueAsString(row.getCell(11));
        member.setEmailAddress(val);

// Year Of Birth
        if (row.getCell(12).getCellType() == Cell.CELL_TYPE_NUMERIC) {
            int mno = (int) row.getCell(12).getNumericCellValue();
             member.setYearOfBirth(mno);
        }
        else
        {
            val = getCellValueAsString(row.getCell(12));
            member.setYearOfBirth(Integer.valueOf(val));
        }

        com.paulcondran.collection.data.CollectionDatabase database = CollectionDatabase.getInstance();
        database.persist(member);
        return true;
    }

    public void processWorksheet(String filename) throws Exception {

        processWorksheet( new FileInputStream(filename));
    }
    public void processWorksheet(InputStream inputStream) throws Exception {

        Workbook wb = WorkbookFactory.create(inputStream);
        Sheet sheet = wb.getSheetAt(0);

        int rowIndex = 0;
        while (true) {
//            output("processing Row = " + rowIndex);
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
