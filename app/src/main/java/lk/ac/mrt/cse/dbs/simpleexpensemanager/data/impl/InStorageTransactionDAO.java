package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Prasanna Deshappriya on 11/20/2016.
 */
public class InStorageTransactionDAO implements TransactionDAO{
    private SQLiteDatabase sqldb;
    private String command;

    public InStorageTransactionDAO(SQLiteDatabase sqldb){
        this.sqldb = sqldb;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        command = "INSERT INTO transaction_data (account_no,type,amount,date) VALUES (?,?,?,?)";
        SQLiteStatement statement = sqldb.compileStatement(command);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        command="SELECT * FROM transaction_data";
        Cursor c = sqldb.rawQuery(command,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(c.moveToFirst()) {
            do{
                Transaction t = new Transaction(
                        new Date(c.getLong(c.getColumnIndex("date"))),
                        c.getString(c.getColumnIndex("account_no")),
                        (c.getInt(c.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        c.getDouble(c.getColumnIndex("amount")));
                transactions.add(t);
            }while (c.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        command="SELECT * FROM transaction_data LIMIT " + limit;
        Cursor c = sqldb.rawQuery(command,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(c.moveToFirst()) {
            do {
                Transaction t = new Transaction(
                        new Date(c.getLong(c.getColumnIndex("date"))),
                        c.getString(c.getColumnIndex("account_no")),
                        (c.getInt(c.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        c.getDouble(c.getColumnIndex("amount")));
                transactions.add(t);
            } while (c.moveToNext());
        }
        return transactions;
    }
}
