-- 作者表
DROP TABLE IF EXISTS reservation_info;
CREATE TABLE reservation_info (
    id                              bigint auto_increment not null comment '主键ID',
    reservation_name                varchar(50) not null comment '考生姓名',
    reservation_gender              varchar(2)  not null comment '考生性别',
    reservation_phone               varchar(20) not null comment '考生手机号',
    communication_time              datetime    not null comment '沟通时间',
    reservation_province            varchar(32) not null comment '考生所处的省份',
    estimated_score                 int         not null comment '考生预估分数',
    reservation_desc                text comment '考生所处的省份',
    PRIMARY KEY (id),
    INDEX idx_reservation_phone (reservation_phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '预定信息表';