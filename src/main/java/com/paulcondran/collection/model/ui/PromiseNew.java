package com.paulcondran.collection.model.ui;

import com.paulcondran.collection.model.data.Donation;
import com.paulcondran.collection.model.data.Member;
import com.paulcondran.collection.model.data.Promise;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 22/10/12
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromiseNew {

    private String memberSearch;

    private Promise promise;

    private Member member;

    public String getMemberSearch() {
        return memberSearch;
    }

    public void setMemberSearch(String memberSearch) {
        this.memberSearch = memberSearch;
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

    /**
     * @return the promise
     */
    public Promise getPromise() {
        if (promise == null) {
            promise = new Promise();
        }
        return promise;
    }

    /**
     * @param promise the promise to set
     */
    public void setPromise(Promise promise) {
        this.promise = promise;
    }
}
