# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [0.1.18](https://github.com/limaofeng/asany-server/compare/v0.1.17...v0.1.18) (2022-05-04)

### [0.1.17](https://github.com/limaofeng/asany-server/compare/v0.1.16...v0.1.17) (2022-05-04)


### Features

* 修改密码接口 ([03aed8c](https://github.com/limaofeng/asany-server/commit/03aed8c24af7ca2d7fd131a5f9463a239094a3c5))
* 登录 Token 优化 ([d8d2f49](https://github.com/limaofeng/asany-server/commit/d8d2f49de28049d8e35f7d06365df46c61730586))

### [0.1.16](https://github.com/limaofeng/asany-server/compare/v0.1.15...v0.1.16) (2022-05-03)


### Features

* 数据字典相关代码 ([9a4f865](https://github.com/limaofeng/asany-server/commit/9a4f8658083c0921264360ac4941d418d602087f))
* 文件预览接口 ([8494088](https://github.com/limaofeng/asany-server/commit/849408847d6a66c2e303c142b0ddb08ff7931874))
* 门店,数据字典代码 ([d2700f1](https://github.com/limaofeng/asany-server/commit/d2700f19d0659dae035ae343e975df3bcc2204d3))
* 高德 IP 定位 + 门店数据导入测试类 ([f4f65ac](https://github.com/limaofeng/asany-server/commit/f4f65acf20bc6cd2b5c62aee6054aa9a3f980283))


### Bug Fixes

* 支持通过 ID 查询门店 ([24a0cbc](https://github.com/limaofeng/asany-server/commit/24a0cbc5419a0cd20414541d82dbd458f2b7f331))
* 登录接口返回 account 字段 ([3b04653](https://github.com/limaofeng/asany-server/commit/3b04653d95cc7a44368f1e73ab918023b80d4c9a))
* 高德 IP 定位接口 JSON 解析错误的问题 ([f13a071](https://github.com/limaofeng/asany-server/commit/f13a071c3f68bba13632a1a089f4a8f741ac1194))

### [0.1.14](https://github.com/limaofeng/asany-server/compare/v0.1.13...v0.1.14) (2022-04-08)


### Bug Fixes

* 解决 java 版本错误，导致运行失败 ([084f61b](https://github.com/limaofeng/asany-server/commit/084f61b046ad2544194b81c888309175292809c3))

### [0.1.13](https://github.com/limaofeng/asany-server/compare/v0.1.12...v0.1.13) (2022-04-08)


### Bug Fixes

* 解决构建镜像后的启动问题 ([0c9343b](https://github.com/limaofeng/asany-server/commit/0c9343be5d5a511dab94516bb77b9b0a9d76e72a))

### [0.1.12](https://github.com/limaofeng/asany-server/compare/v0.1.11...v0.1.12) (2022-04-08)

### [0.1.11](https://github.com/limaofeng/asany-server/compare/v0.1.10...v0.1.11) (2022-04-08)


### Features

* 添加提取缩略图的接口 ([8401b01](https://github.com/limaofeng/asany-server/commit/8401b01563cff96cf996b8996abe10a81febdb42))

### [0.1.10](https://github.com/limaofeng/asany-server/compare/v0.1.9...v0.1.10) (2022-04-06)


### Features

* 优化分片上传，使分片上传功能，更加合理 ([48ce829](https://github.com/limaofeng/asany-server/commit/48ce8296205f81217521aa4a7439a1d85e58b546))
* 优化视频的在线播放 ([c74e563](https://github.com/limaofeng/asany-server/commit/c74e563ff264d719183345696bb62833358a3da8))
* 完成单个文件的下载功能 ([d6e8eda](https://github.com/limaofeng/asany-server/commit/d6e8edadcfdeb432d95c63dc3981c72b871f7dfd))
* 添加下载 Controller ([53bbbb4](https://github.com/limaofeng/asany-server/commit/53bbbb44ea6e6bffd3fb3e6330f46406d302ea3c))
* 重构文件 Storage 逻辑 ([27af82a](https://github.com/limaofeng/asany-server/commit/27af82acdda13701390d09ef9ef819800bef8c3d))


### Bug Fixes

* download 优化, isKeepAlive 的错误用户 ([f803bd0](https://github.com/limaofeng/asany-server/commit/f803bd0012241876e530cb708fba924548291dfb))
* 为 Storage 添加 分片上传 API, 并实现基于 OSS 的分片上传逻辑 ([776fa9d](https://github.com/limaofeng/asany-server/commit/776fa9d241e5f2d1a0444b85a3fb94cb8b3e78f7))
* 优化上传逻辑 ([eb9baa8](https://github.com/limaofeng/asany-server/commit/eb9baa8fc99d809f55d1ddaddba6b9ac658586f7))
* 优化上传逻辑 ([1b1ab96](https://github.com/limaofeng/asany-server/commit/1b1ab96c6b383161f2311c9fb6cba76b5aad22b8))

### [0.1.9](https://github.com/limaofeng/asany-server/compare/v0.1.8...v0.1.9) (2022-03-26)


### Features

* 将回收站的默认名称更改为"$RECYCLE.BIN" ([9233fd2](https://github.com/limaofeng/asany-server/commit/9233fd2f9b5d18ebf976c39ced0610a50d34794a))


### Bug Fixes

* 优化清空回收站逻辑 ([5410e43](https://github.com/limaofeng/asany-server/commit/5410e43c7c242b8c53122ae6f22e45cad87ab461))

### [0.1.8](https://github.com/limaofeng/asany-server/compare/v0.1.7...v0.1.8) (2022-03-25)


### Features

* 清空回收站操作 ([afeecf4](https://github.com/limaofeng/asany-server/commit/afeecf493b02ec6d14ebba5facad2ccb72a14476))

### [0.1.7](https://github.com/limaofeng/asany-server/compare/v0.1.6...v0.1.7) (2022-03-21)

### [0.1.6](https://github.com/limaofeng/asany-server/compare/v0.1.5...v0.1.6) (2022-03-21)

### [0.1.5](https://github.com/limaofeng/asany-server/compare/v0.1.4...v0.1.5) (2022-03-21)


### Features

* 优化邮件发送接口, 添加邮件清空回收站接口 ([0444d79](https://github.com/limaofeng/asany-server/commit/0444d797317bc480c1c3c4659e4c1d0922f452e3))
* 发送邮件接口 ([ed4cce2](https://github.com/limaofeng/asany-server/commit/ed4cce262049abe9e00526be379e3997991f3e10))
* 接口文件对象 FileObject 添加 createdAt， description ([4832942](https://github.com/limaofeng/asany-server/commit/4832942ec3f0acca15bfb7a39be50bca2d9d5a49))
* 文件查询接口重构，支持复杂查询 ([91de34b](https://github.com/limaofeng/asany-server/commit/91de34badbb7f0ab01f68dd9ebb5f912eca714c5))
* 文件重命名接口 ([98e5bac](https://github.com/limaofeng/asany-server/commit/98e5bacf936c17f9a6de1ab7562a6aad8254947a))
* 新增新增与修改邮件接口 ([0a63c9b](https://github.com/limaofeng/asany-server/commit/0a63c9baa21098c4095a12f43e85b57513052def))
* 添加云盘模块 ([058d27f](https://github.com/limaofeng/asany-server/commit/058d27fe18ebf286f819e9760c87ba144afe16d5))
* 添加文件删除接口 ([aabb96e](https://github.com/limaofeng/asany-server/commit/aabb96e8f0fc9721b15fac01f94f77ff79669b76))
* 添加联系人接口 ([dc2de14](https://github.com/limaofeng/asany-server/commit/dc2de1473f8d684352a98c4f5d061938cf124ec3))
* 添加通讯录模块 ([be824e9](https://github.com/limaofeng/asany-server/commit/be824e9a2c1f0f4ffb5dfcb0653f4c6fa8257528))
* 邮件默认按照 internalDate 排序 ([74e558e](https://github.com/limaofeng/asany-server/commit/74e558ef1c3365ef4668b414e73af1f79135c6fc))


### Bug Fixes

* 修复文件列表排序问题 ([ff36d1b](https://github.com/limaofeng/asany-server/commit/ff36d1bb49e624f08c2c5b090f438c6307a601c5))

### [0.1.4](https://github.com/limaofeng/asany-server/compare/v0.1.3...v0.1.4) (2022-03-06)


### Features

* 优化日程查询 ([af5106f](https://github.com/limaofeng/asany-server/commit/af5106f3fe536e75ca2cdc07411d60cc3ef6b7f8))
* 优化日程查询 ([6d8d93c](https://github.com/limaofeng/asany-server/commit/6d8d93c5726c8baf4069a29bcede024cf765439a))
* 优化邮件查询接口，添加更多的邮件详情字段 ([7eb2e77](https://github.com/limaofeng/asany-server/commit/7eb2e771925d41025ccf879cb45c2161b714ca04))
* 启用缓存，邮箱返回统计计算字段 ([2fc1eb0](https://github.com/limaofeng/asany-server/commit/2fc1eb0d8e4e8bd670f90e5172e1007d441e38bd))
* 完成标记邮件及移动邮件功能 ([f7f4e4d](https://github.com/limaofeng/asany-server/commit/f7f4e4d422e0d0ff4fd19e6ce8225070654acee0))
* 日历新增修改逻辑 ([1377794](https://github.com/limaofeng/asany-server/commit/1377794b2d8c2fca871def3ba93cb11bc8584608))
* 添加日历账户表，新增日历集更新/删除接口 ([6548185](https://github.com/limaofeng/asany-server/commit/65481851c36d1f6037a86b26ec6efd773523dd6f))
* 邮件 Base64 显示错误 ([4170303](https://github.com/limaofeng/asany-server/commit/4170303ff6f4b212f9c690d0c76a391f5058129c))
* 邮箱设置/文件夹 ([4ef36fe](https://github.com/limaofeng/asany-server/commit/4ef36fe7736a9a565f3c411ca0b05f64a8ecf3fb))
* 邮箱设置/文件夹个人收藏 ([63a18d9](https://github.com/limaofeng/asany-server/commit/63a18d95ef1540402626914f8c2cdc90aa5ba32a))


### Bug Fixes

* 保存草稿及发送邮件功能 ([212ba4c](https://github.com/limaofeng/asany-server/commit/212ba4cc84573f4ddfebb649a73c74893191d663))
* 修复文件上传接口中的BUG ([e49538e](https://github.com/limaofeng/asany-server/commit/e49538ef019aa48fad0a30a1c37f4a1ce6eb805b))
* 解决日历事件查询时 limit -1,1 bug ([f7429a4](https://github.com/limaofeng/asany-server/commit/f7429a4cde88d2ec57892dd4d37e17352d202c20))
* 解决邮箱自定义文件夹新增BUG ([e6c6e4a](https://github.com/limaofeng/asany-server/commit/e6c6e4aafab785b3cf751463164a9569749f053d))

### [0.1.3](https://github.com/limaofeng/asany-server/compare/v0.1.2...v0.1.3) (2022-01-17)


### Bug Fixes

* 邮件模块, 修改配置读取方式 ([c8422f6](https://github.com/limaofeng/asany-server/commit/c8422f6d0af0000521de96314ea0f1b71b39a30b))

### [0.1.2](https://github.com/limaofeng/asany-server/compare/v0.1.1...v0.1.2) (2022-01-17)


### Bug Fixes

* jpa 外键定义冲突 ([729a16c](https://github.com/limaofeng/asany-server/commit/729a16cd21e06b1cffbe61bda55b991d38a808f7))

### [0.1.1](https://github.com/limaofeng/asany-server/compare/v0.1.0...v0.1.1) (2022-01-17)


### Features

* 保存发件箱 ([339fa72](https://github.com/limaofeng/asany-server/commit/339fa72a0885b6f537233d87ee96c543cb1150e5))
* 升级依赖 ([ac4d053](https://github.com/limaofeng/asany-server/commit/ac4d053c1134d1b3b3cfdc32ffa7e4217f71b8a6))
* 日历 - 添加删除日历方法 ([c09713c](https://github.com/limaofeng/asany-server/commit/c09713c30ac5b0aa12e25b7c3db0d26fc53a9a34))
* 添加日历事件查询接口 ([0a3cc67](https://github.com/limaofeng/asany-server/commit/0a3cc6744677813356a086c9220dcf43114725b5))
* 添加日历模块，优化邮件逻辑 ([cdbe749](https://github.com/limaofeng/asany-server/commit/cdbe749d8e916e7bc613e0f6f1cc83b38de9b51d))
* 组织 yml 导入 ([f2970ad](https://github.com/limaofeng/asany-server/commit/f2970adf90ab97ab4e6fe064cabdf351b791ee9e))
* 邮件模块 ([9d448d9](https://github.com/limaofeng/asany-server/commit/9d448d92bea3b234c40ade298b3aceb3f2e733b0))


### Bug Fixes

* 优化应用查询，提告一些场景下的速度 ([c327337](https://github.com/limaofeng/asany-server/commit/c327337d02f134490a3f694b7cba11ddd95161ac))
* 优化日历查询 ([14a34fd](https://github.com/limaofeng/asany-server/commit/14a34fd7aabdb6630138ea2e37ea5125b8d7e32f))
* 日历查询优化 ([5990e4e](https://github.com/limaofeng/asany-server/commit/5990e4e3d77ac2811d84d0b53ca85441ef2838ed))

## [0.1.0](https://github.com/limaofeng/asany-server/compare/v0.0.27...v0.1.0) (2021-12-22)


### Features

* 代码格式化 ([3879b2e](https://github.com/limaofeng/asany-server/commit/3879b2e9dfd144225c0a69c38d1d230618340d2a))

### [0.0.27](https://github.com/limaofeng/asany-server/compare/v0.0.26...v0.0.27) (2021-12-22)


### Features

* 添加 TeamMember ([7aa1a7e](https://github.com/limaofeng/asany-server/commit/7aa1a7e973b0d8deb4ea4c58022e6132e3e6f5a5))

### [0.0.26](https://github.com/limaofeng/asany-server/compare/v0.0.25...v0.0.26) (2021-12-21)


### Features

* 导入 CMS 模块时，可以一同导入文章 ([b320343](https://github.com/limaofeng/asany-server/commit/b320343b1943d857417914ef333e5d3aaa6475e4))

### [0.0.25](https://github.com/limaofeng/asany-server/compare/v0.0.24...v0.0.25) (2021-12-17)


### Bug Fixes

* 导入应用时, modules 可以不存在 ([662946d](https://github.com/limaofeng/asany-server/commit/662946d7feb0ff746a6df18b443c93a74bb783b9))
* 文章栏目接口可以返回该栏目文章 ([a54fc3d](https://github.com/limaofeng/asany-server/commit/a54fc3d9e06c1dd863387e65dbd4f3fa3ca83a6f))

### [0.0.24](https://github.com/limaofeng/asany-server/compare/v0.0.23...v0.0.24) (2021-12-08)


### Bug Fixes

* 修复 importApplication 接口 ([00a53d4](https://github.com/limaofeng/asany-server/commit/00a53d4b1a919a1009c33e3dfe6ce83f9be757e4))

### [0.0.23](https://github.com/limaofeng/asany-server/compare/v0.0.22...v0.0.23) (2021-12-08)


### Features

* 通过 yml 导入应用时，支持模块定义 ([2c1478f](https://github.com/limaofeng/asany-server/commit/2c1478f2e1d6eb50f39358d166cb6ff676548f77))

### [0.0.22](https://github.com/limaofeng/asany-server/compare/v0.0.21...v0.0.22) (2021-12-07)


### Features

* 文章添加 channel_startsWith ([850218b](https://github.com/limaofeng/asany-server/commit/850218b02c2a203b2fc6430a55215b7ed0816dc6))
* 添加 Licence 表 ([572c977](https://github.com/limaofeng/asany-server/commit/572c977033c93e36a4e0d6650f74c45ac010d421))

### [0.0.21](https://github.com/limaofeng/asany-server/compare/v0.0.20...v0.0.21) (2021-12-06)


### Features

* 文章查询接口优化 ([540d08d](https://github.com/limaofeng/asany-server/commit/540d08d414fb49370f8d3d5acdfc3b999ddde736))

### [0.0.20](https://github.com/limaofeng/asany-server/compare/v0.0.19...v0.0.20) (2021-12-05)


### Features

* 添加应用导入接口 ([dae8424](https://github.com/limaofeng/asany-server/commit/dae8424484d06b7f6114347ca94310a276f840db))

### [0.0.19](https://github.com/limaofeng/asany-server/compare/v0.0.18...v0.0.19) (2021-12-05)


### Features

* 完善菜单更新接口 ([cb48974](https://github.com/limaofeng/asany-server/commit/cb48974278ff37e0cc1b8c9e205dd6529ce94059))

### [0.0.18](https://github.com/limaofeng/asany-server/compare/v0.0.17...v0.0.18) (2021-12-04)


### Features

* schema 修改 ([e8846b5](https://github.com/limaofeng/asany-server/commit/e8846b58b9cde2e7c0339db67042eb65e46a7c66))
* 完善菜单新增与删除接口 ([25ea276](https://github.com/limaofeng/asany-server/commit/25ea27611426edc1fdb6dd63c32c2d6e3f35b2e2))
* 数据集逻辑完善 ([584e40a](https://github.com/limaofeng/asany-server/commit/584e40a2ee836cf1c5b609624a2e8738bb47dfb4))
* 添加 Query.dataset 接口 ([f11b4f9](https://github.com/limaofeng/asany-server/commit/f11b4f90fe405923e44cad1d940ec23e21b7142c))
* 添加数据源逻辑 ([b2ac38e](https://github.com/limaofeng/asany-server/commit/b2ac38e281186f2505db49e08731cbf1f1bc1f0c))
* 组件更新接口 ([2a1f328](https://github.com/limaofeng/asany-server/commit/2a1f328a06c564ff2692171c5494eb5f6b4b4bfb))
* 路由布局设置中可以设置隐藏菜单栏 ([cbf18ad](https://github.com/limaofeng/asany-server/commit/cbf18ad3e6d33f12e7df3ff0d55a423c6fde24bc))

### [0.0.17](https://github.com/limaofeng/asany-server/compare/v0.0.16...v0.0.17) (2021-11-02)


### Features

* 优化文章保存 ([81bed06](https://github.com/limaofeng/asany-server/commit/81bed062afbdcf4a6c6fea2f7e9b80db41474e79))
* 合并 org 模块到 security ([268a787](https://github.com/limaofeng/asany-server/commit/268a78724bc9a71190ab07f3328462c957da6ce9))
* 文件查看逻辑 ([6f7e305](https://github.com/limaofeng/asany-server/commit/6f7e305450bdeac34612651f88ea83ac3cb08bec))
* 文件管理接口优化 ([f1478e4](https://github.com/limaofeng/asany-server/commit/f1478e4326fde1e80ce65f9bef510beda04f9dff))
* 文件管理相关接口 ([d3b9d6a](https://github.com/limaofeng/asany-server/commit/d3b9d6ac1a25aa2a7f7df18ca96f098dc42e061a))
* 文件重建索引 ([67becde](https://github.com/limaofeng/asany-server/commit/67becdedf14a6c92e2cacff42bc5dfb3074f185a))
* 文章 code 更新 ([17230af](https://github.com/limaofeng/asany-server/commit/17230af6ec252dd512e398e3ce94be9cd8178938))
* 文章内容保存 ([3b8c3f2](https://github.com/limaofeng/asany-server/commit/3b8c3f2071a3cd9b5751b929bff4eb053c9c6645))
* 文章批量删除 ([c89217e](https://github.com/limaofeng/asany-server/commit/c89217ed186e1503693af458c76563abdc140657))
* 添加应用订阅逻辑 ([037b50c](https://github.com/limaofeng/asany-server/commit/037b50c0d5a5a2aef3763b984dc7df3f82f5ed6c))
* 用户角色优化 ([0f99a63](https://github.com/limaofeng/asany-server/commit/0f99a638747d9e0afa2e6e3139809812591987c7))
* 调整文章特征逻辑 ([473ed58](https://github.com/limaofeng/asany-server/commit/473ed583da7813be6ce53180a586cf8696e1fa3c))
* 路由支持 layout 设置 ([7062043](https://github.com/limaofeng/asany-server/commit/706204383c0ed9b180657c8a27894b6ea9d4cf22))


### Bug Fixes

* 解决 ApplicationRoute 转换 BUG ([d760aa5](https://github.com/limaofeng/asany-server/commit/d760aa5f2aa8d08a3bf15c2937f5f03120fc36eb))
* 解决 EmployeeIdType 导致启动 BUG ([36aae8e](https://github.com/limaofeng/asany-server/commit/36aae8eea9b58a81bafefb00f003fb2cc4c87d6e))
* 解决 GraphQL FileObject 转换 BUG ([21d47df](https://github.com/limaofeng/asany-server/commit/21d47dfd44258d2f21bd7336c5ef8423538ebebd))
* 解决 GraphQL FileObject 转换 BUG ([dfc64eb](https://github.com/limaofeng/asany-server/commit/dfc64eb0fcb0eeca562ac9b6a1df6ce6f3eedfc7))

### [0.0.16](https://github.com/limaofeng/asany-server/compare/v0.0.15...v0.0.16) (2021-09-18)


### Bug Fixes

* 解决 cn.asany.*.*.runner 未加载的问题 ([50be23a](https://github.com/limaofeng/asany-server/commit/50be23a843607117dde3136b75b0032b40cf99b5))

### [0.0.15](https://github.com/limaofeng/asany-server/compare/v0.0.14...v0.0.15) (2021-09-17)


### Features

* 为 Banner 添加 subtitle 字段 ([d270348](https://github.com/limaofeng/asany-server/commit/d270348e1c64b0848797c16bff1f814b353aac75))

### [0.0.14](https://github.com/limaofeng/asany-server/compare/v0.0.13...v0.0.14) (2021-09-16)

### [0.0.13](https://github.com/limaofeng/asany-server/compare/v0.0.12...v0.0.13) (2021-09-16)


### Features

* 添加 Banner 接口 ([a14814c](https://github.com/limaofeng/asany-server/commit/a14814c8a7ae272d031541f876dda9b00b2b68fa))
* 添加 CMS 模块 ([125c0f3](https://github.com/limaofeng/asany-server/commit/125c0f3c3b86acd53d7c203e3b2ba21f46c8a79a))
* 添加 FileObjectFormatDirective 指令 ([587ae87](https://github.com/limaofeng/asany-server/commit/587ae875db4c6b3a6b0fbc7c0c61c24b0ad5aae4))
* 添加菜单及banner ([4b35c70](https://github.com/limaofeng/asany-server/commit/4b35c70dd388493ccf78377671f9c62b1ace23e5))


### Bug Fixes

* 解决 CMS 模块编译问题 ([41918b9](https://github.com/limaofeng/asany-server/commit/41918b978f081e179e5903c1283c717ae6ff007d))

## [0.0.10](https://github.com/limaofeng/asany-server/compare/v0.0.9...v0.0.10) (2021-07-29)


### Features

* importIcons 返回值修改未 Icon[] ([d1d75f1](https://github.com/limaofeng/asany-server/commit/d1d75f1471ca71260faf58cfd5eb582dd800e81e))
* 优化同步时的日志记录方式 ([ef4befb](https://github.com/limaofeng/asany-server/commit/ef4befbe96125fa28bedee0c6f9365a68c533ad7))
* 删除 deleteLibrary 方法 ([dee69b5](https://github.com/limaofeng/asany-server/commit/dee69b5a6db38db66d83cd1812eaaa2b11f01ef4))
* 图标库及图标的维护接口 ([1e322ed](https://github.com/limaofeng/asany-server/commit/1e322ed7094779c69c11f4c08d82903c414c4bc3))
* 添加操作日志 ([c2693a8](https://github.com/limaofeng/asany-server/commit/c2693a88776bad9b500ee507c94e976ec922a91e))
