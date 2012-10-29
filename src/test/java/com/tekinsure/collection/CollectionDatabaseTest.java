package com.tekinsure.collection;

import com.tekinsure.thecollection.data.CollectionDatabase;
import com.tekinsure.thecollection.model.data.*;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigDecimal;
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
        donation.setReceiptNo("R-756");
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

        database.shutdown();
    }

    @Test
    public void categoryTableTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");

        Donation donation = new Donation();
        donation.setName("test");
        donation.setTotal(new BigDecimal(123.45));
        donation.setMemberID("Member1");
        donation.setReceiptNo("R-756");
        donation.setDate(new java.sql.Date(new java.util.Date().getTime()));
        
        List<DonationCategory> list = donation.getCategoryList();
        
        DonationCategory donationCategory = new DonationCategory();
        donationCategory.setAmount(new BigDecimal(100.00));
        donationCategory.setCategoryName("DonationCategory 1");
        list.add(donationCategory);
        
        EntityManager em = database.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(donation);
        em.flush();
        transaction.commit();

        Query q = em.createQuery("from Donation order by date desc");
        q.setMaxResults(1);

        List<Donation> resultlist = q.getResultList();
        donation = resultlist.get(0);
        List<DonationCategory> dcList = donation.getCategoryList();
        assert(dcList.get(0).getCategoryName().equals( "DonationCategory 1"));
        assert(dcList.size() == 1);

        database.shutdown();
    }


    @Test
    public void memberTableTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");

        Member member = new Member();
        member.setMemberID("M585-001");
        member.setName("Joe Member");
        member.setEmailAddress("joe@test.org");

        EntityManager em = database.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(member);
        em.flush();
        transaction.commit();

        Query q = em.createQuery("from Member where memberID = 'M585-001'");
        q.setMaxResults(1);

        List list = q.getResultList();
        assert(list.size() == 1);

        database.shutdown();
    }

    @Test
    public void userTableTest() {
        CollectionDatabase database = CollectionDatabase.getInstance();
        database.connectDatabase("hsqldb-test");

        User user = new User();
        user.setUserID("U321");
        user.setName("Mary User");
        user.setEmailAddress("mary@test.org");

        EntityManager em = database.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user);
        em.flush();
        transaction.commit();

        Query q = em.createQuery("from User where emailAddress = 'mary@test.org'");
        q.setMaxResults(1);

        List list = q.getResultList();
        assert(list.size() == 1);

        database.shutdown();
    }
}
