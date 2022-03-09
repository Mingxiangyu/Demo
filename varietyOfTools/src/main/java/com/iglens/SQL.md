##  MySQL 删除重复记录并且只保留一条

### 单个字段的操作

#### 查询分组

~~~sql
Select 重复字段 From 表 Group By 重复字段 Having Count(*)>1
~~~

查看是否有重复的数据：

- GROUP BY <列名序列>
- HAVING <组条件表达式>



> count(*)与count(1) 其实没有什么差别，用哪个都可以
>
> count(*)与count（列名）的区别：
>
> count(*)将返回表格中所有存在的行的总数包括值为null的行，然而count(列名)将返回表格中除去null以外的所有行的总数(有默认值的列也会被计入

####  **查询全部重复的数据**

~~~sql
Select * From 表 Where 重复字段 In (Select 重复字段 From 表 Group By 重复字段 Having Count(*)>1)
~~~

#### **查询所有要删除的数据（即重复数据）**

~~~sql
SELECT
 * 
FROM
 table_name AS ta 
WHERE
 ta.唯一键 <> ( SELECT max( tb.唯一键 ) FROM table_name AS tb WHERE ta.判断重复的列 = tb.判断重复的列 );
~~~

#### **删除表中多余重复试题并且只留1条**

~~~sql
DELETE 
FROM
 table_name AS ta 
WHERE
 ta.唯一键 <> (
SELECT
 t.maxid 
FROM
 ( SELECT max( tb.唯一键 ) AS maxid FROM table_name AS tb WHERE ta.判断重复的列 = tb.判断重复的列 ) t 
 );
~~~

**优化**
> - 在经常查询的字段上加上索引
> - 将*改为你需要查询出来的字段，不要全部查询出来
> - 小表驱动大表用IN，大表驱动小表用EXISTS。IN适合的情况是外表数据量小的情况，而不是外表数据大的情况，因为IN会遍历外表的全部数据，假设a表100条，b表10000条那么遍历次数就是100*10000次，而exists则是执行100次去判断a表中的数据是否在b表中存在，它只执行了a.length次数。至于哪一个效率高是要看情况的，因为in是在内存中比较的，而exists则是进行数据库查询操作的
>

