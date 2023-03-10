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

## MySQL统计年月日的数据量

>https://blog.csdn.net/qq_43170312/article/details/125320371

### 使用SQL查询近30天/月的日期

####  近30天的日期

```sql
SELECT ADDDATE(y.first, x.d) as date
FROM
(SELECT 1 AS d UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL
SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL
SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL
SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL
SELECT 29 UNION ALL SELECT 30 ) x,
(SELECT DATE_SUB( '2022-06-14' , INTERVAL 30 DAY) AS first,CONCAT('2022-06-14') AS last) y
```

#### 近30个月的日期

```sql
SELECT ADDDATE(y.first,INTERVAL + x.d MONTH) as date
FROM
(SELECT 1 AS d UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL
SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL
SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL
SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 UNION ALL SELECT 28 UNION ALL
SELECT 29 UNION ALL SELECT 30 ) x,
(SELECT DATE_SUB( '2022-06-14' , INTERVAL 30 MONTH) AS first,CONCAT('2022-06-14') AS last) y
```

## Mysql 按日，周，月统计

> https://blog.csdn.net/weixin_43190879/article/details/99544779

*to_char(date,”yyyy-mm-dd”),结果报错说to_char方法不存在,一查才发现to_char方法是Oracle的,想在MySQL中用相应的要用date_format(date,’%Y-%m-%d %H:%i:%S’),改为date_format后显示正常*https://blog.csdn.net/sx729034738/article/details/70314463

下面给出方法格式化参数:

```sh
%Y：代表4位的年份
%y：代表2为的年份

%m：代表月, 格式为(01……12)
%c：代表月, 格式为(1……12)

%d：代表月份中的天数,格式为(00……31)
%e：代表月份中的天数, 格式为(0……31)

%H：代表小时,格式为(00……23)
%k：代表 小时,格式为(0……23)
%h： 代表小时,格式为(01……12)
%I： 代表小时,格式为(01……12)
%l ：代表小时,格式为(1……12)

%i： 代表分钟, 格式为(00……59)

%r：代表 时间,格式为12 小时(hh:mm:ss [AP]M)
%T：代表 时间,格式为24 小时(hh:mm:ss)

%S：代表 秒,格式为(00……59)
%s：代表 秒,格式为(00……59)
```

### 按天统计

如果表内时间字段格式是[date类](https://so.csdn.net/so/search?q=date类&spm=1001.2101.3001.7020)型的就不用to_date(day,‘YYYY-MM-dd’)转换了，

```sql
 select max as 最大降雨,min as 最小降雨,prcp as 降雨量,sndp as 降雪量,s_date as 日期
    from s_data where s_date >= '2022-01-01' and s_date <= '2022-12-31'
```
结果如下表：

| 最大降雨 | 最小降雨 | 降雨量 | 降雪量 | 日期       |
| -------- | -------- | ------ | ------ | ---------- |
| 46.9     | 10.4     | 0      | 999.9  | 2022/1/1   |
| 46.9     | 12.2     | 0      | 999.9  | 2022/1/2   |
| 43.7     | 12.2     | 0      | 999.9  | 2022/1/3   |
| 43.7     | 14       | 0      | 999.9  | 2022/1/4   |
| 39       | 17.6     | 0      | 999.9  | 2022/1/5   |
| 43.7     | 15.8     | 0      | 999.9  | 2022/1/6   |
| 43.7     | 17.6     | 0      | 999.9  | 2022/1/7   |
| 49.3     | 20.5     | 0      | 999.9  | 2022/1/8   |
| 49.3     | 20.8     | 0      | 999.9  | 2022/1/9   |
| 36.3     | 15.8     | 0      | 999.9  | 2022/1/10  |
| 36.3     | 14       | 0      | 999.9  | 2022/1/11  |
| 37       | 16.7     | 0      | 999.9  | 2022/1/12  |
| 39.2     | 10.4     | 0      | 999.9  | 2022/1/13  |
| 39       | 12.2     | 0      | 999.9  | 2022/1/14  |
| 39.2     | 14       | 0      | 999.9  | 2022/1/15  |
| 38.5     | 12.2     | 0      | 999.9  | 2022/1/16  |
| -        | -        | -      | -      | 2022/12/31 |

### 按月统计

```sql
select date_format(s_date,'%m') AS 日期,MAX(max) as 最大降雨 ,MIN(min) as 最小降雨 , AVG(temp) as 平均降雨, AVG(prcp) as 降雨量, AVG(sndp) as 降雪量
    from  s_data  
    where s_date >= '2022-01-01' and s_date <= '2022-12-31'
    group by date_format(s_date,'%m')  
    ORDER BY s_date;  
```
结果如下表：
| 日期 | 最大降雨 | 最小降雨 | 平均降雨    | 降雨量      | 降雪量      |
| ---- | -------- | -------- | ----------- | ----------- | ----------- |
| 1    | 49.3     | 10.4     | 26.94516139 | 0.003548387 | 999.9000244 |
| 2    | 61.2     | 6.8      | 29.33928592 | 0.004285714 | 999.9000244 |
| 3    | 69.8     | 19.4     | 44.46774169 | 0.012903226 | 967.6580881 |
| 4    | 84.2     | 32       | 60.45333366 | 0.017666667 | 999.9000244 |
| 5    | 98.6     | 42.8     | 69.64193578 | 0.014193548 | 999.9000244 |
| 6    | 102.6    | 55.4     | 77.63666636 | 0.114666666 | 999.9000244 |
| 7    | 99       | 66.2     | 81.2193542  | 0.38354839  | 999.9000244 |
| 8    | 98.6     | 51.8     | 78.01935479 | 0.138387097 | 999.9000244 |
| 9    | 91.4     | 44.6     | 71.4799998  | 0.005666666 | 999.9000244 |
| 10   | 84.9     | 30.2     | 54.06129074 | 0.008709677 | 999.9000244 |
| 11   | 64.4     | 6.8      | 42.5399999  | 0.043999998 | 999.9000244 |
| 12   | 50       | 3.2      | 24.37096768 | 0           | 999.9000244 |