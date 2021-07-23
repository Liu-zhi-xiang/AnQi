package com.motorbike.anqi.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.motorbike.anqi.bean.UsreBean;

import com.motorbike.anqi.greendao.gen.UsreBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig usreBeanDaoConfig;

    private final UsreBeanDao usreBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        usreBeanDaoConfig = daoConfigMap.get(UsreBeanDao.class).clone();
        usreBeanDaoConfig.initIdentityScope(type);

        usreBeanDao = new UsreBeanDao(usreBeanDaoConfig, this);

        registerDao(UsreBean.class, usreBeanDao);
    }
    
    public void clear() {
        usreBeanDaoConfig.clearIdentityScope();
    }

    public UsreBeanDao getUsreBeanDao() {
        return usreBeanDao;
    }

}
