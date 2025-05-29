# 🚀 ДЕПЛОЙ AIUEducation НА СЕРВЕР

## ✅ ГОТОВАЯ КОНФИГУРАЦИЯ:
- **Nginx** на порту 80 
- **Spring Boot** приложение
- **PostgreSQL** база данных
- **Docker Compose** оркестрация

## 📋 КОМАНДЫ ДЛЯ ДЕПЛОЯ:

### 1. Создай .env файл:
```bash
echo "DATABASE_PASSWORD=aiueducation123" > .env
echo "SPRING_PROFILES_ACTIVE=prod" >> .env  
echo "DATABASE_USERNAME=aiueducation" >> .env
echo "DATABASE_NAME=aiueducation_prod" >> .env
```

### 2. Останови системный nginx (если есть):
```bash
sudo systemctl stop nginx
sudo systemctl disable nginx
```

### 3. Очисти старые контейнеры:
```bash
docker-compose down --remove-orphans
docker volume rm demo_postgres_data 2>/dev/null || true
docker system prune -f
```

### 4. Запусти проект:
```bash
docker-compose up --build -d
```

### 5. Проверь статус:
```bash
docker-compose ps
curl -I http://localhost
```

## 🌐 РЕЗУЛЬТАТ:
- **Сайт**: http://your-server-ip
- **Nginx**: Порт 80 (HTTP)
- **Приложение**: Внутренний порт 8080
- **База данных**: Внутренний порт 5432

## 📊 МОНИТОРИНГ:
```bash
# Логи всех сервисов
docker-compose logs -f

# Логи конкретного сервиса  
docker-compose logs nginx
docker-compose logs app
docker-compose logs db

# Статус контейнеров
docker-compose ps
```

## 🔧 УПРАВЛЕНИЕ:
```bash
# Остановить
docker-compose down

# Перезапустить
docker-compose restart

# Обновить код
git pull && docker-compose up --build -d
```

## ⚡ ОСОБЕННОСТИ:
- **Автоматический рестарт** контейнеров
- **Health checks** для приложения
- **Rate limiting** в Nginx
- **Security headers** настроены
- **Gzip сжатие** включено
- **Логирование** настроено

## 🎯 ГОТОВО К ПРОДАКШЕНУ! 