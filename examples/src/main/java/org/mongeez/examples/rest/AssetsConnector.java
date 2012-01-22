package org.mongeez.examples.rest;

import org.mongeez.examples.dao.AssetDao;
import org.mongeez.examples.dto.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AssetsConnector {
    @Autowired
    private AssetDao assetDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Asset> find() {
        return assetDao.list();
    }
}
