package com.abc;


import java.util.Date;

public class Transaction {
    public final double amount;

    private Date transactionDate;

    public Transaction(double amount) {
        this.amount = amount;
        this.transactionDate = DateProvider.getInstance().now();
    }
    
    /*
     * Added method for adding transactions for specific date
     * 
     */
    
    public Transaction(double amount, Date date) {
        this.amount = amount;
        this.transactionDate = date;
    }
    
    public Date getTransDate() {
    	return transactionDate;
    }

}
