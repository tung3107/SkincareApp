package btl.skincareapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import btl.skincareapp.model.Product;
import btl.skincareapp.model.MyProduct;
import btl.skincareapp.model.Routine;
import btl.skincareapp.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // MYProduct
    public static final String MYPRODUCT = "MYPRODUCT";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_BRAND = "BRAND_NAME";
    public static final String COLUMN_Date_hethan = "EXPIRE_DATE";
    public static final String COLUMN_Date_mua = "BROUGHT_DATE";
    public static final String COLUMN_gia = "PRICE";
    public static final String COLUMN_anhurl = "IMAGE_URL";
    private static final String COLUMN_MY_PRODUCT_USER_ID = "user_id";
    
    /// User
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_SKIN_TYPE = "skin_type";
    Map<String, Object> combineMap = new HashMap<>();

    /// product
    private static final String TABLE_PRODUCT = "Product";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_SKIN_TYPE = "skin_type";
    private static final String COLUMN_PRODUCT_IMAGE_LINK = "image_link";
    private static final String COLUMN_PRODUCT_BRAND = "brand";

    // Routine
    private static final String TABLE_ROUTINE = "Routine";
    private static final String COLUMN_ROUTINE_ID = "id";
    private static final String COLUMN_ROUTINE_TIME = "time"; // Morning/Night
    private static final String COLUMN_ROUTINE_NAME = "name";
    private static final String COLUMN_ROUTINE_USER_ID = "user_id";

    private String createRoutineProductTable = "CREATE TABLE RoutineProduct (" +
            "routine_id "+  " INTEGER NOT NULL, " +     "product_id INTEGER NOT NULL, " +
            "PRIMARY KEY (routine_id, product_id), " +              "FOREIGN KEY (routine_id) REFERENCES Routine(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (product_id) REFERENCES MYPRODUCT(ID) ON DELETE CASCADE)";

    private String createTableStatementMyProduct = "CREATE TABLE " + MYPRODUCT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    COLUMN_NAME + " TEXT," + COLUMN_BRAND + " TEXT," + COLUMN_gia + " TEXT," + COLUMN_anhurl + " TEXT," + COLUMN_Date_hethan + " DATETIME," + COLUMN_Date_mua + " DATETIME," +
    COLUMN_MY_PRODUCT_USER_ID + " INTEGER, " +
    "FOREIGN KEY (" + COLUMN_MY_PRODUCT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private String createTableStatementuser = "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE," + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT NOT NULL," + COLUMN_USER_SKIN_TYPE + " TEXT" 
                    +")";

    private String createProductTable = "CREATE TABLE " + TABLE_PRODUCT + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_SKIN_TYPE + " TEXT, " +
                    COLUMN_PRODUCT_IMAGE_LINK + " TEXT, " +
                    COLUMN_PRODUCT_BRAND + " TEXT)";
    

    private String createRoutineTable = "CREATE TABLE " + TABLE_ROUTINE + " (" +
                    COLUMN_ROUTINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROUTINE_TIME + " TEXT NOT NULL, " + COLUMN_ROUTINE_NAME + " TEXT, "  +
                    COLUMN_ROUTINE_USER_ID + " INTEGER, " +
                    "FOREIGN KEY (" + COLUMN_ROUTINE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
            
        

    public DatabaseHelper(@Nullable Context context) {
        super(context, "skincareapp.db", null, 10 );
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //       Cai nay goi dau tien khi database duoc accessed
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(createTableStatementuser);
//        sqLiteDatabase.execSQL(createTableStatementMyProduct);
//        sqLiteDatabase.execSQL(createProductTable);
        sqLiteDatabase.execSQL(createRoutineTable);
        sqLiteDatabase.execSQL(createRoutineProductTable);
    }
    /// version of database thay doi -> goi
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("Drop table if exists MYPRODUCT");
//        sqLiteDatabase.execSQL("Drop table if exists Users");
//        sqLiteDatabase.execSQL("Drop table if exists Product");
        sqLiteDatabase.execSQL("Drop table if exists " + TABLE_ROUTINE);
        sqLiteDatabase.execSQL("Drop table if exists " + "RoutineProduct");

        onCreate(sqLiteDatabase);
    }

    public User dangky(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_NAME, user.getName());
        cv.put(COLUMN_USER_EMAIL, user.getEmail());
        cv.put(COLUMN_USER_SKIN_TYPE, user.getLoaiDa());
        cv.put(COLUMN_USER_PASSWORD, user.getPassword());

        long insert = db.insert(TABLE_USERS, null, cv);
        if(insert != -1) {
            return new User((int)insert,user.getName(), user.getLoaiDa(), user.getEmail(), user.getPassword());
        } else {
            return null;
        }
    }

    public User dangnhap(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * from " + TABLE_USERS + " where " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{email, password});
        if(cursor.moveToFirst()) {
            return new User(cursor.getInt(0), cursor.getString(2), cursor.getString(4), cursor.getString(1), cursor.getString(3));
        }
        cursor.close();
        return null;
    }
    public User getUserById(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * from " + TABLE_USERS + " where " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(user_id)});
        if(cursor.moveToFirst()) {
            return new User(cursor.getInt(0), cursor.getString(2), cursor.getString(4), cursor.getString(1), cursor.getString(3));
        }
        cursor.close();
        return null;
    }

    public boolean addOneSP(MyProduct product) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, product.getTenSp());
        cv.put(COLUMN_BRAND, product.getNhanHieu());
        cv.put(COLUMN_gia, product.getGia());
        cv.put(COLUMN_anhurl, product.getUrl_Anh());
        cv.put(COLUMN_Date_mua, product.getNgayMua());
        cv.put(COLUMN_Date_hethan, product.getNgayHetHan());
        cv.put(COLUMN_MY_PRODUCT_USER_ID, product.getUser_id());

        long insert = db.insert(MYPRODUCT, null, cv);
        return insert == 1;
    }


    ///// Pattern
//    private void patternDB(SQLiteDatabase db, String modelQuery, String dataType, String definedName, int currentMonth, int currentYear, int user_id) {
//        Cursor cursor = db.rawQuery(modelQuery, new String[]{String.valueOf(currentMonth), String.valueOf(currentYear), String.valueOf(user_id)});
//        if(cursor.moveToFirst() && Objects.equals(dataType, "string")) {
//            combineMap.put(definedName, cursor.getString(0));
//        } else if (cursor.moveToFirst() && Objects.equals(dataType, "int")) {
//            combineMap.put(definedName, cursor.getInt(0));
//        }
//        cursor.close();
//    }
    private void patternDB(SQLiteDatabase db, String modelQuery, String dataType, String definedName, int user_id) {
        Cursor cursor = db.rawQuery(modelQuery, new String[]{ String.valueOf(user_id)});
        if(cursor.moveToFirst() && Objects.equals(dataType, "string")) {
            combineMap.put(definedName, cursor.getString(0));
        } else if (cursor.moveToFirst() && Objects.equals(dataType, "int")) {
            combineMap.put(definedName, cursor.getInt(0));
        }
        cursor.close();
    }
    public List<Routine> getAllRoutine(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Routine where user_id = ?", new String[]{String.valueOf(user_id)});
        List<Routine> routineList = new ArrayList<Routine>();
        if(cursor.moveToFirst()) {
            do {
                routineList.add(new Routine(cursor.getInt(0), user_id, cursor.getString(2), cursor.getString(1), null));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (Routine routine: routineList) {
            cursor = db.rawQuery("SELECT MYPRODUCT.ID, MYPRODUCT.NAME, MYPRODUCT.IMAGE_URL from RoutineProduct " +
                    "INNER JOIN MYPRODUCT ON RoutineProduct.product_id = MYPRODUCT.ID WHERE routine_id = ? and MYPRODUCT.user_id = ?", new String[]{String.valueOf(routine.getId()), String.valueOf(user_id)});
            List<MyProduct> productList = new ArrayList<MyProduct>();
            if(cursor.moveToFirst()) {
                do {
                    productList.add(new MyProduct(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
                } while (cursor.moveToNext());
            }
            routine.setProductList(productList);
            cursor.close();
        }
        db.close();
        return routineList;
    }
    public boolean deleteOneRoutine(int user_id, int routine_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_ROUTINE, "user_id = ? and id = ?", new String[]{String.valueOf(user_id), String.valueOf(routine_id)});

        return result != -1;
    }

    public boolean addOneRoutine(int user_id, Routine routine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ROUTINE_TIME, routine.getTime());
        cv.put(COLUMN_ROUTINE_NAME, routine.getName());
        cv.put(COLUMN_ROUTINE_USER_ID, user_id);

        long routineID = -1;

        List<MyProduct> routineProductList = routine.getProductList();
        try {
            db.beginTransaction();
            routineID = db.insert(TABLE_ROUTINE, null, cv);
            if(routineID == -1) {
                throw new Exception("Không tạo được bảng routine");
            }
            for (MyProduct product : routineProductList) {
                ContentValues cvRoutineProduct = new ContentValues();
                cvRoutineProduct.put("product_id", product.getId());
                cvRoutineProduct.put("routine_id", routineID);

                long result = db.insert("RoutineProduct", null, cvRoutineProduct);
                if (result == -1) {
                    throw new Exception("Có lỗi trong quá trình tạo dữ liệu bảng routine product");
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
            db.close();
        }
        return routineID != -1;
    }

    public boolean updateOneRoutine(int user_id, Routine routine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ROUTINE_NAME, routine.getName().toString());
        cv.put(COLUMN_ROUTINE_TIME, routine.getTime().toString());
        db.update(TABLE_ROUTINE, cv, "user_id = ? and id = ?", new String[]{String.valueOf(user_id), String.valueOf(routine.getId())});

        List<MyProduct> productList = routine.getProductList();
        ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>();
//
        for (MyProduct product: productList) {
            String query = "INSERT OR REPLACE INTO RoutineProduct (routine_id, product_id) " +
                    "VALUES (?, ?) ";
            db.execSQL(query, new Object[]{String.valueOf(routine.getId()), String.valueOf(product.getId())});
            arrayList.add(String.valueOf(product.getId()));
        }
        String excludedProduct = String.join(",", arrayList);
        String deleteQuery = "DELete from RoutineProduct where routine_id = ? and product_id not in ("+excludedProduct+")";
        db.execSQL(deleteQuery, new Object[]{String.valueOf(routine.getId())});
        db.close();
        return true;
    }

    public Map<String, Object> thongKe(int user_id) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        SQLiteDatabase db = this.getReadableDatabase();

        /// Lay max
        String Maxquery = "SELECT " + COLUMN_NAME + ", MAX(CAST(" + COLUMN_gia + " AS INTEGER)) AS MAX_PRICE " +
                "FROM MYPRODUCT WHERE strftime('%m', " + COLUMN_Date_mua + ") = '" + currentMonth + "' " +
                "AND strftime('%Y', " + COLUMN_Date_mua + ") = '" + currentYear + "' " +
                "AND user_id = ?";
        patternDB(db,Maxquery,"string", "max_gia_name", user_id);

        String averageQuery = "SELECT "+ "AVG(CAST("+COLUMN_gia +" AS INTEGER)) AS AVG_PRICE FROM MYPRODUCT where user_id = ?";
        patternDB(db, averageQuery, "int", "avg_price", user_id);

        String countQuery = "SELECT "+ "COUNT("+ COLUMN_ID +") AS count_product FROM MYPRODUCT where user_id = ?";
        patternDB(db,countQuery, "int", "count_product", user_id);

        String sumQuery = "SELECT "+ "SUM(CAST("+COLUMN_gia +" AS INTEGER)) AS sum_PRICE FROM MYPRODUCT where user_id = ?";
        patternDB(db, sumQuery, "int", "sum_price", user_id);

        String MonthLySpent = "SELECT SUM(CAST("+COLUMN_gia +" AS INTEGER)) AS COUNT_PRICE FROM MYPRODUCT where "
                + "strftime('%m', " + COLUMN_Date_mua + ") = ? AND strftime('%Y', " + COLUMN_Date_mua + ") = ? and user_id = ?";
        Cursor cursor = db.rawQuery(MonthLySpent, new String[]{String.valueOf(currentMonth), String.valueOf(currentYear), String.valueOf(user_id)});
        System.out.println(MonthLySpent);
        if (cursor.moveToFirst()) {
            combineMap.put("monthly_spent", cursor.getInt(0));
        } else {
            combineMap.put("monthly_spent", 0);
        }
        cursor.close();


        String spentByMonth = "Select strftime('%Y-%m'," + COLUMN_Date_mua + ") as month, SUM(CAST("+COLUMN_gia +" AS INTEGER)) AS COUNT_PRICE" +
                " from MYPRODUCT where strftime('%Y-%m'," + COLUMN_Date_mua + ") between strftime('%Y-%m', date('now', '-6 months')) and strftime('%Y-%m', date('now')) and user_id = ? group by strftime('%Y-%m'," + COLUMN_Date_mua + ") order by month ASC";
        cursor = db.rawQuery(spentByMonth, new String[]{String.valueOf(user_id)});
        Map<String, Integer> barChartValue = new HashMap<>();
        System.out.println(spentByMonth);
        while (cursor.moveToNext()) {
            barChartValue.put(cursor.getString(0), cursor.getInt(1));
        }
        combineMap.put("barChartValue", barChartValue);
        System.out.println(combineMap);
        cursor.close();
        db.close();
        return combineMap;
        
    }
    public List<MyProduct> getMyProduct(int user_id) throws ParseException {
        List<MyProduct> myProductList = new ArrayList<>();
        String queryString = "SELECT * FROM MYPRODUCT where user_id = " + user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()) {
            do {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                MyProduct myProduct = new MyProduct(cursor.getInt(0), cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(6), cursor.getString(5), user_id);
                myProductList.add(myProduct);
            } while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return myProductList;
                
    }
    public List<Product> getExpiredProducts(int user_id) {
        List<Product> resultList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "Select " + COLUMN_PRODUCT_NAME + ", " + COLUMN_anhurl + ", cast(julianday(strftime('%Y-%m-%d', "+ COLUMN_Date_hethan +" )) - julianday(strftime('%Y-%m-%d', 'now')) as INTEGER) as expire_date "+
                "from MYPRODUCT where cast(julianday(strftime('%Y-%m-%d', EXPIRE_DATE )) - julianday(strftime('%Y-%m-%d', 'now')) as INTEGER) <= 15 and user_id = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(user_id)});
        if (cursor.moveToFirst()) {
            do {
                resultList.add(new Product(cursor.getString(0), String.valueOf(cursor.getInt(2)), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        System.out.println(resultList);
        cursor.close();
        return resultList;
    }
    public boolean updateSP(MyProduct myProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BRAND, myProduct.getNhanHieu());
        cv.put(COLUMN_NAME, myProduct.getTenSp());
        cv.put(COLUMN_Date_hethan, myProduct.getNgayHetHan());
        cv.put(COLUMN_Date_mua, myProduct.getNgayMua());
        cv.put(COLUMN_anhurl, myProduct.getUrl_Anh());
        cv.put(COLUMN_gia, myProduct.getGia());

        long insert = db.update(MYPRODUCT, cv, "ID = ? and user_id = ?",
                new String[]{String.valueOf(myProduct.getId()), String.valueOf(myProduct.getUser_id())});
        db.close();
        return insert != -1;
    };
    public MyProduct getOneSP(int product_id, int user_id) {
        MyProduct myProduct = new MyProduct();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "Select * from MYPRODUCT where " + COLUMN_PRODUCT_ID + " = ? and user_id = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(product_id), String.valueOf(user_id)});
        if(cursor.moveToFirst()) {
            return new MyProduct(product_id, cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(6), cursor.getString(5), user_id);
        }
        cursor.close();
        db.close();
        return myProduct;
    }
    public void deleteOneSP(int productID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MYPRODUCT", "ID" + "=?", new String[]{String.valueOf(productID)} );
        db.close();
    }
    public User updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_EMAIL, user.getEmail());
        cv.put(COLUMN_USER_NAME, user.getName());
        cv.put(COLUMN_USER_SKIN_TYPE, user.getLoaiDa());

        long insert = db.update(TABLE_USERS,cv, "id = ?", new String[]{String.valueOf(user.getId())});
        if(insert != -1) {
            return new User(user.getId(),user.getName(), user.getLoaiDa(), user.getEmail(), user.getPassword());
        } else {
            return null;
        }
    }
}
