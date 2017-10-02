package com.abc;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Account {

    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;

    private final int accountType;
    public List<Transaction> transactions;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }
    
    public void deposit(double amount, Date date) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
        	transactions.add(new Transaction(amount, date));
        }
    }

    public void withdraw(double amount) {
    	if (amount <= 0) {
    		throw new IllegalArgumentException("amount must be greater than zero");
    	} else {
    		transactions.add(new Transaction(-amount));
    	}
    }
    
    /*
     *  New method for withdraw with specific date
     */
    
    public void withdraw(double amount, Date date) {
    	if (amount <= 0) {
    		throw new IllegalArgumentException("amount must be greater than zero");
    	} else {
    		transactions.add(new Transaction(-amount, date));
    	}
    }
    
    /*
     *  Method for transferring from one account to another
     */
    
    public void send(Account account, double amount) {
    	if(amount <= 0) {
    		throw new IllegalArgumentException("amount must be greater than zero");
    	} else {
    		if(this.sumTransactions() > amount ) {
    			account.deposit(amount);
    			this.withdraw(amount);
    		}
    		else {
    			throw new IllegalArgumentException("not enought amount of money on account");
    		}
    	}
    }

    public double interestEarned() {
        double amount = sumTransactions();
        switch(accountType){
            case SAVINGS:
                if (amount <= 1000)
                    return amount * 0.001;
                else
                    return 1 + (amount-1000) * 0.002;
//            case SUPER_SAVINGS:
//                if (amount <= 4000)
//                    return 20;
            case MAXI_SAVINGS:
            	/*
            	 *  rework case for maxi-savings
            	 */
            	  if(!withdrawalsLast10Days())
            		  return amount * 0.05;
            	  else
            		  return amount * 0.001;
//                if (amount <= 1000)
//                    return amount * 0.02;
//                if (amount <= 2000)
//                    return 20 + (amount-1000) * 0.05;
//                return 70 + (amount-2000) * 0.1;
            default:
                return amount * 0.001;
        }
    }
    
    /*
     * New method to calculate interest
     */
    
    public double calculateInterestNew() {
    	double totalAmount = 0;
    	int lastWithdraw = 11;
    	for(int i = 0; i< transactions.size(); i++) {
    		Transaction t = transactions.get(i);
    		Date next;
    		if(i == transactions.size()-1)
    			next = Calendar.getInstance().getTime();
    		else 
    			next = transactions.get(i+1).getTransDate();
    		if(t.amount < 0) lastWithdraw = 0;
    		else lastWithdraw += 1;
//    		LocalDate nextLocal = next.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    		LocalDate tLocal = t.getTransDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    		long daysBefore = ChronoUnit.DAYS.between(nextLocal, tLocal);
    		long diff = t.getTransDate().getTime() - next.getTime();
    		long daysBefore = diff / 1000 / 60 / 60 / 24;
    		totalAmount += t.amount;
    		for(int j = 0; j< daysBefore; j++) {
    			totalAmount += 1;
    			switch(accountType){
            		case SAVINGS:
            			if (totalAmount <= 1000)
            				totalAmount += totalAmount *  (1 + (0.001 / 365));
            			else
            				totalAmount +=  1 + (totalAmount-1000) * (1 + (0.002 / 365));
            		case MAXI_SAVINGS:
            			if(lastWithdraw >= 10)
            				totalAmount += totalAmount * (1 + (0.05 / 365));
            			else
            				totalAmount += totalAmount * (1 + (0.001 / 365));
            		default:
            			totalAmount +=  totalAmount * (1 + (0.001 / 365));
    			}
    		}
    	}
    	
    	return totalAmount;
    }
    
    /*
     * method to test, if there was any transaction on the account past 10 days
     */
    
    
    public boolean withdrawalsLast10Days() {
    	for(Transaction t : transactions) {
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DATE, -10);
    		Date dateMax = cal.getTime();
    		if(t.getTransDate().after(dateMax) && t.amount < 0) {
    			return true;
    		}
    	}
    	return false;
    }

    public double sumTransactions() {
       return checkIfTransactionsExist(true);
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.amount;
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }

}
