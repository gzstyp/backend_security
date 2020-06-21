/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.3.66
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 192.168.3.66:3306
 Source Schema         : admin_security

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 21/06/2020 16:43:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `name` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标识(权限标识)',
  `category` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '权限类型(1查询;2编辑;3删除;4添加)',
  `icon_style` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'menu-icon fa fa-caret-right' COMMENT '元素span后面的i元素样式',
  `pid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '88888888888888888888888888888888' COMMENT '父级id',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'javascript:;' COMMENT 'url路径',
  `subset` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否含子节点(1含子节点;0不含)',
  `type` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '菜单类型(1导航菜单;2普通按钮;3行内按钮)',
  `relation` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '88888888888888888888888888888888' COMMENT '菜单层次级别关系(限制最多就8级导航菜单)',
  `order_by` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_menu_permission`(`permission`) USING BTREE,
  UNIQUE INDEX `index_menu_url`(`url`) USING BTREE,
  UNIQUE INDEX `index_menu_relation`(`relation`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0000000006a9099300000000100d10cd', '获取数据列表[搜索]', 'menu:btn:listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@0000000006a9099300000000100d10cd', 1);
INSERT INTO `sys_menu` VALUES ('000000000ba93c43ffffffffe5c59508', '添加', 'menu:btn:add', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@000000000ba93c43ffffffffe5c59508', 2);
INSERT INTO `sys_menu` VALUES ('000000002d8402d60000000072f7dea2', '获取数据列表[搜索]', 'user:btn:listData', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000002d8402d60000000072f7dea2', 1);
INSERT INTO `sys_menu` VALUES ('000000002e18cf48ffffffffab242e16', '角色菜单(保存菜单)', 'role:row:saveRoleMenu', 4, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/saveRoleMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@000000002e18cf48ffffffffab242e16', 8);
INSERT INTO `sys_menu` VALUES ('0000000035fbca03000000003a33d6b7', '删除(批量删除)', 'user:btn:delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/delByKeys', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@0000000035fbca03000000003a33d6b7', 5);
INSERT INTO `sys_menu` VALUES ('000000003a56d4fb0000000037016444', '角色(保存角色)', 'user:btn_row:saveAllotRole', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/saveAllotRole', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000003a56d4fb0000000037016444', 9);
INSERT INTO `sys_menu` VALUES ('0000000058e2ec83000000002f978e03', '编辑', 'menu:row:edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@0000000058e2ec83000000002f978e03', 4);
INSERT INTO `sys_menu` VALUES ('000000005c41138100000000288a6b45', '删除', 'menu:row:delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@000000005c41138100000000288a6b45', 3);
INSERT INTO `sys_menu` VALUES ('000000007330a2a9ffffffff98cebe66', '添加', 'user:btn:add', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007330a2a9ffffffff98cebe66', 2);
INSERT INTO `sys_menu` VALUES ('0000000077224459ffffffffc9d752ca', '启用禁用', 'user:row:editEnabled', 2, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/editEnabled', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@0000000077224459ffffffffc9d752ca', 10);
INSERT INTO `sys_menu` VALUES ('000000007937b8de0000000034ef3b70', '清空菜单', 'role:row:delEmptyMenu', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delEmptyMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@000000007937b8de0000000034ef3b70', 6);
INSERT INTO `sys_menu` VALUES ('000000007a3bebc1ffffffffc0d19222', '角色(获取角色)', 'user:btn_row:getAllotRole', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getAllotRole', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007a3bebc1ffffffffc0d19222', 8);
INSERT INTO `sys_menu` VALUES ('000000007ea75783000000004607fdec', '编辑', 'user:row:edit', 2, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007ea75783000000004607fdec', 3);
INSERT INTO `sys_menu` VALUES ('111fffffbd911aa0ffffffffd5637fff', '角色权限', 'page:sys_role', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_role.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff', 3);
INSERT INTO `sys_menu` VALUES ('ffffffff87212d7d000000005fbd6e98', '添加', 'role:btn:add', 4, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff87212d7d000000005fbd6e98', 2);
INSERT INTO `sys_menu` VALUES ('ffffffff8b559df0ffffffff834bba04', '系统设置', 'page:sys_setting', 1, 'menu-icon fa fa-cog', '88888888888888888888888888888888', 'sys_setting', 1, 1, 'ffffffff8b559df0ffffffff834bba04', 1);
INSERT INTO `sys_menu` VALUES ('ffffffff8c468a55ffffffff8f59d635', '用户账号', 'page:sys_user', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_user.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635', 2);
INSERT INTO `sys_menu` VALUES ('ffffffff8e6151ff0000000015f6e145', '删除(行内删除)', 'role:row:delById', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff8e6151ff0000000015f6e145', 4);
INSERT INTO `sys_menu` VALUES ('ffffffff93f9086effffffffb4a239cd', '删除(批量删除)', 'role:btn:delByKeys', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delByKeys', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff93f9086effffffffb4a239cd', 5);
INSERT INTO `sys_menu` VALUES ('ffffffff9db05c1000000000002e5179', '私有菜单(获取菜单)', 'user:row:getOwnMenu', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getOwnMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffff9db05c1000000000002e5179', 6);
INSERT INTO `sys_menu` VALUES ('ffffffff9dcbdcebffffffff99573f80', '私有菜单(保存菜单)', 'user:row:saveOwnMenu', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/saveOwnMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffff9dcbdcebffffffff99573f80', 7);
INSERT INTO `sys_menu` VALUES ('ffffffffb71281a2ffffffffd931eb95', '删除(行内删除)', 'user:row:delById', 3, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffb71281a2ffffffffd931eb95', 4);
INSERT INTO `sys_menu` VALUES ('ffffffffc43097b8000000003a72e62f', '权限菜单', 'user:row:getMenuData', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getMenuData', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffc43097b8000000003a72e62f', 11);
INSERT INTO `sys_menu` VALUES ('ffffffffdae3cd4affffffff9894b623', '角色菜单(获取菜单)', 'role:row:getRoleMenu', 1, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/getRoleMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffffdae3cd4affffffff9894b623', 7);
INSERT INTO `sys_menu` VALUES ('ffffffffddf9f51ffffffffff6157ca3', '根据id获取详情', 'menu:row:queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/queryById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@ffffffffddf9f51ffffffffff6157ca3', 6);
INSERT INTO `sys_menu` VALUES ('ffffffffdfaf333c000000001760f4a5', '编辑', 'role:row:edit', 2, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffffdfaf333c000000001760f4a5', 3);
INSERT INTO `sys_menu` VALUES ('ffffffffe56e8ef0ffffffff912af74a', '菜单管理', 'page:sys_menu', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_menu.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a', 1);
INSERT INTO `sys_menu` VALUES ('fffffffff051bf04ffffffffa2ae3c8e', '获取树形菜单', 'menu:btn:queryTreeMenu', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/queryTreeMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@fffffffff051bf04ffffffffa2ae3c8e', 5);
INSERT INTO `sys_menu` VALUES ('fffffffffaad7649ffffffffe7c3d6a6', '获取数据列表[搜索]', 'role:btn:listData', 1, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@fffffffffaad7649ffffffffe7c3d6a6', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_flag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_role_name`(`role_name`) USING BTREE,
  UNIQUE INDEX `index_role_flag`(`role_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('000000003035dacf000000000c087217', '游客', 'ROLE_GUEST');
INSERT INTO `sys_role` VALUES ('ffffffffa04b32a6ffffffffba40a72e', '系统管理员', 'ROLE_ADMINISTRATORS');
INSERT INTO `sys_role` VALUES ('ffffffffdc2ebe6a000000006b149e18', '操作员', 'ROLE_OPERATOR');
INSERT INTO `sys_role` VALUES ('ffffffffde90da3fffffffffdef7150f', '管理员', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `role_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色的id(sys_role.kid)',
  `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单的id(sys_menu.kid)',
  PRIMARY KEY (`kid`) USING BTREE,
  INDEX `index_role_menu`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限(菜单)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('000000007f13261d000000005501c6d7', 'ffffffffde90da3fffffffffdef7150f', '0000000006a9099300000000100d10cd');
INSERT INTO `sys_role_menu` VALUES ('ffffffffcab349520000000001497712', 'ffffffffde90da3fffffffffdef7150f', '000000000ba93c43ffffffffe5c59508');
INSERT INTO `sys_role_menu` VALUES ('0000000015f6a64cffffffffee690a58', 'ffffffffde90da3fffffffffdef7150f', '000000002d8402d60000000072f7dea2');
INSERT INTO `sys_role_menu` VALUES ('0000000051b979070000000009b1da20', 'ffffffffde90da3fffffffffdef7150f', '000000002e18cf48ffffffffab242e16');
INSERT INTO `sys_role_menu` VALUES ('000000002e429313ffffffff8924e3aa', 'ffffffffde90da3fffffffffdef7150f', '0000000035fbca03000000003a33d6b7');
INSERT INTO `sys_role_menu` VALUES ('00000000059cb90b0000000016dbd9ae', 'ffffffffde90da3fffffffffdef7150f', '000000003a56d4fb0000000037016444');
INSERT INTO `sys_role_menu` VALUES ('ffffffffaf9dd1f0ffffffff84e6a2b9', 'ffffffffde90da3fffffffffdef7150f', '0000000058e2ec83000000002f978e03');
INSERT INTO `sys_role_menu` VALUES ('000000006c762becffffffff8179e1d6', 'ffffffffde90da3fffffffffdef7150f', '000000005c41138100000000288a6b45');
INSERT INTO `sys_role_menu` VALUES ('ffffffffac3151100000000008dfdeaf', 'ffffffffde90da3fffffffffdef7150f', '000000007330a2a9ffffffff98cebe66');
INSERT INTO `sys_role_menu` VALUES ('0000000066539da5ffffffffc7fc925e', 'ffffffffde90da3fffffffffdef7150f', '0000000077224459ffffffffc9d752ca');
INSERT INTO `sys_role_menu` VALUES ('ffffffffda4a14f80000000033214822', 'ffffffffde90da3fffffffffdef7150f', '000000007937b8de0000000034ef3b70');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa0edaa8affffffffb94715a5', 'ffffffffde90da3fffffffffdef7150f', '000000007a3bebc1ffffffffc0d19222');
INSERT INTO `sys_role_menu` VALUES ('000000007e7260280000000029f56bf3', 'ffffffffde90da3fffffffffdef7150f', '000000007ea75783000000004607fdec');
INSERT INTO `sys_role_menu` VALUES ('0000000075bf785b00000000088f043c', 'ffffffffde90da3fffffffffdef7150f', '111fffffbd911aa0ffffffffd5637fff');
INSERT INTO `sys_role_menu` VALUES ('000000004e5ad468ffffffffc856bd87', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff87212d7d000000005fbd6e98');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd9694ae2ffffffffda58e1e9', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_role_menu` VALUES ('000000001f274cca0000000047ee700a', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8c468a55ffffffff8f59d635');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd1738955000000003a996a36', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8e6151ff0000000015f6e145');
INSERT INTO `sys_role_menu` VALUES ('ffffffff9c4cac760000000022bd5488', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff93f9086effffffffb4a239cd');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc87fe8ffffffffffcfc843df', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff9db05c1000000000002e5179');
INSERT INTO `sys_role_menu` VALUES ('0000000047d80a99ffffffffc205ac44', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff9dcbdcebffffffff99573f80');
INSERT INTO `sys_role_menu` VALUES ('0000000006ff4e28000000006c885a32', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffb71281a2ffffffffd931eb95');
INSERT INTO `sys_role_menu` VALUES ('0000000043a67e9d00000000647ef271', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffc43097b8000000003a72e62f');
INSERT INTO `sys_role_menu` VALUES ('00000000257d3332ffffffffff7dfd87', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffdae3cd4affffffff9894b623');
INSERT INTO `sys_role_menu` VALUES ('fffffffff2b0e39cffffffffa5b49f53', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffddf9f51ffffffffff6157ca3');
INSERT INTO `sys_role_menu` VALUES ('000000002607c45700000000692f29be', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffdfaf333c000000001760f4a5');
INSERT INTO `sys_role_menu` VALUES ('ffffffffca6eb0bc0000000067d4b168', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffe56e8ef0ffffffff912af74a');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd4bce7fd000000007059a35b', 'ffffffffde90da3fffffffffdef7150f', 'fffffffff051bf04ffffffffa2ae3c8e');
INSERT INTO `sys_role_menu` VALUES ('00000000171b3488ffffffffbc99ca78', 'ffffffffde90da3fffffffffdef7150f', 'fffffffffaad7649ffffffffe7c3d6a6');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名|账号',
  `type` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '用户名|账号类型(1系统账号;2注册账号;)',
  `add_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `enabled` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否冻结（0正常1冻结）',
  `logintime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后登录时间',
  `times` int(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '已成功登录次数,默认为1，每次累加1',
  `error_count` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '登录错误次数',
  `error_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '记录登录错误的时刻',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('00000000440bf94e00000000173c7f4e', 'gzstyp', 1, '2020-04-30 19:00:27', 0, '2020-04-30 19:00:27', 1, 0, '2020-04-30 19:00:27');
INSERT INTO `sys_user` VALUES ('ffffffff81490e7cffffffffb1302917', 'a', 1, '2020-06-03 18:00:24', 0, '2020-06-03 18:00:24', 1, 0, '2020-06-03 18:00:24');
INSERT INTO `sys_user` VALUES ('ffffffff9b66521effffffff9d61cd5d', 'hgl', 1, '2020-05-01 02:47:54', 0, '2020-05-01 02:47:54', 1, 0, '2020-05-01 02:47:54');
INSERT INTO `sys_user` VALUES ('ffffffffbd471a55ffffffff976c6d1b', 'typ', 1, '2020-04-13 19:55:34', 0, '2020-04-29 23:41:27', 50, 0, '2020-04-29 23:41:27');
INSERT INTO `sys_user` VALUES ('ffffffffddf9f1ffffffffff88888888', 'admin', 1, '2020-04-15 22:39:30', 0, '2020-04-17 16:43:45', 5, 0, '2020-04-17 16:43:45');
INSERT INTO `sys_user` VALUES ('fffffffff9c4f265ffffffffdab33c59', 'sss', 1, '2020-05-01 15:18:10', 0, '2020-05-01 15:18:10', 1, 0, '2020-05-01 15:18:10');

-- ----------------------------
-- Table structure for sys_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_menu`;
CREATE TABLE `sys_user_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户菜单(私有菜单)id',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id(sys_user.kid)',
  `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单id(sys_menu.kid)',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户菜单(私有菜单)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_menu
-- ----------------------------
INSERT INTO `sys_user_menu` VALUES ('8b7aaa2c85ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_user_menu` VALUES ('8b7aafaa85ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', 'ffffffffe56e8ef0ffffffff912af74a');
INSERT INTO `sys_user_menu` VALUES ('8b7ab14485ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', '0000000006a9099300000000100d10cd');
INSERT INTO `sys_user_menu` VALUES ('8b7ab24785ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', '000000000ba93c43ffffffffe5c59508');
INSERT INTO `sys_user_menu` VALUES ('8b7ab2df85ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', '000000005c41138100000000288a6b45');
INSERT INTO `sys_user_menu` VALUES ('8b7ab3a885ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', '0000000058e2ec83000000002f978e03');
INSERT INTO `sys_user_menu` VALUES ('8b7ab44185ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', 'fffffffff051bf04ffffffffa2ae3c8e');
INSERT INTO `sys_user_menu` VALUES ('8b7ab53085ef11ea978d02004a400001', 'ffffffffab9fc98dffffffff8b642b39', '1111ffffe56e8ef0ffffffff912af111');
INSERT INTO `sys_user_menu` VALUES ('b4e910f18a2e11ea978d02004a400001', 'ffffffffbd471a55ffffffff976c6d1b', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_user_menu` VALUES ('b4e914968a2e11ea978d02004a400001', 'ffffffffbd471a55ffffffff976c6d1b', 'ffffffff8c468a55ffffffff8f59d635');
INSERT INTO `sys_user_menu` VALUES ('b4e9156e8a2e11ea978d02004a400001', 'ffffffffbd471a55ffffffff976c6d1b', '000000002d8402d60000000072f7dea2');

-- ----------------------------
-- Table structure for sys_user_password
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_password`;
CREATE TABLE `sys_user_password`  (
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'user主键(sys_user.kid)',
  `user_password` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号密码',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`user_password`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户密码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_password
-- ----------------------------
INSERT INTO `sys_user_password` VALUES ('ffffffffddf9f1ffffffffff88888888', '$2a$10$2swupMvymbSVqW1qHWaxCuI4rv/Er6EdSqlVTKqe.wTqgGUGhLaDm');
INSERT INTO `sys_user_password` VALUES ('ffffffff81490e7cffffffffb1302917', '$2a$10$aKvsfiUHBtL21s48a1wjSe6xrZSXd4RYL0xtPj9.aukBLnJTaXwnG');
INSERT INTO `sys_user_password` VALUES ('fffffffff9c4f265ffffffffdab33c59', '$2a$10$l1caMcY893hXNmStaLGQLe7Jj/SHKoOY7/vPSoehgxANoQgu8e/Gi');
INSERT INTO `sys_user_password` VALUES ('ffffffffbd471a55ffffffff976c6d1b', '$2a$10$RYzlUBbtZZNDzuylcZoTve8LXM7yxioBQUM94HhL0za6bVLb/slqW');
INSERT INTO `sys_user_password` VALUES ('00000000440bf94e00000000173c7f4e', '$2a$10$U/.CBNIMYm.OBQtuUTJjIuhcpmJJtaiKVZghQpDPqk2mbnpBCHWaC');
INSERT INTO `sys_user_password` VALUES ('ffffffff9b66521effffffff9d61cd5d', '$2a$10$VXX3DKF1cwPbyDlj7f.DWuM77fr7H4MX/BlHPw8d./q5cAQb2DC62');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名|账号的id(sys_user.kid)',
  `role_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色的id(sys_role.kid)',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_user_role`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('ffffffffa04b32a6ffffffffba40ffff', 'ffffffffddf9f1ffffffffff88888888', 'ffffffffa04b32a6ffffffffba40a72e');
INSERT INTO `sys_user_role` VALUES ('fffffffff051bf04ffffffffa2afffff', 'ffffffffddf9f1ffffffffff88888888', 'ffffffffde90da3fffffffffdef7150f');

-- ----------------------------
-- View structure for view_user_login
-- ----------------------------
DROP VIEW IF EXISTS `view_user_login`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_user_login` AS select `su`.`kid` AS `kid`,`su`.`user_name` AS `user_name`,`sup`.`user_password` AS `user_password`,`su`.`enabled` AS `enabled`,`su`.`error_count` AS `error_count`,`su`.`error_time` AS `error_time` from (`sys_user` `su` join `sys_user_password` `sup`) where (`su`.`kid` = `sup`.`user_id`);

SET FOREIGN_KEY_CHECKS = 1;
