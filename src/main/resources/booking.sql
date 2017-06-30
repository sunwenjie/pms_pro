# 给[广告主]添加[货币类型、是否跨区域]字段
ALTER TABLE `clients`
ADD COLUMN `currency_id int(11) DEFAULT NULL AFTER `industry_id`；

# 给[广告主]添加[状态]字段
ALTER TABLE `clients`
ADD COLUMN `client_status` varchar(20) DEFAULT 'Active',

# 创建[货币类型]表
CREATE TABLE `currencies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `remarks` text,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

# 插入[货币类型]初始数据
INSERT INTO `currencies` (`id`, `name`, `remarks`, `created_at`, `updated_at`)
VALUES
	(1, 'HKD', NULL, '2011-07-04 10:52:23', '2011-07-04 10:52:23'),
	(2, 'RMB', NULL, '2011-07-04 10:52:23', '2011-07-04 10:52:23'),
	(3, 'USD', NULL, '2011-07-04 10:52:23', '2011-07-04 10:52:23'),
	(4, 'SGD', NULL, '2011-07-04 10:52:23', '2011-07-04 10:52:23'),
	(5, 'TWD', NULL, '2011-07-04 10:52:23', '2011-07-04 10:52:23'),
	(6, 'KRW', '', '2011-07-12 03:29:46', '2011-07-12 03:29:46'),
	(8, 'JPY', '', '2011-07-12 11:08:44', '2011-07-12 11:08:44'),
	(9, 'MYR', NULL, '2012-08-15 06:58:58', '2012-08-15 06:58:58'),
	(10, 'GBP', NULL, '2012-08-15 06:58:58', '2012-08-15 06:58:58'),
	(11, 'EUR', '', '2012-11-29 10:14:25', '2012-11-29 10:14:25'),
	(12, 'AUD', NULL, '2013-07-22 08:12:55', '2013-07-22 08:12:55'),
	(13, 'THB', '', '2015-02-26 07:04:40', '2015-02-26 07:04:40'),
	(14, 'RUB', '', '2015-02-26 07:04:51', '2015-02-26 07:04:51'),
	(15, 'IDR', '', '2015-02-26 07:05:01', '2015-02-26 07:05:01');
	
# 创建[广告主联系人]表
CREATE TABLE `client_contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) DEFAULT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `is_delete` smallint(1) DEFAULT 1,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 加字段广告形式
alter table business_opportunity_products
add column `product_category_id` int(11) DEFAULT NULL after `product_id`;


####### mysql  functions#######
DELIMITER $$
CREATE  FUNCTION `getNamesByOpportunityId` (opportunityId int(11)) RETURNS varchar(255)
begin
			DECLARE returnVal VARCHAR(255) DEFAULT '';
				select group_concat(name) into returnVal  from xmo.users where FIND_IN_SET (id,
					(select concat(owner_sale, IF(cooperate_sales is null,'',concat(',',cooperate_sales)) ) from business_opportunities 
					where id = opportunityId)
				);
			RETURN returnVal;
			END$$
			
			
CREATE DEFINER=`xmo_prd`@`%` FUNCTION `getGPByProductId`(id int(11)) RETURNS varchar(255) CHARSET utf8
begin
			DECLARE returnVal float DEFAULT 50;				
				select IFNULL(c.gp,50) into returnVal from products a left join product_categories b on a.product_type = b.value 
				left join product_category_gp c on b.id = c.category_id where a.id = id;
			RETURN returnVal;
			END;
			
CREATE DEFINER=`xmo_prd`@`%` FUNCTION `getLocalByOpportunityId`(id int(11)) RETURNS varchar(255) CHARSET utf8
begin
			DECLARE returnVal VARCHAR(255) DEFAULT '';
				select b.bu into returnVal from business_opportunities a left join xmo.users b on a.owner_sale = b.id where a.id = id;
			RETURN returnVal;
			END;
			
CREATE DEFINER=`xmo_prd`@`%` FUNCTION `getLocalByOrderId`(id int(11)) RETURNS varchar(255) CHARSET utf8
begin
			DECLARE returnVal VARCHAR(255) DEFAULT '';				
				select group_concat(c.bu) into returnVal from orders a left join share_orders b on a.id=b.order_id left join xmo.users c on b.share_id=c.id where a.id=id;
			RETURN returnVal;
			END;
			
CREATE DEFINER=`xmo_prd`@`%` FUNCTION `queryChildrenDataSharing`(userId INT) RETURNS varchar(4000) CHARSET utf8
BEGIN
DECLARE sTemp VARCHAR(4000);
DECLARE sTempChd VARCHAR(4000);
SET sTemp = '$';
SET sTempChd = cast(userId as char);

WHILE sTempChd is not NULL DO
SET sTemp = CONCAT(sTemp,',',sTempChd);
SELECT 
    GROUP_CONCAT(user_id)
INTO sTempChd FROM
    data_sharing
WHERE
    FIND_IN_SET(parent_id, sTempChd) > 0;
END WHILE;

RETURN sTemp;
END;


DELIMITER $$  
CREATE  FUNCTION `split`(  
f_string varchar(1000),f_delimiter varchar(100),f_order int) RETURNS varchar(1000) CHARSET utf8  
BEGIN  
  declare result varchar(1000) default '';  
  set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),reverse(f_delimiter),1));  
  return result;  
END$$  
DELIMITER ;

## 判断合作销售
DELIMITER $$  
CREATE  FUNCTION `is_mixed`(  
splitStr varchar(1000),delimiter varchar(100),targetSet varchar(2000)) RETURNS int(11)
BEGIN  
  DECLARE count int(11);
	DECLARE i int(11);
	DECLARE result int(11) default 0;
	SET count = split_count(splitStr,delimiter);
	SET i = 1;
	while i <= count and result=0  do
		select FIND_IN_SET(split(splitStr,delimiter,i) , targetSet) into result;
		SET i = i+1;
	end while;
	return result; 
END$$  
DELIMITER ;

CREATE TABLE `sales_booking_production`.`reports_logs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `file_path` VARCHAR(255) NULL,
  `receives` VARCHAR(255) NULL,
  `send_time` DATETIME NULL,
  `status` INT NULL,
  PRIMARY KEY (`id`));

##上周数据
CREATE TABLE `last_week_data` (
  `id` int(11) NOT NULL DEFAULT '0',
  `client_id` int(11) DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 NOT NULL,
  `clientname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget` decimal(10,2) DEFAULT NULL,
  `budget_currency` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget_ratio` decimal(18,2) DEFAULT NULL,
  `ad_platform` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `ad_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `sale_model` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `bu` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `group_names` text CHARACTER SET utf8,
  `area` text CHARACTER SET utf8,
  `created_user` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `is_standard` tinyint(1) DEFAULT NULL,
  `free_tag` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gp_evaluate` float DEFAULT NULL,
  `rebate` decimal(6,2) DEFAULT NULL,
  `market_cost` decimal(6,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `ending_date` date DEFAULT NULL,
  `total_day` int(11) DEFAULT NULL,
  `current_quarter_have_day` int(11) DEFAULT NULL,
  `shang_quarter_have_day` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

##本周数据
CREATE TABLE `current_week_data` (
  `id` int(11) NOT NULL DEFAULT '0',
  `client_id` int(11) DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 NOT NULL,
  `clientname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget` decimal(10,2) DEFAULT NULL,
  `budget_currency` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget_ratio` decimal(18,2) DEFAULT NULL,
  `ad_platform` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `ad_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `sale_model` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `bu` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `group_names` text CHARACTER SET utf8,
  `area` text CHARACTER SET utf8,
  `created_user` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `is_standard` tinyint(1) DEFAULT NULL,
  `free_tag` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gp_evaluate` float DEFAULT NULL,
  `rebate` decimal(6,2) DEFAULT NULL,
  `market_cost` decimal(6,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `ending_date` date DEFAULT NULL,
  `total_day` int(11) DEFAULT NULL,
  `current_quarter_have_day` int(11) DEFAULT NULL,
  `shang_quarter_have_day` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


#本周数据包含被删除的数据
CREATE TABLE `current_week_all_data` (
  `id` int(11) NOT NULL DEFAULT '0',
  `client_id` int(11) DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 NOT NULL,
  `clientname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget` decimal(10,2) DEFAULT NULL,
  `budget_currency` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `budget_ratio` decimal(18,2) DEFAULT NULL,
  `ad_platform` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `ad_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `sale_model` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `cost` decimal(6,2) DEFAULT NULL,
  `bu` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `group_names` text CHARACTER SET utf8,
  `area` text CHARACTER SET utf8,
  `created_user` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `is_standard` tinyint(1) DEFAULT NULL,
  `free_tag` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gp_evaluate` float DEFAULT NULL,
  `rebate` decimal(6,2) DEFAULT NULL,
  `market_cost` decimal(6,2) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `ending_date` date DEFAULT NULL,
  `total_day` int(11) DEFAULT NULL,
  `current_quarter_have_day` int(11) DEFAULT NULL,
  `shang_quarter_have_day` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;





create view  v_only_order_last_week as
select l.id as l_id,l.client_id as l_client_id,l.name as l_name,l.code as l_code,l.clientname as l_clientname,l.title as l_title,l.start_date as l_start_date,l.ending_date as l_ending_date,l.total_day as l_total_day,l.current_quarter_have_day as l_current_quarter_have_day,l.shang_quarter_have_day as l_shang_quarter_have_day,l.budget as l_budget,l.budget_currency as l_budget_currency, count(1) as l_count from last_week_data l group by l.id,l.client_id,l.name,l.code,l.clientname,l.title,l.start_date,l.ending_date,l.total_day,l.current_quarter_have_day,l.shang_quarter_have_day,l.budget,l.budget_currency

create view  v_only_order_current_week as
select c.id as c_id,c.client_id as c_client_id,c.name as c_name,c.code as c_code,c.clientname as c_clientname,c.title as c_title,c.start_date as c_start_date,c.ending_date as c_ending_date,c.total_day as c_total_day,c.current_quarter_have_day as c_current_quarter_have_day,c.shang_quarter_have_day as  c_shang_quarter_have_day,c.budget as c_budget,c.budget_currency as c_budget_currency,count(1) as c_count from current_week_data c group by c.id,c.client_id,c.name,c.code,c.clientname,c.title,c.start_date,c.ending_date,c.total_day,c.current_quarter_have_day,c.shang_quarter_have_day,c.budget,c.budget_currency


create view  v_only_order_all as
select t1.*,t2.* from v_only_order_last_week t1 left join v_only_order_current_week t2 on t1.l_id = t2.c_id
union
select t1.*,t2.* from v_only_order_last_week t1 right join v_only_order_current_week  t2 on t1.l_id = t2.c_id

##订单改变情况视图
create  view v_change_order
as
select t3.l_id,t3.c_id,
case when isNull(t3.l_id) then 1 else 0 end as is_add,
case when isNull(t3.c_id) then 1 else 0 end as is_delete,
case when t3.c_title like '%Demo%' or t3.c_title like '%测试%' then 1 else 0 end as is_test,
case when t3.l_id = t3.c_id and
          (t3.l_client_id != t3.c_client_id
          or t3.l_name != t3.c_name
          or t3.l_clientname != t3.c_clientname
          or t3.l_title != t3.c_title
          or t3.l_start_date != t3.c_start_date
          or t3.l_ending_date != t3.c_ending_date
          or t3.l_total_day != t3.c_total_day
          or t3.l_current_quarter_have_day != t3.c_current_quarter_have_day
          or t3.l_shang_quarter_have_day != t3.c_shang_quarter_have_day
          or t3.l_budget != t3.c_budget
          or t3.l_budget_currency != t3.c_budget_currency
          ) then 1 else 0 end as is_update

 from v_only_order_all t3


