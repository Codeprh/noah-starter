package com.noah.starter.demo.web.mybatis.service.impl;

import com.noah.starter.demo.web.mybatis.entity.Product;
import com.noah.starter.demo.web.mybatis.mapper.ProductMapper;
import com.noah.starter.demo.web.mybatis.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
