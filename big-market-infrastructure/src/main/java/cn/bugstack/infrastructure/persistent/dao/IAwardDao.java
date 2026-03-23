package cn.bugstack.infrastructure.persistent.dao;

import cn.bugstack.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: L
 * @description:奖品表DAO
 * @createTime: 2026-03-23 21:44
 * @version: 1.0
 */
@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();

}
