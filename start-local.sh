#!/bin/bash

# Скрипт для быстрого запуска AIUEducation локально

echo "🚀 Запуск AIUEducation локально..."

# Проверка наличия Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не установлен. Пожалуйста, установите Docker."
    exit 1
fi

# Проверка наличия Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose не установлен. Пожалуйста, установите Docker Compose."
    exit 1
fi

# Выбор режима запуска
echo "Выберите режим запуска:"
echo "1) Простой запуск (только приложение + БД) - рекомендуется"
echo "2) Разработка (с hot reload и pgAdmin)"
echo "3) Локальное тестирование (с Nginx)"
echo "4) Продакшн режим"
read -p "Введите номер (1-4): " choice

case $choice in
    1)
        echo "🔧 Простой запуск..."
        docker-compose -f docker-compose.simple.yml down 2>/dev/null
        docker-compose -f docker-compose.simple.yml up --build
        ;;
    2)
        echo "🔧 Запуск в режиме разработки..."
        docker-compose -f docker-compose.dev.yml down 2>/dev/null
        docker-compose -f docker-compose.dev.yml up --build
        ;;
    3)
        echo "🌐 Запуск с Nginx..."
        docker-compose -f docker-compose.local.yml down 2>/dev/null
        docker-compose -f docker-compose.local.yml up --build
        ;;
    4)
        echo "🏭 Запуск в продакшн режиме..."
        if [ ! -f .env ]; then
            echo "⚠️  Файл .env не найден. Создаю с базовыми настройками..."
            cat > .env << EOF
SPRING_PROFILES_ACTIVE=prod
DATABASE_NAME=aiueducation_db
DATABASE_USERNAME=aiueducation_user
DATABASE_PASSWORD=aiueducation_pass
EOF
        fi
        docker-compose down 2>/dev/null
        docker-compose up --build
        ;;
    *)
        echo "❌ Неверный выбор. Запуск в простом режиме по умолчанию..."
        docker-compose -f docker-compose.simple.yml down 2>/dev/null
        docker-compose -f docker-compose.simple.yml up --build
        ;;
esac 