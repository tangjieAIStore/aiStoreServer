CREATE TABLE `token_count` (
	`id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
	`user_id` int NOT NULL COMMENT '用户ID',
	`model_id` int NOT NULL COMMENT '模型ID',
	`application_id` int NOT NULL COMMENT '应用id',
	`token_count` int NOT NULL COMMENT '使用量',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`tenant_id` bigint NOT NULL COMMENT '租户编码',
	`deleted` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '0表示正常状态；非0为删除的时间（精确到秒）'
) ENGINE = InnoDB;
CREATE TABLE `model` (
	`id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
	`user_id` int NOT NULL COMMENT '用户ID',
	`frequency_penalty` double DEFAULT NULL COMMENT '罕见词汇',
	`maintain_context` int NOT NULL COMMENT '是否保持上下文',
	`max_responses` bigint DEFAULT NULL COMMENT '最大响应数',
	`model` varchar(1000) DEFAULT NULL COMMENT '选择的模型',
	`name` varchar(255) DEFAULT NULL COMMENT '名称，Name',
	`output_format` varchar(50) DEFAULT NULL COMMENT '输出格式',
	`presence_penalty` double DEFAULT NULL COMMENT '重复词汇',
	`role` varchar(500) DEFAULT NULL COMMENT '角色',
	`temperature` double DEFAULT NULL COMMENT '温度，Temperature',
	`top_p` double DEFAULT NULL COMMENT '多样性及保真度',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`tenant_id` bigint NOT NULL COMMENT '租户编码',
	`deleted` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '0表示正常状态；非0为删除的时间（精确到秒）'
) ENGINE = InnoDB;
CREATE TABLE `knowledge` (
	`id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
	`user_id` int NOT NULL COMMENT '用户ID',
	`model_id` int NOT NULL COMMENT '模型ID',
	`file_url` varchar(255) DEFAULT NULL COMMENT '文件地址',
	`file_name` varchar(100) DEFAULT NULL COMMENT '文件名称',
	`file_status` varchar(60) DEFAULT NULL COMMENT '文件状态',
	`word_count` bigint DEFAULT NULL COMMENT '字数',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`tenant_id` bigint NOT NULL COMMENT '租户编码',
	`deleted` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '0表示正常状态；非0为删除的时间（精确到秒）'
) ENGINE = InnoDB;
CREATE TABLE `application` (
	`id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
	`user_id` int NOT NULL COMMENT '用户ID',
	`model_id` int NOT NULL COMMENT '模型ID',
	`application_icon` varchar(255) DEFAULT NULL COMMENT '应用图标',
	`application_name` varchar(255) DEFAULT NULL COMMENT '应用名称',
	`application_description` varchar(255) DEFAULT NULL COMMENT '应用描述',
	`application_type` varchar(50) DEFAULT NULL COMMENT '应用类型',
	`first_level_category_name` varchar(255) DEFAULT NULL COMMENT '一级类别名称',
	`guiding` text COMMENT '引导语',
	`img_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
	`is_included` int NOT NULL COMMENT '是否收录',
	`second_level_category_name` varchar(255) DEFAULT NULL COMMENT '二级类别名称',
	`training_model` varchar(100) DEFAULT NULL COMMENT '训练的模型',
	`update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`tenant_id` bigint NOT NULL COMMENT '租户编码',
	`deleted` int UNSIGNED NOT NULL DEFAULT '0' COMMENT '0表示正常状态；非0为删除的时间（精确到秒）'
) ENGINE = InnoDB;
