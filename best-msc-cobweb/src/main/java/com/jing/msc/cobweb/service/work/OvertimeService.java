package com.jing.msc.cobweb.service.work;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.msc.cobweb.entity.work.Overtime;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : jing
 * @since : 2025/4/29 15:08
 */
public interface OvertimeService extends IService<Overtime> {

    /**
     * 导入加班日志
     *
     * @param file excel文件
     * @return boolean
     */
    boolean importOvertime(MultipartFile file) throws IOException;


}
