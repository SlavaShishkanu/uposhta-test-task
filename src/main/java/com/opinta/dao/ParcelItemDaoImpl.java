package com.opinta.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ParcelItemDaoImpl implements ParcelItemDao {
    // TODO
    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelItemDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ParcelItem> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(ParcelItem.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public ParcelItem getById(long id) {
        Session session = sessionFactory.getCurrentSession();
        return (ParcelItem) session.get(ParcelItem.class, id);
    }

    @Override
    public ParcelItem save(ParcelItem parcelItem) {
        Session session = sessionFactory.getCurrentSession();
        return (ParcelItem) session.merge(parcelItem);
    }

    @Override
    public void update(ParcelItem parcelItem) {
        Session session = sessionFactory.getCurrentSession();
        session.update(parcelItem);
    }

    @Override
    public void delete(ParcelItem parcelItem) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(parcelItem);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParcelItem> getByParcel(Parcel parcel) {
        // TODO Auto-generated method stub
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(ParcelItem.class).add(Restrictions.eq("parcel", parcel))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}

