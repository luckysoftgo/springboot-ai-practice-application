-- 1.高校信息表
DROP TABLE IF EXISTS college_info;
CREATE TABLE college_info (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  college_name varchar(100) NOT NULL COMMENT '院校名称',
  province_name varchar(50) NOT NULL COMMENT '省份',
  city_name varchar(50) NOT NULL COMMENT '城市',
  school_type varchar(30) DEFAULT NULL COMMENT '公办/民办/双一流/985/211',
  nature_type varchar(30) DEFAULT NULL COMMENT '综合/理工/医药/师范',
  avg_tuition int DEFAULT NULL COMMENT '平均学费',
  college_desc text COMMENT '院校简介',
  deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院校信息表';

-- 2.专业信息表
DROP TABLE IF EXISTS major_info;
CREATE TABLE major_info (
    id INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    major_name VARCHAR(100) NOT NULL COMMENT '专业名称',
    major_category VARCHAR(50) COMMENT '学科门类, 如: 工学, 理学',
    major_desc TEXT COMMENT '专业简介',
    deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业信息表';

-- 3.学生信息表
DROP TABLE IF EXISTS student_info;
CREATE TABLE student_info (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  student_name varchar(50) DEFAULT NULL COMMENT '考生姓名',
  student_score int NOT NULL COMMENT '高考总分',
  student_rank int NOT NULL COMMENT '全省位次',
  subject_phone varchar(100) NOT NULL COMMENT '联系方式',
  subject_combination varchar(100) NOT NULL COMMENT '选科组合',
  target_city varchar(200) DEFAULT NULL COMMENT '意向城市',
  target_major varchar(500) DEFAULT NULL COMMENT '意向专业大类',
  student_budget int DEFAULT NULL COMMENT '学费预算上限',
  student_desc text COMMENT '院校简介',
  deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考生基础信息';

-- 4. 历年录取数据表
DROP TABLE IF EXISTS admission_info;
CREATE TABLE admission_info (
    id INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    college_id INT NOT NULL COMMENT '学校ID',
    major_id INT NOT NULL COMMENT '专业ID',
    admission_year YEAR NOT NULL COMMENT '录取年份',
    province VARCHAR(20) NOT NULL COMMENT '生源省份',
    enrollment_plan INT COMMENT '招生计划人数',
    max_score INT COMMENT '最高录取分',
    min_score INT COMMENT '最低录取分',
    avg_score DECIMAL(5,2) COMMENT '平均录取分',
    ranking INT COMMENT '最低录取位次',
    deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='历年录取数据表';

-- 5.专业历年分数线
DROP TABLE IF EXISTS major_score;
CREATE TABLE major_score (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  college_id bigint NOT NULL COMMENT '关联院校ID',
  major_name varchar(100) NOT NULL COMMENT '专业名称',
  major_category varchar(50) NOT NULL COMMENT '专业大类',
  subject_require varchar(200) NOT NULL COMMENT '选科要求',
  admission_year int NOT NULL COMMENT '招生年份',
  min_score int NOT NULL COMMENT '最低录取分',
  min_rank int NOT NULL COMMENT '最低录取位次',
  plan_num int DEFAULT NULL COMMENT '招生计划人数',
  tuition int DEFAULT NULL COMMENT '专业学费',
  deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业历年分数线';

-- 6.AI对话历史
DROP TABLE IF EXISTS chat_history;
CREATE TABLE chat_history (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  student_id bigint NOT NULL COMMENT '考生ID',
  user_question text NOT NULL COMMENT '用户提问',
  ai_answer text NOT NULL COMMENT 'AI回复',
  reference_doc text COMMENT '引用知识库片段',
  deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI对话历史';

-- 7.知识库文档元数据
DROP TABLE IF EXISTS knowledge_doc;
CREATE TABLE knowledge_doc (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  doc_title varchar(200) NOT NULL COMMENT '文档标题（政策/招生简章）',
  doc_type varchar(50) NOT NULL COMMENT '文档类型：填报规则/院校简章/专业介绍',
  doc_content text NOT NULL COMMENT '完整原文',
  deleted INT DEFAULT 0 COMMENT '是否删除：1删除，0保留',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库文档元数据';

-- 学校信息
INSERT INTO `college_info` VALUES (1,'西北农林科技大学','陕西','杨凌','985','综合',8000,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (2,'西北大学','陕西','西安','985','综合',8000,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (3,'西北工业大学','陕西','西安','985','综合',8500,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (4,'西安交通大学','陕西','西安','985','综合',9000,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (5,'西安电子科技大学','陕西','西安','985','综合',8000,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (6,'长安大学','陕西','西安','211','综合',8000,'这是个好学校',0,NULL,NULL);
INSERT INTO `college_info` VALUES (7,'陕西师范大学','陕西','西安','211','综合',8000,'这是个好学校',0,NULL,NULL);

-- 学生信息
INSERT INTO `student_info` VALUES (1,'张三', '560', '2599', '13666666666', '211大学/普通本科', '西安', '电子信息化', '11000', '这是个好学生',0,NULL,NULL);
INSERT INTO `student_info` VALUES (2,'李四', '580', '2599', '13666666667', '211大学/普通本科', '西安', 'AI人工智能', '80000', '这是个好学生',0,NULL,NULL);
INSERT INTO `student_info` VALUES (3,'王五', '590', '2398', '13666666668', '211大学/普通本科', '西安', '电子信息化', '10000', '这是个好学生',0,NULL,NULL);
INSERT INTO `student_info` VALUES (4,'赵六', '660', '599', '13666666669', '211大学/985', '西安', '生物医疗', '10000', '这是个好学生',0,NULL,NULL);
INSERT INTO `student_info` VALUES (5,'思琪', '590', '2399', '13666666665', '211大学/普通本科', '西安', 'AI人工智能', '90000', '这是个好学生',0,NULL,NULL);
INSERT INTO `student_info` VALUES (6,'佳欢', '568', '2559', '13666666664', '211大学/普通本科', '西安', '新媒体', '80000', '这是个好学生',0,NULL,NULL);

-- 专业信息
INSERT INTO `major_info` VALUES (1,'AI人工智能', '工学', '工学学科',0,NULL,NULL);
INSERT INTO `major_info` VALUES (2,'生物医疗', '工学', '工学学科',0,NULL,NULL);
INSERT INTO `major_info` VALUES (3,'新媒体', '理学', '理学学科',0,NULL,NULL);
INSERT INTO `major_info` VALUES (4,'大数据', '工学', '工学学科',0,NULL,NULL);
INSERT INTO `major_info` VALUES (5,'生物制药', '理学', '理学学科',0,NULL,NULL);
INSERT INTO `major_info` VALUES (6,'计算机科学与技术', '理学', '理学学科',0,NULL,NULL);

-- 专业录取分数
INSERT INTO `major_score` VALUES (1,'1', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (2,'1', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (3,'1', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (4,'1', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (5,'1', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (6,'1', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (7,'2', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (8,'2', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (9,'2', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (10,'2', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (11,'2', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (12,'2', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (13,'3', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (14,'3', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (15,'3', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (16,'3', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (17,'3', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (18,'3', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (19,'4', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (20,'4', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (21,'4', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (22,'4', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (23,'4', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (24,'4', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (25,'5', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (26,'5', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (27,'5', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (28,'5', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (29,'5', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (30,'5', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (31,'6', 'AI人工智能', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (32,'6', '生物医疗', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (33,'6', '新媒体', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (34,'6', '大数据', '工学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (35,'6', '生物制药', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);
INSERT INTO `major_score` VALUES (36,'6', '计算机科学与技术', '理学', '理科生', '2025', '500', '20000', '300', '9000',0,NULL,NULL);

-- 省份录取情况
INSERT INTO `admission_info` VALUES (1,'1', '1', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (2,'1', '2', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (3,'1', '3', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (4,'1', '4', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (5,'1', '5', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (6,'1', '6', 2025, '陕西', '50', '680', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (7,'2', '1', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (8,'2', '2', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (9,'2', '3', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (10,'2', '4', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (11,'2', '5', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (12,'2', '6', 2025, '陕西', '60', '660', '580', '630', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (13,'3', '1', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (14,'3', '2', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (15,'3', '3', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (16,'3', '4', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (17,'3', '5', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (18,'3', '6', 2025, '陕西', '70', '680', '590', '620', '25000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (19,'4', '1', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (20,'4', '1', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (21,'4', '1', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (22,'4', '2', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (23,'4', '3', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (24,'4', '4', 2025, '陕西', '80', '740', '680', '690', '4000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (25,'5', '1', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (26,'5', '2', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (27,'5', '3', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (28,'5', '4', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (29,'5', '5', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (30,'5', '6', 2025, '陕西', '90', '580', '480', '530', '65000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (31,'6', '1', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (32,'6', '2', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (33,'6', '3', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (34,'6', '4', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (35,'6', '5', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);
INSERT INTO `admission_info` VALUES (36,'6', '6', 2025, '陕西', '100', '590', '480', '520', '45000',0,NULL,NULL);












