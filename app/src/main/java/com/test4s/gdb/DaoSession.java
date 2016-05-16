package com.test4s.gdb;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.test4s.gdb.GameInfo;
import com.test4s.gdb.Investment;
import com.test4s.gdb.OutSource;
import com.test4s.gdb.IP;
import com.test4s.gdb.CP;
import com.test4s.gdb.Distribution;
import com.test4s.gdb.GameType;
import com.test4s.gdb.Adverts;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.gdb.IndexAdvert;
import com.test4s.gdb.Order;
import com.test4s.gdb.NewsInfo;
import com.test4s.gdb.History;
import com.test4s.gdb.Information;

import com.test4s.gdb.GameInfoDao;
import com.test4s.gdb.InvestmentDao;
import com.test4s.gdb.OutSourceDao;
import com.test4s.gdb.IPDao;
import com.test4s.gdb.CPDao;
import com.test4s.gdb.DistributionDao;
import com.test4s.gdb.GameTypeDao;
import com.test4s.gdb.AdvertsDao;
import com.test4s.gdb.IndexItemInfoDao;
import com.test4s.gdb.IndexAdvertDao;
import com.test4s.gdb.OrderDao;
import com.test4s.gdb.NewsInfoDao;
import com.test4s.gdb.HistoryDao;
import com.test4s.gdb.InformationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig gameInfoDaoConfig;
    private final DaoConfig investmentDaoConfig;
    private final DaoConfig outSourceDaoConfig;
    private final DaoConfig iPDaoConfig;
    private final DaoConfig cPDaoConfig;
    private final DaoConfig distributionDaoConfig;
    private final DaoConfig gameTypeDaoConfig;
    private final DaoConfig advertsDaoConfig;
    private final DaoConfig indexItemInfoDaoConfig;
    private final DaoConfig indexAdvertDaoConfig;
    private final DaoConfig orderDaoConfig;
    private final DaoConfig newsInfoDaoConfig;
    private final DaoConfig historyDaoConfig;
    private final DaoConfig informationDaoConfig;

    private final GameInfoDao gameInfoDao;
    private final InvestmentDao investmentDao;
    private final OutSourceDao outSourceDao;
    private final IPDao iPDao;
    private final CPDao cPDao;
    private final DistributionDao distributionDao;
    private final GameTypeDao gameTypeDao;
    private final AdvertsDao advertsDao;
    private final IndexItemInfoDao indexItemInfoDao;
    private final IndexAdvertDao indexAdvertDao;
    private final OrderDao orderDao;
    private final NewsInfoDao newsInfoDao;
    private final HistoryDao historyDao;
    private final InformationDao informationDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        gameInfoDaoConfig = daoConfigMap.get(GameInfoDao.class).clone();
        gameInfoDaoConfig.initIdentityScope(type);

        investmentDaoConfig = daoConfigMap.get(InvestmentDao.class).clone();
        investmentDaoConfig.initIdentityScope(type);

        outSourceDaoConfig = daoConfigMap.get(OutSourceDao.class).clone();
        outSourceDaoConfig.initIdentityScope(type);

        iPDaoConfig = daoConfigMap.get(IPDao.class).clone();
        iPDaoConfig.initIdentityScope(type);

        cPDaoConfig = daoConfigMap.get(CPDao.class).clone();
        cPDaoConfig.initIdentityScope(type);

        distributionDaoConfig = daoConfigMap.get(DistributionDao.class).clone();
        distributionDaoConfig.initIdentityScope(type);

        gameTypeDaoConfig = daoConfigMap.get(GameTypeDao.class).clone();
        gameTypeDaoConfig.initIdentityScope(type);

        advertsDaoConfig = daoConfigMap.get(AdvertsDao.class).clone();
        advertsDaoConfig.initIdentityScope(type);

        indexItemInfoDaoConfig = daoConfigMap.get(IndexItemInfoDao.class).clone();
        indexItemInfoDaoConfig.initIdentityScope(type);

        indexAdvertDaoConfig = daoConfigMap.get(IndexAdvertDao.class).clone();
        indexAdvertDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        newsInfoDaoConfig = daoConfigMap.get(NewsInfoDao.class).clone();
        newsInfoDaoConfig.initIdentityScope(type);

        historyDaoConfig = daoConfigMap.get(HistoryDao.class).clone();
        historyDaoConfig.initIdentityScope(type);

        informationDaoConfig = daoConfigMap.get(InformationDao.class).clone();
        informationDaoConfig.initIdentityScope(type);

        gameInfoDao = new GameInfoDao(gameInfoDaoConfig, this);
        investmentDao = new InvestmentDao(investmentDaoConfig, this);
        outSourceDao = new OutSourceDao(outSourceDaoConfig, this);
        iPDao = new IPDao(iPDaoConfig, this);
        cPDao = new CPDao(cPDaoConfig, this);
        distributionDao = new DistributionDao(distributionDaoConfig, this);
        gameTypeDao = new GameTypeDao(gameTypeDaoConfig, this);
        advertsDao = new AdvertsDao(advertsDaoConfig, this);
        indexItemInfoDao = new IndexItemInfoDao(indexItemInfoDaoConfig, this);
        indexAdvertDao = new IndexAdvertDao(indexAdvertDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);
        newsInfoDao = new NewsInfoDao(newsInfoDaoConfig, this);
        historyDao = new HistoryDao(historyDaoConfig, this);
        informationDao = new InformationDao(informationDaoConfig, this);

        registerDao(GameInfo.class, gameInfoDao);
        registerDao(Investment.class, investmentDao);
        registerDao(OutSource.class, outSourceDao);
        registerDao(IP.class, iPDao);
        registerDao(CP.class, cPDao);
        registerDao(Distribution.class, distributionDao);
        registerDao(GameType.class, gameTypeDao);
        registerDao(Adverts.class, advertsDao);
        registerDao(IndexItemInfo.class, indexItemInfoDao);
        registerDao(IndexAdvert.class, indexAdvertDao);
        registerDao(Order.class, orderDao);
        registerDao(NewsInfo.class, newsInfoDao);
        registerDao(History.class, historyDao);
        registerDao(Information.class, informationDao);
    }
    
    public void clear() {
        gameInfoDaoConfig.getIdentityScope().clear();
        investmentDaoConfig.getIdentityScope().clear();
        outSourceDaoConfig.getIdentityScope().clear();
        iPDaoConfig.getIdentityScope().clear();
        cPDaoConfig.getIdentityScope().clear();
        distributionDaoConfig.getIdentityScope().clear();
        gameTypeDaoConfig.getIdentityScope().clear();
        advertsDaoConfig.getIdentityScope().clear();
        indexItemInfoDaoConfig.getIdentityScope().clear();
        indexAdvertDaoConfig.getIdentityScope().clear();
        orderDaoConfig.getIdentityScope().clear();
        newsInfoDaoConfig.getIdentityScope().clear();
        historyDaoConfig.getIdentityScope().clear();
        informationDaoConfig.getIdentityScope().clear();
    }

    public GameInfoDao getGameInfoDao() {
        return gameInfoDao;
    }

    public InvestmentDao getInvestmentDao() {
        return investmentDao;
    }

    public OutSourceDao getOutSourceDao() {
        return outSourceDao;
    }

    public IPDao getIPDao() {
        return iPDao;
    }

    public CPDao getCPDao() {
        return cPDao;
    }

    public DistributionDao getDistributionDao() {
        return distributionDao;
    }

    public GameTypeDao getGameTypeDao() {
        return gameTypeDao;
    }

    public AdvertsDao getAdvertsDao() {
        return advertsDao;
    }

    public IndexItemInfoDao getIndexItemInfoDao() {
        return indexItemInfoDao;
    }

    public IndexAdvertDao getIndexAdvertDao() {
        return indexAdvertDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public NewsInfoDao getNewsInfoDao() {
        return newsInfoDao;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

    public InformationDao getInformationDao() {
        return informationDao;
    }

}
