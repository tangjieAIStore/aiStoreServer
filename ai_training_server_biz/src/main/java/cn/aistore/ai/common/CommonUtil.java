package cn.aistore.ai.common;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class CommonUtil {

    public static String getLikeStr(String str) {
        return "%" + str + "%";
    }

    public static Integer getRealPageNo(Integer pageNo) {
        if (pageNo == null || pageNo == 0) {
            return 0;
        }
        return pageNo - 1;
    }

    public static Integer getCurrentTime() {
        Long res = System.currentTimeMillis()/1000;
        return res.intValue();
    }

    public static <T, R> Page<R> copyPage(Page<T> sourcePage, ModelMapper modelMapper, Class<R> targetCls) {
        List<R> list = sourcePage.toList().stream().map(item -> modelMapper.map(item, targetCls))
                .collect(Collectors.toList());
        Page<R> resPage = new PageImpl<>(list, sourcePage.getPageable(), sourcePage.getTotalElements());
        return resPage;
    }

}
