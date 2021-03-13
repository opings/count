-- subject相关
CREATE TABLE deep_comment.`comment_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `out_biz_type` varchar(32) NOT NULL COMMENT '外部业务类型',
  `out_biz_no` varchar(64) NOT NULL COMMENT '外部业务单据号',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `comment_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评论数',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`),
  UNIQUE KEY `uk_out_biz` (`out_biz_type`,`out_biz_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='评论主体表';

CREATE TABLE deep_comment.`count_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `out_biz_type` varchar(64) DEFAULT NULL,
  `out_biz_no` varchar(128) DEFAULT NULL COMMENT '外部业务单据号',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `split_subject_id` varchar(256) DEFAULT NULL COMMENT '分裂的主体id列表,用,隔离',
  `split_subject_id_count` varchar(32) DEFAULT NULL COMMENT '分裂的主体id列表数量',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`),
  UNIQUE KEY `uk_out_biz_split_subject_id` (`out_biz_type`,`out_biz_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='计数主体表';

CREATE TABLE deep_comment.`id_segment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `biz_tag` varchar(50) NOT NULL COMMENT '业务标识',
  `max_id` bigint(20) NOT NULL COMMENT '分配的id号段的最大值',
  `p_step` bigint(20) NOT NULL COMMENT '步长',
  `last_update_time` datetime NOT NULL COMMENT '上一次最后更新时间',
  `current_update_time` datetime NOT NULL COMMENT '当前更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_biz_tag` (`biz_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='号段存储表';

CREATE TABLE deep_comment.`light_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `out_biz_type` varchar(32) NOT NULL COMMENT '外部业务类型',
  `out_biz_no` varchar(32) NOT NULL COMMENT '外部业务单据号',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`),
  UNIQUE KEY `uk_out_biz` (`out_biz_type`,`out_biz_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='点亮主体表';

CREATE TABLE deep_comment.`black_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `out_biz_type` varchar(32) NOT NULL COMMENT '外部业务类型',
  `out_biz_no` varchar(32) NOT NULL COMMENT '外部业务单据号',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`),
  UNIQUE KEY `uk_out_biz` (`out_biz_type`,`out_biz_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='点灭主体表';

CREATE TABLE deep_comment.`score_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(20) NOT NULL COMMENT '主体id',
  `out_biz_no` varchar(64) NOT NULL COMMENT '外部业务id',
  `out_biz_type` varchar(32) NOT NULL COMMENT '外部业务类型',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`),
  UNIQUE KEY `uk_out_biz` (`out_biz_type`,`out_biz_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='打分主体表';

CREATE TABLE deep_comment.`split_count_subject` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `parent_subject_id` varchar(32) NOT NULL COMMENT '父主体id',
  `count_value` int(10) NOT NULL DEFAULT '0' COMMENT '计数值',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分裂计数主体表';

insert into deep_comment.id_segment (biz_tag, max_id, p_step, last_update_time, current_update_time) values ('COMMENT', 1000, 1000, '2020-11-30 00:00:00', '2020-11-30 00:00:00');
insert into deep_comment.id_segment (biz_tag, max_id, p_step, last_update_time, current_update_time) values ('SCORE', 1000, 1000, '2020-11-30 00:00:00', '2020-11-30 00:00:00');
insert into deep_comment.id_segment (biz_tag, max_id, p_step, last_update_time, current_update_time) values ('LIGHT', 1000, 1000, '2020-11-30 00:00:00', '2020-11-30 00:00:00');
insert into deep_comment.id_segment (biz_tag, max_id, p_step, last_update_time, current_update_time) values ('BLACK', 1000, 1000, '2020-11-30 00:00:00', '2020-11-30 00:00:00');
insert into deep_comment.id_segment (biz_tag, max_id, p_step, last_update_time, current_update_time) values ('COUNTER', 1000, 1000, '2020-11-30 00:00:00', '2020-11-30 00:00:00');

-- 评论库
CREATE TABLE deep_comment_[1-8].`comment_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `comment_id` varchar(32) NOT NULL COMMENT '评论id',
  `user_id` bigint(32) NOT NULL COMMENT '用户id',
  `parent_comment_id` varchar(32) DEFAULT NULL COMMENT '父评论id',
  `comment_content` varchar(1200) NOT NULL COMMENT '评论内容',
  `comment_content_extend` varchar(1024) DEFAULT NULL COMMENT '评论内容扩展',
  `light_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评论点亮数',
  `black_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评论点灭数',
  `reply_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评论回复数。评论有子评论',
  `machine_audit_status` varchar(32) DEFAULT NULL COMMENT '机器审核状态',
  `manual_audit_status` varchar(32) DEFAULT NULL COMMENT '人工审核状态',
  `machine_audit_request_id` varchar(64) DEFAULT NULL COMMENT '机器审核返回的requestId',
  `final_audit_status` varchar(32) NOT NULL COMMENT '最终审核状态',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `publish_time` bigint(20) unsigned NOT NULL COMMENT '发布时间',
  `audit_user_name` varchar(32) DEFAULT NULL COMMENT '审核人姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_id` (`comment_id`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_subject_id` (`subject_id`,`user_id`,`publish_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='评论记录表';

CREATE TABLE deep_comment_[1-8].`user_comment_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `comment_id` varchar(32) NOT NULL COMMENT '评论id',
  `user_id` bigint(32) NOT NULL COMMENT '用户id',
  `parent_comment_id` varchar(32) DEFAULT NULL COMMENT '父评论id',
  `machine_audit_status` varchar(32) DEFAULT NULL COMMENT '机器审核状态',
  `manual_audit_status` varchar(32) DEFAULT NULL COMMENT '人工审核状态',
  `final_audit_status` varchar(32) NOT NULL COMMENT '最终审核状态',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `publish_time` bigint(20) unsigned NOT NULL COMMENT '发布时间',
  `audit_user_name` varchar(32) DEFAULT NULL COMMENT '审核人姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_comment_id` (`user_id`,`comment_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户维度评论记录表';

-- 点亮库
CREATE TABLE deep_light_[1-8].`light_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `light_record_id` varchar(32) NOT NULL COMMENT '点亮记录id',
  `light_status` varchar(32) NOT NULL COMMENT 'LIGHT 点亮 CANCEL_LIGHT 取消点亮',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_light_record_id` (`light_record_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='点亮记录表';

CREATE TABLE deep_light_[1-8].`user_light_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `light_record_id` varchar(32) NOT NULL COMMENT '点亮记录id',
  `light_status` varchar(32) NOT NULL COMMENT 'LIGHT 点亮 CANCEL_LIGHT 取消点亮',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_light_record_id` (`light_record_id`),
  UNIQUE KEY `uk_user_subject_id` (`user_id`,`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户点亮记录表';

-- 点灭库
CREATE TABLE deep_black_[1-8].`black_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `black_record_id` varchar(32) NOT NULL COMMENT '点灭记录id',
  `black_status` varchar(32) NOT NULL COMMENT 'BLACK 点灭 CANCEL_BLACK 取消点灭',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_black_record_id` (`black_record_id`),
  KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='点灭记录表';

CREATE TABLE deep_black_[1-8].`user_black_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `black_record_id` varchar(32) NOT NULL COMMENT '点灭记录id',
  `black_status` varchar(32) NOT NULL COMMENT 'BLACK 点灭 CANCEL_BLACK 取消点灭',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_black_record_id` (`black_record_id`),
  UNIQUE KEY `uk_user_subject_id` (`user_id`,`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户点灭记录表';

-- 评分库
CREATE TABLE deep_score_[1-8].`score_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `score_record_id` varchar(32) NOT NULL COMMENT '打分记录id',
  `user_id` bigint(32) NOT NULL COMMENT '用户id',
  `score` int(10) unsigned NOT NULL COMMENT '分值',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_score_record_id` (`score_record_id`),
  KEY `idx_subject_id` (`subject_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打分记录表';

CREATE TABLE deep_score_[1-8].`user_score_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `score_record_id` varchar(32) NOT NULL COMMENT '打分记录id',
  `user_id` bigint(32) NOT NULL COMMENT '用户id',
  `score` int(10) unsigned NOT NULL COMMENT '分值',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id_subject` (`user_id`,`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打分记录表用户反向索引表';

-- 计数库
CREATE TABLE deep_count_[1-8].`count_record_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `count_record_id` varchar(32) NOT NULL COMMENT '计数流水id',
  `count_value` int(10) NOT NULL DEFAULT '0' COMMENT '计数值',
  `idempotent_no` varchar(32) NOT NULL COMMENT '幂等单据号',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_count_record_id` (`count_record_id`),
  UNIQUE KEY `uk_subject_id_idempotent_no` (`subject_id`,`idempotent_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='计数流水表';

CREATE TABLE deep_count_[1-8].`split_count_subject_[1-100]` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` varchar(32) NOT NULL COMMENT '主体id',
  `parent_subject_id` varchar(32) NOT NULL COMMENT '父主体id',
  `count_value` int(10) NOT NULL DEFAULT '0' COMMENT '计数值',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_id` (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分裂计数主体表';

-- feature/2020_12_21
alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_msg_id varchar(64) null comment '融云消息id' after comment_id ;
update deep_comment_[1-8].comment_record_[1-100] set rongcloud_msg_id = comment_id where rongcloud_msg_id is null;
alter table deep_comment_[1-8].comment_record_[1-100] modify rongcloud_msg_id varchar(64) not null comment '融云消息id';
alter table deep_comment_[1-8].comment_record_[1-100] add unique index uk_rongcloud_msg_id(rongcloud_msg_id);

alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_msg_time bigint(20) null comment '融云消息时间' after rongcloud_msg_id ;
update deep_comment_[1-8].comment_record_[1-100] set rongcloud_msg_time = publish_time where rongcloud_msg_time is null;
alter table deep_comment_[1-8].comment_record_[1-100] modify rongcloud_msg_time bigint(20) not null comment '融云消息时间';

-- feature/2021_01_04
alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_room_msg_id varchar(64) null comment '融云聊天室消息id' after rongcloud_msg_time ;
alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_room_msg_time bigint(20) null comment '融云聊天室消息时间' after rongcloud_room_msg_id ;

-- feature/2021_01_18
alter table deep_comment_[1-8].comment_record_[1-100] add column chat_group_topic_id varchar(64) not null default '0' comment '团话题id' after rongcloud_room_msg_time ;
alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_topic_room_msg_id varchar(64) comment '团话题融云聊天室消息id' after chat_group_topic_id ;
alter table deep_comment_[1-8].comment_record_[1-100] add column rongcloud_topic_room_msg_time bigint(20) comment '团话题融云聊天室消息时间' after rongcloud_topic_room_msg_id ;
alter table deep_comment_[1-8].comment_record_[1-100] add index `idx_chat_group_topic_id` (`chat_group_topic_id`)  ;




