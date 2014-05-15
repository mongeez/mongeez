package org.mongeez.examples.dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongeez.examples.dto.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("assetDaoMongo")
public class AssetDaoImpl extends BasicDAO<Asset, String> implements AssetDao {
    @Autowired
    public AssetDaoImpl(Datastore ds) {
        super(Asset.class, ds);
    }

    public List<Asset> list() {
        return find().asList();
    }
}
