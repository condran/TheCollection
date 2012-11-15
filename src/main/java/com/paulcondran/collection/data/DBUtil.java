package com.paulcondran.collection.data;

import com.paulcondran.collection.components.CollectionUtil;
import com.paulcondran.collection.model.data.Member;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class DBUtil {

    /**
     * Performs a standard member search across name, family name, suburb and member id
     * @param search
     * @return
     */
    public static List<String> searchMembers(String search) {
        List<String> results = new ArrayList<String>();

        List<Member> members = performMemberQuery(search);
        if (members != null) {
            for (Member member : members) {
                StringBuilder result = new StringBuilder();

                CollectionUtil.appendIfNotBlank(result, member.getName(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getFamilyName(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getSuburb(), "%s-");
                CollectionUtil.appendIfNotBlank(result, member.getMemberID(), "[%s]");

                results.add(result.toString());
            }
        }

        return results;
    }

    /**
     * Queries the Member database to fill out the member details
     */
    public static List<Member> performMemberQuery(String search) {

        try {


            CollectionDatabase db = CollectionDatabase.getInstance();
            String query = "from Member where lower(memberID)='"+search.toLowerCase()+"' or lower(name) like '%"+
                    search.toLowerCase()+"%'";
            Query q = db.getEntityManager().createQuery(query);

            return q.getResultList();

        } catch (Exception ee)
        {
            ee.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the exact member based on type ahead query
     * @param memberSearch
     * @return
     */
    public static Member findMember(String memberSearch) {

        if (memberSearch == null) { return null;}
        String memberID = memberSearch.replace("[","").replace("]",""); //StringUtils.substringBetween(memberSearch, "[", "]");
        if (memberID == null || StringUtils.isEmpty(memberID)) { return null; }
        CollectionDatabase db = CollectionDatabase.getInstance();
        Query q = db.getEntityManager().createQuery("from Member where memberID='"+memberID+"'");

        if (q.getResultList().isEmpty()) {
            return null;
        }
        return (Member) q.getResultList().get(0);
    }
}
