
# 一个简单博客系统示例.

此项目是一个简单的博客系统，提供一些用户管理和博客文章管理。目的是演示如何做一个更加 Pythonic 的项目。

如果您有任何意见和建议，欢迎开启 ISSUE 发起讨论。期待与您打造更加完美的 Python 示例。

## 协作开发

- Fork 仓库
- 编写代码，测试，提交
- 发起 PR
- 审核通过后合并，协作完成



# 学习
[1.FastAPI 入门教程 ](https://blog.csdn.net/lilygg/article/details/110525068)

[2.Python 项目工程化开发指南 ](https://pyloong.github.io/pythonic-project-guidelines/practices/web/)

[3.使用FastAPI来开发项目，项目的目录结构如何规划的一些参考和基类封装的一些处理 ](https://www.cnblogs.com/wuhuacong/p/18380808)


# 踩坑集锦
[1.fastapi 使用本地静态文件替换 swagger cdn ](https://segmentfault.com/a/1190000041855999?sort=newest)

[2.Fastapi访问/docs和/redoc接口文档显示空白或无法加载 ](https://zhuanlan.zhihu.com/p/520541957)

[3.fastapi swagger-ui报错 ](https://blog.csdn.net/Lyn_10086/article/details/143201273)


# 目录结构
```
best-msc-python/
├── app/
│   ├── __init__.py
│   ├── main.py            # 入口文件
│   ├── core/
│   │   ├── __init__.py
│   │   ├── config.py      # 配置文件
│   │   ├── security.py    # 安全相关
│   │   └── ...            # 其他核心功能
│   ├── api/
│   │   ├── __init__.py
│   │   ├── v1/
│   │   │   ├── __init__.py
│   │   │   ├── endpoints/
│   │   │   │   ├── __init__.py
│   │   │   │   ├── users.py     # 用户相关接口
│   │   │   │   ├── items.py     # 其他接口
│   │   │   │   └── ...
│   │   │   └── ...              # 其他版本的API
│   ├── models/
│   │   ├── __init__.py
│   │   ├── user.py         # 用户模型
│   │   ├── item.py         # 其他模型
│   │   └── ...
│   ├── schemas/
│   │   ├── __init__.py
│   │   ├── user.py         # 用户数据模型
│   │   ├── item.py         # 其他数据模型
│   │   └── ...
│   ├── crud/
│   │   ├── __init__.py
│   │   ├── user.py         # 用户CRUD操作
│   │   ├── item.py         # 其他CRUD操作
│   │   └── ...
│   ├── db/
│   │   ├── __init__.py
│   │   ├── base.py         # 数据库基础设置
│   │   ├── session.py      # 数据库会话
│   │   └── ...
│   ├── tests/
│   │   ├── __init__.py
│   │   ├── test_main.py    # 测试主文件
│   │   ├── test_users.py   # 用户相关测试
│   │   └── ...
│   └── utils/
│       ├── __init__.py
│       ├── utils.py        # 工具函数
│       └── ...
├── .env                    # 环境变量文件
├── alembic/                # 数据库迁移工具目录
│   ├── env.py
│   ├── script.py.mako
│   └── versions/
│       └── ...
├── alembic.ini             # Alembic 配置文件
├── requirements.txt        # 项目依赖
├── Dockerfile              # Docker 配置文件
└── README.md               # 项目说明文件
```


