# 🚀 БЫСТРЫЙ ДЕПЛОЙ НА СЕРВЕРЕ

## 1. Создай .env файл:
```bash
cat > .env << 'EOF'
DATABASE_PASSWORD=aiueducation123
SPRING_PROFILES_ACTIVE=prod
DATABASE_USERNAME=aiueducation
DATABASE_NAME=aiueducation_prod
EOF
```

## 2. Удали старый compose.yaml:
```bash
rm -f compose.yaml
```

## 3. Очисти старые контейнеры:
```bash
docker-compose down --remove-orphans
docker system prune -f
```

## 4. Обнови Docker Compose:
```bash
# Удали старую версию
apt remove docker-compose -y

# Установи новую
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Проверь версию
docker-compose --version
```

## 5. Запусти:
```bash
docker-compose up --build -d
```

## 6. Проверь:
```bash
docker-compose ps
curl http://localhost:8080
```

---

## 🔥 ЕСЛИ НЕ РАБОТАЕТ - АЛЬТЕРНАТИВА:

Используй docker без compose:

```bash
# Запусти базу данных
docker run -d \
  --name aiueducation-db \
  -e POSTGRES_DB=aiueducation_prod \
  -e POSTGRES_USER=aiueducation \
  -e POSTGRES_PASSWORD=aiueducation123 \
  -p 5432:5432 \
  postgres:15-alpine

# Собери приложение
docker build -t aiueducation-app .

# Запусти приложение
docker run -d \
  --name aiueducation-app \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://aiueducation-db:5432/aiueducation_prod \
  -e DATABASE_USERNAME=aiueducation \
  -e DATABASE_PASSWORD=aiueducation123 \
  -p 8080:8080 \
  --link aiueducation-db \
  aiueducation-app
```

## ✅ Готово!
Сайт: http://your-ip:8080 