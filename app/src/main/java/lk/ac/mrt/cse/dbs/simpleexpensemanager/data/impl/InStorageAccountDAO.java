package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Prasanna Deshappriya on 11/20/2016.
 */
public class InStorageAccountDAO implements AccountDAO {
    private SQLiteDatabase sqldb;
    private String command;

    public InStorageAccountDAO(SQLiteDatabase sqldb){
        this.sqldb=sqldb;
    }
    @Override
    public List<String> getAccountNumbersList() {
        command = "SELECT account_no FROM account_data";
        Cursor c = sqldb.rawQuery(command,null);
        //list to store data
        List<String> accounts = new ArrayList<String>();
        if(c.moveToFirst()) {
            do {
                accounts.add(c.getString(c.getColumnIndex("account_no")));
            } while (c.moveToNext());
        }
        //Return
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        command = "SELECT * FROM account_data";
        Cursor c = sqldb.rawQuery(command,null);
        List<Account> accounts = new ArrayList<Account>();
        if(c.moveToFirst()) {
            do {
                Account account = new Account(
                        c.getString(c.getColumnIndex("account_no")),
                        c.getString(c.getColumnIndex("bank")),
                        c.getString(c.getColumnIndex("holder_name")),
                        c.getDouble(c.getColumnIndex("init_amount")));
                accounts.add(account);
            } while (c.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        command = "SELECT * FROM account_data WHERE account_no = " + accountNo;
        Cursor c = sqldb.rawQuery(command,null);
        Account account = null;
        if(c.moveToFirst()) {
            do {
                account = new Account(
                        c.getString(c.getColumnIndex("account_no")),
                        c.getString(c.getColumnIndex("bank")),
                        c.getString(c.getColumnIndex("holder_name")),
                        c.getDouble(c.getColumnIndex("init_amount")));
            } while (c.moveToNext());
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        //use prepared statements for insert
        command = "INSERT INTO account_data (account_no,bank,holder_name,init_amount) VALUES (?,?,?,?)";
        SQLiteStatement statement = sqldb.compileStatement(command);

        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        command = "DELETE FROM account_data WHERE account_no = ?";
        SQLiteStatement statement = sqldb.compileStatement(command);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        command = "UPDATE account_data SET init_amount = init_amount + ?";
        SQLiteStatement statement = sqldb.compileStatement(command);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }
}
