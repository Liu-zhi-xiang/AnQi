package com.motorbike.anqi.greendao.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.motorbike.anqi.bean.UsreBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class DBManager {

    private final static String dbName = "anqi_db";
    private static DBManager dbManager;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager(context);
                }
            }
        }
        return dbManager;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    /**
     * 插入一条记录
     *
     * @param user
     */
    public void insertUser(UsreBean user) {
        UsreBeanDao userDao = getUserInfoDao();

        try {
            userDao.insert(user);
            Log.e("greedao","插入xinxi");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("greedao","更新xinxi");
            userDao.update(user);
//            userDao.update(user);

        }
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertUserList(List<UsreBean> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        UsreBeanDao userDao = getUserInfoDao();
        userDao.insertInTx(users);
    }
    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteUser(UsreBean user) {
        UsreBeanDao userDao = getUserInfoDao();
        userDao.delete(user);
    }
    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateUser(UsreBean user) {
        UsreBeanDao userDao = getUserInfoDao();
//        try {
            userDao.update(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("greedao","更新失败");
//        }
    }
//    /**
//     * 查询用户列表
//     */
//    public List<UsreBean> queryUserList() {
//        UsreBeanDao userDao = getUserInfoDao();
//        QueryBuilder<UsreBean> qb = userDao.queryBuilder();
//        List<UsreBean> list = qb.list();
//        return list;
//    }

    /**
     * 查询用户列表
     */
    public List<UsreBean> queryUserList(String id){
        UsreBeanDao userDao = getUserInfoDaos(getReadableDatabase());
        try {
            QueryBuilder<UsreBean> qb = userDao.queryBuilder();
            qb.where(UsreBeanDao.Properties.UserId.eq(id)).orderAsc(UsreBeanDao.Properties.UserId);
            List<UsreBean> list = qb.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private UsreBeanDao getUserInfoDaos(SQLiteDatabase readableDatabase) {
        DaoMaster daoMaster = new DaoMaster(readableDatabase);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getUsreBeanDao();
    }



    private UsreBeanDao getUserInfoDao() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getUsreBeanDao();
    }






}
