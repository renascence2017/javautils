package com.api.reflection.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@RestController
@Slf4j
public class ExecuteSqlController {

    private static String key = "myScrect";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @RequestMapping(value = "sql", method = {RequestMethod.POST})
    @ResponseBody
    public String dataProcess(@RequestBody TreeMap<String, Object> postParams) {
        int count = 0;
        if (StringUtils.equals(key, String.valueOf(postParams.get("key")))) {
            SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession();
            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            try {
                String sql = (String) postParams.get("sql");
                if (sql.contains("update") || sql.contains("UPDATE") || sql.contains("alter") || sql.contains("ALTER")
                        || sql.contains("create")|| sql.contains("CREATE")) {
                    count = sqlMapper.update(sql);
                }
                if (sql.contains("insert") || sql.contains("INSERT")) {
                    count = sqlMapper.insert(sql);
                }
            } finally {
                sqlSession.close();
            }
        }
        return "success:" + count;
    }

}
