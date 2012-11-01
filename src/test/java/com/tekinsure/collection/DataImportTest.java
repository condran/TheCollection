package com.tekinsure.collection;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.dataimports.ImportDonations;
import com.tekinsure.thecollection.dataimports.ImportMembers;
import com.tekinsure.thecollection.model.data.*;
import java.io.File;
import java.io.FileInputStream;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Class to test the database routines.
 */
public class DataImportTest {

    @Test
    public void donationTableImportTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");
        
//        File directory = new File (".");
// try {
// System.out.println ("Current directory's canonical path: " 
//  + directory.getCanonicalPath()); 
//   System.out.println ("Current directory's absolute  path: " 
//  + directory.getAbsolutePath());
// }catch(Exception e) {
// System.out.println("Exceptione is ="+e.getMessage());
//  }

        
        ImportDonations di = new ImportDonations();
        di.organisation = "Org 1";
        try         {
            di.processWorksheet("src/test/resources/test donation data.xlsx");
            
            
        } catch (Exception ee)
        {
            ee.printStackTrace();
        }
        EntityManager em = database.getEntityManager();
        Query q = em.createQuery("from Donation order by date desc");
         q.setMaxResults(1);

        List list = q.getResultList();
        
        assert(list.size() == 1);

        database.shutdown();
    }

    @Test
    public void memberTableImportTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");
        
//        File directory = new File (".");
// try {
// System.out.println ("Current directory's canonical path: " 
//  + directory.getCanonicalPath()); 
//   System.out.println ("Current directory's absolute  path: " 
//  + directory.getAbsolutePath());
// }catch(Exception e) {
// System.out.println("Exceptione is ="+e.getMessage());
//  }

        
        ImportMembers di = new ImportMembers();
        try         {
            di.processWorksheet("src/test/resources/member List sample.xlsx");
            
            
        } catch (Exception ee)
        {
            ee.printStackTrace();
        }
        EntityManager em = database.getEntityManager();
        Query q = em.createQuery("from Members");
         q.setMaxResults(1);

        List list = q.getResultList();
        
        assert(list.size() == 1);

        database.shutdown();
    }
}
