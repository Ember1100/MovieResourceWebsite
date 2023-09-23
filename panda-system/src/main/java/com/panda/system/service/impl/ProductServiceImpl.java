package com.panda.system.service.impl;

import com.panda.system.domin.Product;
import com.panda.system.mapper.ProductMapper;
import com.panda.system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getList() {
        return productMapper.getProductList();
    }

}
