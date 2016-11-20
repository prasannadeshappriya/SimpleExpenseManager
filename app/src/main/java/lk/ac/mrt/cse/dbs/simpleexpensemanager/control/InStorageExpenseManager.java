package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InStorageAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InStorageTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


/**
 * Created by Prasanna Deshappriya on 11/20/2016.
 */
public class InStorageExpenseManager extends ExpenseManager {
    private Context context;
    private String command;     //command for sql
    public InStorageExpenseManager(Context context){
        //Initialize
        this.context = context;
        setup();
    }
    @Override
    public void setup(){
        //Open the database, if not exist, create new database
        SQLiteDatabase sqldb = context.openOrCreateDatabase("140062D", context.MODE_PRIVATE, null);

        //if database doesn't exist, we'll have to create all tables first
        command = "CREATE TABLE account_data(" +
                "account_no VARCHAR PRIMARY KEY," +
                "bank VARCHAR," +
                "holder_name VARCHAR," +
                "init_amount REAL" +
                " );";
        sqldb.execSQL(command);

        command = "CREATE TABLE transaction_data(" +
                "transaction_id INTEGER PRIMARY KEY," +
                "account_no VARCHAR," +
                "type INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (account_no) REFERENCES account_data(account_no)" +
                ");";
        sqldb.execSQL(command);

        InStorageAccountDAO accountDAO = new InStorageAccountDAO(sqldb);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(new InStorageTransactionDAO(sqldb));
    }
}
