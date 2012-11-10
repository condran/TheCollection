package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.Member;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DonationNew {

    private String memberSearch;

    private Donation donation;

    private Member member;

    public String getMemberSearch() {
        return memberSearch;
    }

    public void setMemberSearch(String memberSearch) {
        this.memberSearch = memberSearch;
    }

    public Donation getDonation() {
        if (donation == null) {
            donation = new Donation();
        }
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public Member getMember() {
        if (member == null) {
            member = new Member();
        }
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
