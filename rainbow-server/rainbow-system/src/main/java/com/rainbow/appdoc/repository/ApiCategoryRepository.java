package com.rainbow.appdoc.repository;

import com.rainbow.appdoc.entity.AppCategory;
import com.rainbow.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiCategoryRepository extends BaseRepository<AppCategory, String> {

  @Query("select t from AppCategory t " +
          "  where t.status = '0' " +
          " and (?1 is null " +
          "   or LOWER(className) like concat('%',LOWER(?1),'%')" +
          "   or LOWER(simpleName) like concat('%',LOWER(?1),'%')" +
          "   or LOWER(requestUrl) like concat('%',LOWER(?1),'%')" +
          "   or LOWER(description) like concat('%',LOWER(?1),'%')  ) " +
          "   order by t.orderNum")
  Page<AppCategory> findByKeyword(String keyword, Pageable pageable);

  @Query(value = "\t select c.* from app_category c \n" +
          "\t inner join (\n" +
          "\t select t.id,count(a.category_id) cnt,count(m.interface_id) cnt2\n" +
          "\t from app_category t\n" +
          "\t left join app_interface a on t.id = a.category_id \n" +
          "\t left join sys_menu m on a.id = m.interface_id \n" +
          "\t where t.status = '0' \n" +
          "\t   and t.disabled = 0 \n" +
          "\t group by t.id ) d on c.id = d.id  and d.cnt2 < d.cnt \n " +
          "\t  where c.status= '0' \n" +
          "\t    and c.disabled = 0 " +
          "\t    and (?1 is null \n" +
          "\t   or LOWER(c.class_name) like  concat('%',LOWER(?1),'%') \n" +
          "\t   or LOWER(c.simple_name) like concat('%',LOWER(?1),'%') \n" +
          "\t   or LOWER(c.request_url) like concat('%',LOWER(?1),'%') \n" +
          "\t   or LOWER(c.description) like concat('%',LOWER(?1),'%')  ) " +
          "\t  order by c.order_num ", nativeQuery = true)
  Page<AppCategory> findMenuByKeyword(String keyword, Pageable pageable);

  @Query("select t from AppCategory t where id not in (?1)")
  List<AppCategory> findNotInId(List<String> idList);


}