package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Member;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberNew {

    private Member member;

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
