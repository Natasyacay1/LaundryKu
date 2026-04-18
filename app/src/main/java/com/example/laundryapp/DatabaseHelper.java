package com.example.laundryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "laundry.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_ADDRESS = "address";
    public static final String COL_USER_ROLE = "role";

    public static final String TABLE_SERVICES = "services";
    public static final String COL_SERVICE_ID = "id";
    public static final String COL_SERVICE_NAME = "name";
    public static final String COL_SERVICE_PRICE = "price";
    public static final String COL_SERVICE_UNIT = "unit";
    public static final String COL_SERVICE_DESC = "description";
    public static final String COL_SERVICE_ESTIMATE_DAYS = "estimate_days";

    public static final String TABLE_ORDERS = "orders";
    public static final String COL_ORDER_ID = "id";
    public static final String COL_ORDER_USER_ID = "user_id";
    public static final String COL_ORDER_SERVICE_ID = "service_id";
    public static final String COL_ORDER_WEIGHT = "weight";
    public static final String COL_ORDER_TOTAL = "total_price";
    public static final String COL_ORDER_DISCOUNT = "discount";
    public static final String COL_ORDER_FINAL_TOTAL = "final_total";
    public static final String COL_ORDER_PROMO_CODE = "promo_code";
    public static final String COL_ORDER_STATUS = "status";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_ORDER_FINISH_DATE = "finish_date";
    public static final String COL_ORDER_ESTIMATE_DATE = "estimate_date";
    public static final String COL_ORDER_NOTES = "notes";

    public static final String TABLE_RATINGS = "ratings";
    public static final String COL_RATING_ID = "id";
    public static final String COL_RATING_ORDER_ID = "order_id";
    public static final String COL_RATING_USER_ID = "user_id";
    public static final String COL_RATING_SCORE = "score";
    public static final String COL_RATING_REVIEW = "review";
    public static final String COL_RATING_DATE = "date";

    public static final String TABLE_PROMOS = "promos";
    public static final String COL_PROMO_ID = "id";
    public static final String COL_PROMO_CODE = "code";
    public static final String COL_PROMO_DESC = "description";
    public static final String COL_PROMO_TYPE = "type";
    public static final String COL_PROMO_VALUE = "value";
    public static final String COL_PROMO_MIN_ORDER = "min_order";
    public static final String COL_PROMO_ACTIVE = "is_active";
    public static final String COL_PROMO_EXPIRY = "expiry_date";

    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COL_NOTIF_ID = "id";
    public static final String COL_NOTIF_USER_ID = "user_id";
    public static final String COL_NOTIF_TITLE = "title";
    public static final String COL_NOTIF_MESSAGE = "message";
    public static final String COL_NOTIF_DATE = "date";
    public static final String COL_NOTIF_READ = "is_read";
    public static final String COL_NOTIF_ORDER_ID = "order_id";

    private static DatabaseHelper instance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    public DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NAME + " TEXT NOT NULL, " + COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " + COL_USER_PASSWORD + " TEXT NOT NULL, " + COL_USER_PHONE + " TEXT, " + COL_USER_ADDRESS + " TEXT, " + COL_USER_ROLE + " TEXT DEFAULT 'customer')");
        db.execSQL("CREATE TABLE " + TABLE_SERVICES + " (" + COL_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SERVICE_NAME + " TEXT NOT NULL, " + COL_SERVICE_PRICE + " REAL NOT NULL, " + COL_SERVICE_UNIT + " TEXT DEFAULT 'kg', " + COL_SERVICE_DESC + " TEXT, " + COL_SERVICE_ESTIMATE_DAYS + " INTEGER DEFAULT 2)");
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" + COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ORDER_USER_ID + " INTEGER NOT NULL, " + COL_ORDER_SERVICE_ID + " INTEGER NOT NULL, " + COL_ORDER_WEIGHT + " REAL NOT NULL, " + COL_ORDER_TOTAL + " REAL NOT NULL, " + COL_ORDER_DISCOUNT + " REAL DEFAULT 0, " + COL_ORDER_FINAL_TOTAL + " REAL NOT NULL, " + COL_ORDER_PROMO_CODE + " TEXT, " + COL_ORDER_STATUS + " TEXT DEFAULT 'Pending', " + COL_ORDER_DATE + " TEXT NOT NULL, " + COL_ORDER_FINISH_DATE + " TEXT, " + COL_ORDER_ESTIMATE_DATE + " TEXT, " + COL_ORDER_NOTES + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_RATINGS + " (" + COL_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_RATING_ORDER_ID + " INTEGER UNIQUE NOT NULL, " + COL_RATING_USER_ID + " INTEGER NOT NULL, " + COL_RATING_SCORE + " INTEGER NOT NULL, " + COL_RATING_REVIEW + " TEXT, " + COL_RATING_DATE + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_PROMOS + " (" + COL_PROMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PROMO_CODE + " TEXT UNIQUE NOT NULL, " + COL_PROMO_DESC + " TEXT, " + COL_PROMO_TYPE + " TEXT DEFAULT 'percent', " + COL_PROMO_VALUE + " REAL NOT NULL, " + COL_PROMO_MIN_ORDER + " REAL DEFAULT 0, " + COL_PROMO_ACTIVE + " INTEGER DEFAULT 1, " + COL_PROMO_EXPIRY + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NOTIFICATIONS + " (" + COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NOTIF_USER_ID + " INTEGER NOT NULL, " + COL_NOTIF_TITLE + " TEXT NOT NULL, " + COL_NOTIF_MESSAGE + " TEXT NOT NULL, " + COL_NOTIF_DATE + " TEXT NOT NULL, " + COL_NOTIF_READ + " INTEGER DEFAULT 0, " + COL_NOTIF_ORDER_ID + " INTEGER DEFAULT -1)");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (name,email,password,phone,role) VALUES ('Admin Laundry','admin@laundry.com','admin123','081234567890','admin')");
        db.execSQL("INSERT INTO " + TABLE_SERVICES + " (name,price,unit,description,estimate_days) VALUES ('Cuci & Setrika',7000,'kg','Cuci bersih + setrika rapi',2),('Cuci Kering',5000,'kg','Cuci + pengeringan mesin',1),('Setrika Saja',4000,'kg','Setrika pakaian bersih',1),('Express (1 Hari)',12000,'kg','Selesai dalam 1 hari',1),('Cuci Sepatu',30000,'pasang','Cuci bersih sepatu',3)");
        db.execSQL("INSERT INTO " + TABLE_PROMOS + " (code,description,type,value,min_order,is_active,expiry_date) VALUES ('HEMAT10','Diskon 10% untuk semua order','percent',10,20000,1,'2026-12-31'),('GRATIS5K','Potongan Rp 5.000','fixed',5000,15000,1,'2026-12-31')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // USER
    public long registerUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, user.getName()); cv.put(COL_USER_EMAIL, user.getEmail());
        cv.put(COL_USER_PASSWORD, user.getPassword()); cv.put(COL_USER_PHONE, user.getPhone());
        cv.put(COL_USER_ADDRESS, user.getAddress()); cv.put(COL_USER_ROLE, "customer");
        long r = db.insert(TABLE_USERS, null, cv); db.close(); return r;
    }
    public long addAdmin(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, user.getName()); cv.put(COL_USER_EMAIL, user.getEmail());
        cv.put(COL_USER_PASSWORD, user.getPassword()); cv.put(COL_USER_PHONE, user.getPhone());
        cv.put(COL_USER_ROLE, "admin");
        long r = db.insert(TABLE_USERS, null, cv); db.close(); return r;
    }
    public UserModel loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null, COL_USER_EMAIL+"=? AND "+COL_USER_PASSWORD+"=?", new String[]{email,password}, null,null,null);
        UserModel u = null; if(c!=null&&c.moveToFirst()){u=cursorToUser(c);c.close();} db.close(); return u;
    }
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,new String[]{COL_USER_ID},COL_USER_EMAIL+"=?",new String[]{email},null,null,null);
        boolean e=(c!=null&&c.getCount()>0); if(c!=null)c.close(); db.close(); return e;
    }
    public UserModel getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,null,COL_USER_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        UserModel u=null; if(c!=null&&c.moveToFirst()){u=cursorToUser(c);c.close();} db.close(); return u;
    }
    public List<UserModel> getAllCustomers() {
        List<UserModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_USERS,null,COL_USER_ROLE+"=?",new String[]{"customer"},null,null,COL_USER_NAME+" ASC");
        if(c!=null){while(c.moveToNext())list.add(cursorToUser(c));c.close();} db.close(); return list;
    }
    public List<UserModel> getAllAdmins() {
        List<UserModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_USERS,null,COL_USER_ROLE+"=?",new String[]{"admin"},null,null,COL_USER_NAME+" ASC");
        if(c!=null){while(c.moveToNext())list.add(cursorToUser(c));c.close();} db.close(); return list;
    }
    public int updateUser(UserModel user) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_USER_NAME,user.getName()); cv.put(COL_USER_PHONE,user.getPhone()); cv.put(COL_USER_ADDRESS,user.getAddress());
        if(user.getPassword()!=null&&!user.getPassword().isEmpty()) cv.put(COL_USER_PASSWORD,user.getPassword());
        int r=db.update(TABLE_USERS,cv,COL_USER_ID+"=?",new String[]{String.valueOf(user.getId())}); db.close(); return r;
    }
    public int deleteUser(int id) { SQLiteDatabase db=this.getWritableDatabase(); int r=db.delete(TABLE_USERS,COL_USER_ID+"=?",new String[]{String.valueOf(id)}); db.close(); return r; }
    private UserModel cursorToUser(Cursor c) {
        UserModel u=new UserModel(); u.setId(c.getInt(c.getColumnIndexOrThrow(COL_USER_ID))); u.setName(c.getString(c.getColumnIndexOrThrow(COL_USER_NAME)));
        u.setEmail(c.getString(c.getColumnIndexOrThrow(COL_USER_EMAIL))); u.setPassword(c.getString(c.getColumnIndexOrThrow(COL_USER_PASSWORD)));
        u.setPhone(c.getString(c.getColumnIndexOrThrow(COL_USER_PHONE))); u.setAddress(c.getString(c.getColumnIndexOrThrow(COL_USER_ADDRESS)));
        u.setRole(c.getString(c.getColumnIndexOrThrow(COL_USER_ROLE))); return u;
    }

    // SERVICE
    public long addService(ServiceModel s) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_SERVICE_NAME,s.getName()); cv.put(COL_SERVICE_PRICE,s.getPrice()); cv.put(COL_SERVICE_UNIT,s.getUnit()); cv.put(COL_SERVICE_DESC,s.getDescription()); cv.put(COL_SERVICE_ESTIMATE_DAYS,s.getEstimateDays());
        long r=db.insert(TABLE_SERVICES,null,cv); db.close(); return r;
    }
    public List<ServiceModel> getAllServices() {
        List<ServiceModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_SERVICES,null,null,null,null,null,COL_SERVICE_NAME+" ASC");
        if(c!=null){while(c.moveToNext())list.add(cursorToService(c));c.close();} db.close(); return list;
    }
    public ServiceModel getServiceById(int id) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_SERVICES,null,COL_SERVICE_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        ServiceModel s=null; if(c!=null&&c.moveToFirst()){s=cursorToService(c);c.close();} db.close(); return s;
    }
    public int updateService(ServiceModel s) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_SERVICE_NAME,s.getName()); cv.put(COL_SERVICE_PRICE,s.getPrice()); cv.put(COL_SERVICE_UNIT,s.getUnit()); cv.put(COL_SERVICE_DESC,s.getDescription()); cv.put(COL_SERVICE_ESTIMATE_DAYS,s.getEstimateDays());
        int r=db.update(TABLE_SERVICES,cv,COL_SERVICE_ID+"=?",new String[]{String.valueOf(s.getId())}); db.close(); return r;
    }
    public int deleteService(int id) { SQLiteDatabase db=this.getWritableDatabase(); int r=db.delete(TABLE_SERVICES,COL_SERVICE_ID+"=?",new String[]{String.valueOf(id)}); db.close(); return r; }
    private ServiceModel cursorToService(Cursor c) {
        ServiceModel s=new ServiceModel(); s.setId(c.getInt(c.getColumnIndexOrThrow(COL_SERVICE_ID))); s.setName(c.getString(c.getColumnIndexOrThrow(COL_SERVICE_NAME)));
        s.setPrice(c.getDouble(c.getColumnIndexOrThrow(COL_SERVICE_PRICE))); s.setUnit(c.getString(c.getColumnIndexOrThrow(COL_SERVICE_UNIT))); s.setDescription(c.getString(c.getColumnIndexOrThrow(COL_SERVICE_DESC)));
        int ei=c.getColumnIndex(COL_SERVICE_ESTIMATE_DAYS); s.setEstimateDays(ei>=0?c.getInt(ei):2); return s;
    }

    // ORDER
    public long addOrder(OrderModel o) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_ORDER_USER_ID,o.getUserId()); cv.put(COL_ORDER_SERVICE_ID,o.getServiceId()); cv.put(COL_ORDER_WEIGHT,o.getWeight());
        cv.put(COL_ORDER_TOTAL,o.getTotalPrice()); cv.put(COL_ORDER_DISCOUNT,o.getDiscount()); cv.put(COL_ORDER_FINAL_TOTAL,o.getFinalTotal());
        cv.put(COL_ORDER_PROMO_CODE,o.getPromoCode()); cv.put(COL_ORDER_STATUS,o.getStatus()); cv.put(COL_ORDER_DATE,o.getOrderDate());
        cv.put(COL_ORDER_FINISH_DATE,o.getFinishDate()); cv.put(COL_ORDER_ESTIMATE_DATE,o.getEstimateDate()); cv.put(COL_ORDER_NOTES,o.getNotes());
        long r=db.insert(TABLE_ORDERS,null,cv); db.close(); return r;
    }
    public List<OrderModel> getAllOrders() { return queryOrders(null,null); }
    public List<OrderModel> getOrdersByUser(int uid) { return queryOrders("o."+COL_ORDER_USER_ID+"=?",new String[]{String.valueOf(uid)}); }
    public List<OrderModel> getActiveOrdersByUser(int uid) { return queryOrders("o."+COL_ORDER_USER_ID+"=? AND o."+COL_ORDER_STATUS+"!='Diambil'",new String[]{String.valueOf(uid)}); }
    public OrderModel getOrderById(int id) { List<OrderModel> l=queryOrders("o."+COL_ORDER_ID+"=?",new String[]{String.valueOf(id)}); return l.isEmpty()?null:l.get(0); }
    public List<OrderModel> searchOrders(String q) { return queryOrders("(CAST(o."+COL_ORDER_ID+" AS TEXT) LIKE ? OR u."+COL_USER_NAME+" LIKE ? OR s."+COL_SERVICE_NAME+" LIKE ? OR o."+COL_ORDER_STATUS+" LIKE ? OR o."+COL_ORDER_DATE+" LIKE ?)",new String[]{"%" +q+"%","%"+q+"%","%"+q+"%","%"+q+"%","%"+q+"%"}); }
    public List<OrderModel> searchOrdersByUser(int uid,String q) { return queryOrders("o."+COL_ORDER_USER_ID+"=? AND (CAST(o."+COL_ORDER_ID+" AS TEXT) LIKE ? OR s."+COL_SERVICE_NAME+" LIKE ? OR o."+COL_ORDER_STATUS+" LIKE ? OR o."+COL_ORDER_DATE+" LIKE ?)",new String[]{String.valueOf(uid),"%"+q+"%","%"+q+"%","%"+q+"%","%"+q+"%"}); }
    private List<OrderModel> queryOrders(String where,String[] args) {
        List<OrderModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        String sql="SELECT o.*,u.name as user_name,s.name as service_name,s.unit as service_unit,s.estimate_days FROM "+TABLE_ORDERS+" o JOIN "+TABLE_USERS+" u ON o."+COL_ORDER_USER_ID+"=u."+COL_USER_ID+" JOIN "+TABLE_SERVICES+" s ON o."+COL_ORDER_SERVICE_ID+"=s."+COL_SERVICE_ID;
        if(where!=null)sql+=" WHERE "+where; sql+=" ORDER BY o."+COL_ORDER_ID+" DESC";
        Cursor c=db.rawQuery(sql,args); if(c!=null){while(c.moveToNext())list.add(cursorToOrder(c));c.close();} db.close(); return list;
    }
    public int updateOrderStatus(int oid,String status) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues(); cv.put(COL_ORDER_STATUS,status);
        int r=db.update(TABLE_ORDERS,cv,COL_ORDER_ID+"=?",new String[]{String.valueOf(oid)}); db.close(); return r;
    }
    public int updateOrder(OrderModel o) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_ORDER_SERVICE_ID,o.getServiceId()); cv.put(COL_ORDER_WEIGHT,o.getWeight()); cv.put(COL_ORDER_TOTAL,o.getTotalPrice());
        cv.put(COL_ORDER_DISCOUNT,o.getDiscount()); cv.put(COL_ORDER_FINAL_TOTAL,o.getFinalTotal()); cv.put(COL_ORDER_STATUS,o.getStatus());
        cv.put(COL_ORDER_FINISH_DATE,o.getFinishDate()); cv.put(COL_ORDER_ESTIMATE_DATE,o.getEstimateDate()); cv.put(COL_ORDER_NOTES,o.getNotes());
        int r=db.update(TABLE_ORDERS,cv,COL_ORDER_ID+"=?",new String[]{String.valueOf(o.getId())}); db.close(); return r;
    }
    public int deleteOrder(int id) { SQLiteDatabase db=this.getWritableDatabase(); int r=db.delete(TABLE_ORDERS,COL_ORDER_ID+"=?",new String[]{String.valueOf(id)}); db.close(); return r; }
    public int getTotalOrders(){return countQuery("SELECT COUNT(*) FROM "+TABLE_ORDERS);}
    public int getPendingOrders(){return countQuery("SELECT COUNT(*) FROM "+TABLE_ORDERS+" WHERE "+COL_ORDER_STATUS+"='Pending'");}
    public int getOrderCountByStatus(String s){return countQuery("SELECT COUNT(*) FROM "+TABLE_ORDERS+" WHERE "+COL_ORDER_STATUS+"='"+s+"'");}
    public double getTotalRevenue(){SQLiteDatabase db=this.getReadableDatabase();Cursor c=db.rawQuery("SELECT SUM("+COL_ORDER_FINAL_TOTAL+") FROM "+TABLE_ORDERS+" WHERE "+COL_ORDER_STATUS+"='Diambil'",null);double t=0;if(c!=null&&c.moveToFirst()){t=c.getDouble(0);c.close();}db.close();return t;}
    public int getTotalCustomers(){return countQuery("SELECT COUNT(*) FROM "+TABLE_USERS+" WHERE "+COL_USER_ROLE+"='customer'");}
    private int countQuery(String sql){SQLiteDatabase db=this.getReadableDatabase();Cursor c=db.rawQuery(sql,null);int n=0;if(c!=null&&c.moveToFirst()){n=c.getInt(0);c.close();}db.close();return n;}
    private OrderModel cursorToOrder(Cursor c) {
        OrderModel o=new OrderModel(); o.setId(c.getInt(c.getColumnIndexOrThrow(COL_ORDER_ID))); o.setUserId(c.getInt(c.getColumnIndexOrThrow(COL_ORDER_USER_ID)));
        o.setServiceId(c.getInt(c.getColumnIndexOrThrow(COL_ORDER_SERVICE_ID))); o.setWeight(c.getDouble(c.getColumnIndexOrThrow(COL_ORDER_WEIGHT))); o.setTotalPrice(c.getDouble(c.getColumnIndexOrThrow(COL_ORDER_TOTAL)));
        int di=c.getColumnIndex(COL_ORDER_DISCOUNT); o.setDiscount(di>=0?c.getDouble(di):0);
        int fi=c.getColumnIndex(COL_ORDER_FINAL_TOTAL); o.setFinalTotal(fi>=0?c.getDouble(fi):o.getTotalPrice());
        int pi=c.getColumnIndex(COL_ORDER_PROMO_CODE); o.setPromoCode(pi>=0?c.getString(pi):null);
        o.setStatus(c.getString(c.getColumnIndexOrThrow(COL_ORDER_STATUS))); o.setOrderDate(c.getString(c.getColumnIndexOrThrow(COL_ORDER_DATE))); o.setFinishDate(c.getString(c.getColumnIndexOrThrow(COL_ORDER_FINISH_DATE)));
        int ei=c.getColumnIndex(COL_ORDER_ESTIMATE_DATE); o.setEstimateDate(ei>=0?c.getString(ei):null); o.setNotes(c.getString(c.getColumnIndexOrThrow(COL_ORDER_NOTES)));
        int un=c.getColumnIndex("user_name"); int sn=c.getColumnIndex("service_name"); int su=c.getColumnIndex("service_unit");
        if(un>=0)o.setUserName(c.getString(un)); if(sn>=0)o.setServiceName(c.getString(sn)); if(su>=0)o.setServiceUnit(c.getString(su)); return o;
    }

    // RATING
    public long addRating(RatingModel r) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_RATING_ORDER_ID,r.getOrderId()); cv.put(COL_RATING_USER_ID,r.getUserId()); cv.put(COL_RATING_SCORE,r.getScore()); cv.put(COL_RATING_REVIEW,r.getReview()); cv.put(COL_RATING_DATE,r.getDate());
        long res=db.insert(TABLE_RATINGS,null,cv); db.close(); return res;
    }
    public RatingModel getRatingByOrderId(int oid) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_RATINGS,null,COL_RATING_ORDER_ID+"=?",new String[]{String.valueOf(oid)},null,null,null);
        RatingModel r=null; if(c!=null&&c.moveToFirst()){r=new RatingModel();r.setId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_ID)));r.setOrderId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_ORDER_ID)));r.setUserId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_USER_ID)));r.setScore(c.getInt(c.getColumnIndexOrThrow(COL_RATING_SCORE)));r.setReview(c.getString(c.getColumnIndexOrThrow(COL_RATING_REVIEW)));r.setDate(c.getString(c.getColumnIndexOrThrow(COL_RATING_DATE)));c.close();}
        db.close(); return r;
    }
    public double getAverageRating(){SQLiteDatabase db=this.getReadableDatabase();Cursor c=db.rawQuery("SELECT AVG("+COL_RATING_SCORE+") FROM "+TABLE_RATINGS,null);double a=0;if(c!=null&&c.moveToFirst()){a=c.getDouble(0);c.close();}db.close();return a;}
    public List<RatingModel> getAllRatings() {
        List<RatingModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT r.*,u.name as user_name,s.name as service_name FROM "+TABLE_RATINGS+" r JOIN "+TABLE_USERS+" u ON r."+COL_RATING_USER_ID+"=u."+COL_USER_ID+" JOIN "+TABLE_ORDERS+" o ON r."+COL_RATING_ORDER_ID+"=o."+COL_ORDER_ID+" JOIN "+TABLE_SERVICES+" s ON o."+COL_ORDER_SERVICE_ID+"=s."+COL_SERVICE_ID+" ORDER BY r."+COL_RATING_ID+" DESC",null);
        if(c!=null){while(c.moveToNext()){RatingModel r=new RatingModel();r.setId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_ID)));r.setOrderId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_ORDER_ID)));r.setUserId(c.getInt(c.getColumnIndexOrThrow(COL_RATING_USER_ID)));r.setScore(c.getInt(c.getColumnIndexOrThrow(COL_RATING_SCORE)));r.setReview(c.getString(c.getColumnIndexOrThrow(COL_RATING_REVIEW)));r.setDate(c.getString(c.getColumnIndexOrThrow(COL_RATING_DATE)));int un=c.getColumnIndex("user_name");int sn=c.getColumnIndex("service_name");if(un>=0)r.setUserName(c.getString(un));if(sn>=0)r.setServiceName(c.getString(sn));list.add(r);}c.close();}
        db.close(); return list;
    }

    // PROMO
    public PromoModel getPromoByCode(String code) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_PROMOS,null,COL_PROMO_CODE+"=? AND "+COL_PROMO_ACTIVE+"=1",new String[]{code.toUpperCase()},null,null,null);
        PromoModel p=null; if(c!=null&&c.moveToFirst()){p=cursorToPromo(c);c.close();} db.close(); return p;
    }
    public List<PromoModel> getAllPromos() {
        List<PromoModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_PROMOS,null,null,null,null,null,COL_PROMO_ID+" DESC");
        if(c!=null){while(c.moveToNext())list.add(cursorToPromo(c));c.close();} db.close(); return list;
    }
    public long addPromo(PromoModel p) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_PROMO_CODE,p.getCode().toUpperCase()); cv.put(COL_PROMO_DESC,p.getDescription()); cv.put(COL_PROMO_TYPE,p.getType()); cv.put(COL_PROMO_VALUE,p.getValue()); cv.put(COL_PROMO_MIN_ORDER,p.getMinOrder()); cv.put(COL_PROMO_ACTIVE,p.isActive()?1:0); cv.put(COL_PROMO_EXPIRY,p.getExpiryDate());
        long r=db.insert(TABLE_PROMOS,null,cv); db.close(); return r;
    }
    public int updatePromo(PromoModel p) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_PROMO_CODE,p.getCode().toUpperCase()); cv.put(COL_PROMO_DESC,p.getDescription()); cv.put(COL_PROMO_TYPE,p.getType()); cv.put(COL_PROMO_VALUE,p.getValue()); cv.put(COL_PROMO_MIN_ORDER,p.getMinOrder()); cv.put(COL_PROMO_ACTIVE,p.isActive()?1:0); cv.put(COL_PROMO_EXPIRY,p.getExpiryDate());
        int r=db.update(TABLE_PROMOS,cv,COL_PROMO_ID+"=?",new String[]{String.valueOf(p.getId())}); db.close(); return r;
    }
    public int deletePromo(int id){SQLiteDatabase db=this.getWritableDatabase();int r=db.delete(TABLE_PROMOS,COL_PROMO_ID+"=?",new String[]{String.valueOf(id)});db.close();return r;}
    private PromoModel cursorToPromo(Cursor c){PromoModel p=new PromoModel();p.setId(c.getInt(c.getColumnIndexOrThrow(COL_PROMO_ID)));p.setCode(c.getString(c.getColumnIndexOrThrow(COL_PROMO_CODE)));p.setDescription(c.getString(c.getColumnIndexOrThrow(COL_PROMO_DESC)));p.setType(c.getString(c.getColumnIndexOrThrow(COL_PROMO_TYPE)));p.setValue(c.getDouble(c.getColumnIndexOrThrow(COL_PROMO_VALUE)));p.setMinOrder(c.getDouble(c.getColumnIndexOrThrow(COL_PROMO_MIN_ORDER)));p.setActive(c.getInt(c.getColumnIndexOrThrow(COL_PROMO_ACTIVE))==1);p.setExpiryDate(c.getString(c.getColumnIndexOrThrow(COL_PROMO_EXPIRY)));return p;}

    // NOTIFICATION
    public long addNotification(NotificationModel n) {
        SQLiteDatabase db=this.getWritableDatabase(); ContentValues cv=new ContentValues();
        cv.put(COL_NOTIF_USER_ID,n.getUserId()); cv.put(COL_NOTIF_TITLE,n.getTitle()); cv.put(COL_NOTIF_MESSAGE,n.getMessage()); cv.put(COL_NOTIF_DATE,n.getDate()); cv.put(COL_NOTIF_READ,0); cv.put(COL_NOTIF_ORDER_ID,n.getOrderId());
        long r=db.insert(TABLE_NOTIFICATIONS,null,cv); db.close(); return r;
    }
    public List<NotificationModel> getNotificationsByUser(int uid) {
        List<NotificationModel> list=new ArrayList<>(); SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_NOTIFICATIONS,null,COL_NOTIF_USER_ID+"=?",new String[]{String.valueOf(uid)},null,null,COL_NOTIF_ID+" DESC");
        if(c!=null){while(c.moveToNext())list.add(cursorToNotif(c));c.close();} db.close(); return list;
    }
    public int getUnreadCount(int uid){return countQuery("SELECT COUNT(*) FROM "+TABLE_NOTIFICATIONS+" WHERE "+COL_NOTIF_USER_ID+"="+uid+" AND "+COL_NOTIF_READ+"=0");}
    public void markAllRead(int uid){SQLiteDatabase db=this.getWritableDatabase();ContentValues cv=new ContentValues();cv.put(COL_NOTIF_READ,1);db.update(TABLE_NOTIFICATIONS,cv,COL_NOTIF_USER_ID+"=?",new String[]{String.valueOf(uid)});db.close();}
    private NotificationModel cursorToNotif(Cursor c){NotificationModel n=new NotificationModel();n.setId(c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_ID)));n.setUserId(c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_USER_ID)));n.setTitle(c.getString(c.getColumnIndexOrThrow(COL_NOTIF_TITLE)));n.setMessage(c.getString(c.getColumnIndexOrThrow(COL_NOTIF_MESSAGE)));n.setDate(c.getString(c.getColumnIndexOrThrow(COL_NOTIF_DATE)));n.setRead(c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_READ))==1);n.setOrderId(c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_ORDER_ID)));return n;}
}
