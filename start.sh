#!/bin/bash

# 退票 AI 代理系统启动脚本
# 使用 JDK 21

echo "==================================="
echo "退票 AI 代理系统启动脚本"
echo "==================================="

# 检查 JDK 21
echo "检查 Java 版本..."

# macOS/Linux 设置 JAVA_HOME
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null)
    if [ -z "$JAVA_HOME" ]; then
        echo "错误: 未找到 JDK 21"
        echo "请安装 JDK 21: https://www.oracle.com/java/technologies/downloads/#java21"
        exit 1
    fi
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux - 需要手动设置
    if [ -z "$JAVA_HOME" ]; then
        echo "请设置 JAVA_HOME 环境变量指向 JDK 21"
        exit 1
    fi
fi

echo "使用 JAVA_HOME: $JAVA_HOME"
$JAVA_HOME/bin/java -version

# 检查 API Key
if [ -z "$AI_API_KEY" ]; then
    echo ""
    echo "警告: 未设置 AI_API_KEY 环境变量"
    echo "请设置: export AI_API_KEY=your-api-key"
    echo ""
fi

# 构建项目
echo ""
echo "构建项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "构建失败!"
    exit 1
fi

# 启动应用
echo ""
echo "启动应用..."
cd refund-ticket-boot
mvn spring-boot:run
