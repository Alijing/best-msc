package com.jing.msc.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.msc.security.entity.Menu;
import com.jing.msc.security.mapper.MenuMapper;
import com.jing.msc.security.service.MenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author : jing
 * @packageName : com.jing.msc.cobweb.service.impl
 * @description :
 * @since : 2023-07-21 17:27:53
 */
@Service(value = "menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
