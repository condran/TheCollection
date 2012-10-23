package com.tekinsure.collection;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.Donation;
import com.tekinsure.thecollection.pages.DonationSearchPage;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Class to test the database routines.
 */
public class CollectionDatabaseTest {

    @Test
    public void donationTableTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");

        Donation donation = new Donation();
        donation.setName("test");
        donation.setTotal(new BigDecimal(123.45));
        donation.setMemberID("Member1");
        donation.setDate(new java.sql.Date(new java.util.Date().getTime()));

        EntityManager em = database.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(donation);
        em.flush();
        transaction.commit();

        Query q = em.createQuery("from Donation order by date desc");
        q.setMaxResults(1);

        List list = q.getResultList();
        assert(list.size() == 1);

    }
}
